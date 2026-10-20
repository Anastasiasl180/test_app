package com.example.testapp.ui.game

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.ui.game.logic.Achievement
import com.example.testapp.ui.game.logic.Board
import com.example.testapp.ui.game.logic.Cell
import com.example.testapp.ui.game.logic.GemType
import com.example.testapp.ui.game.logic.LevelConfig
import com.example.testapp.ui.game.logic.PlacedGem
import com.example.testapp.storage.AppStorage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class GameViewModel(private val storage: AppStorage) : ViewModel() {

    private val board = Board()
    private var config = LevelConfig.forLevel(storage.loadProgress().selectedLevel)
    private var turnJob: Job? = null
    private var bonusGem = storage.loadPlayer().equippedSkin.bonusGem
    private var cascadesInLevel = 0

    private val _state = mutableStateOf(GameUiState())
    val state: State<GameUiState> = _state

    private val _event = MutableSharedFlow<GameUiEvent>()
    val event = _event.asSharedFlow()

    init {
        startLevel(config.level)
    }

    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.TapCell -> onTap(event.cell)
            is GameEvent.SwapGems -> trySwap(event.from, event.to)
            GameEvent.Hint -> showHint()
            GameEvent.Shuffle -> shuffle()
            GameEvent.Pause -> {
                if (_state.value.dialog == null) update { copy(dialog = GameDialog.Pause) }
            }

            GameEvent.Resume -> {
                if (_state.value.dialog == GameDialog.Pause) update { copy(dialog = null) }
            }

            GameEvent.Restart -> startLevel(config.level)
            GameEvent.NextLevel -> {
                val next = config.level + 1
                if (next > LevelConfig.LEVELS_COUNT) return
                storage.persistProgress(storage.loadProgress().copy(selectedLevel = next))
                startLevel(next)
            }

            GameEvent.OpenSettings -> {
                viewModelScope.launch {
                    _event.emit(GameUiEvent.NavigateToSettings)
                }
            }

            GameEvent.NavigateHome -> {
                viewModelScope.launch {
                    _event.emit(GameUiEvent.NavigateHome)
                }
            }
        }
    }

    private fun startLevel(level: Int) {
        turnJob?.cancel()
        config = LevelConfig.forLevel(level)
        bonusGem = storage.loadPlayer().equippedSkin.bonusGem
        cascadesInLevel = 0
        board.fill()
        _state.value = GameUiState(
            level = level,
            rows = board.rows,
            cols = board.cols,
            gems = board.gems(),
            movesLeft = config.moves,
            goal = GoalProgress(config.goalType, config.goalCount),
            balance = storage.loadPlayer().balance,
        )
    }

    private fun onTap(cell: Cell) {
        val current = _state.value
        if (current.isBusy || current.dialog != null) return
        val selected = current.selected
        when {
            selected == null -> update { copy(selected = cell) }
            selected == cell -> update { copy(selected = null) }
            selected.isNeighbourOf(cell) -> trySwap(selected, cell)
            else -> update { copy(selected = cell) }
        }
    }

    private fun trySwap(from: Cell, to: Cell) {
        val current = _state.value
        if (current.isBusy || current.dialog != null) return
        if (!board.contains(from) || !board.contains(to) || !from.isNeighbourOf(to)) return

        turnJob = viewModelScope.launch {
            update { copy(isBusy = true, selected = null, hintIds = emptySet()) }
            board.swap(from, to)
            publishBoard()
            delay(GameTimings.SWAP_MS)

            if (board.findMatches().isEmpty()) {
                board.swap(from, to)
                publishBoard()
                delay(GameTimings.SWAP_MS)
                update { copy(isBusy = false) }
                return@launch
            }

            update { copy(movesLeft = movesLeft - 1) }
            resolveMatches()
            finishTurn()
        }
    }

    private suspend fun resolveMatches() {
        var cascade = 1
        while (true) {
            val lines = board.findLines()
            if (lines.isEmpty()) return
            unlock(Achievement.FirstMatch)
            if (cascade > 1 && ++cascadesInLevel >= COMBO_KING_CASCADES) unlock(Achievement.ComboKing)
            val matches = lines.flatten().toSet()
            val lineBonus = lines.sumOf { line ->
                when {
                    line.size >= 5 -> POINTS_FOR_FIVE
                    line.size == 4 -> POINTS_FOR_FOUR
                    else -> 0
                }
            }

            val matchedIds = board.gems()
                .filter { it.cell in matches }
                .map { it.gem.id }
                .toSet()
            update { copy(matchedIds = matchedIds) }
            delay(GameTimings.MATCH_MS)

            val removed = board.remove(matches)
            board.collapse()
            update {
                copy(
                    matchedIds = emptySet(),
                    score = score + removed.sumOf { gemPoints(it) } * cascade + lineBonus,
                    goal = goal.copy(collected = goal.collected + removed.count { it == goal.type })
                )
            }
            publishBoard()
            delay(GameTimings.FALL_MS)
            cascade++
        }
    }

    private suspend fun finishTurn() {
        val current = _state.value
        when {
            current.goal.isDone || current.movesLeft <= 0 -> finishLevel()

            else -> {
                if (!board.hasPossibleMove()) {
                    board.shuffle()
                    publishBoard()
                    delay(GameTimings.SWAP_MS)
                }
                update { copy(isBusy = false) }
            }
        }
    }

    private fun finishLevel() {
        val current = _state.value
        val stars = LevelConfig.starsFor(current.goal.collected, current.goal.target)
        val isWin = stars > 0
        val score = current.score + if (current.goal.isDone) current.movesLeft * POINTS_PER_MOVE_LEFT else 0
        val reward = stars * COINS_PER_STAR

        if (isWin) {
            val progress = storage.loadProgress()
            val bestStars = progress.levelStars[config.level] ?: 0
            val oldBest = progress.bestScores[config.level] ?: 0
            storage.persistProgress(
                progress.copy(
                    completedLevels = progress.completedLevels + config.level,
                    levelStars = progress.levelStars + (config.level to maxOf(stars, bestStars)),
                    bestScores = progress.bestScores + (config.level to maxOf(score, oldBest))
                )
            )
            if (current.goal.isDone && current.movesLeft > 0) unlock(Achievement.Flawless)
            if (storage.loadProgress().completedLevels.size >= LevelConfig.MARATHON_LEVELS) unlock(Achievement.Marathon)
        }
        val balance = changeBalance(reward)

        update {
            copy(
                isBusy = false,
                score = score,
                balance = balance,
                dialog = GameDialog.Result(
                    score = score,
                    stars = stars,
                    reward = reward,
                    hasNextLevel = isWin && config.level < LevelConfig.LEVELS_COUNT,
                    isWin = isWin
                )
            )
        }
    }

    private fun showHint() {
        val current = _state.value
        if (current.isBusy || current.dialog != null || current.hintIds.isNotEmpty()) return
        if (current.balance < HINT_COST) return notEnoughCoins()
        val (a, b) = board.findPossibleMove() ?: return
        val ids = board.gems()
            .filter { it.cell == a || it.cell == b }
            .map { it.gem.id }
            .toSet()
        update { copy(hintIds = ids, balance = changeBalance(-HINT_COST)) }
    }

    private fun shuffle() {
        val current = _state.value
        if (current.isBusy || current.dialog != null) return
        if (current.balance < SHUFFLE_COST) return notEnoughCoins()
        board.shuffle()
        update {
            copy(
                selected = null,
                hintIds = emptySet(),
                balance = changeBalance(-SHUFFLE_COST)
            )
        }
        publishBoard()
    }

    private fun gemPoints(type: GemType): Int =
        if (type == bonusGem) POINTS_PER_GEM * 2 else POINTS_PER_GEM

    private fun unlock(achievement: Achievement) {
        val progress = storage.loadProgress()
        if (achievement in progress.achievements) return
        storage.persistProgress(progress.copy(achievements = progress.achievements + achievement))
    }

    private fun notEnoughCoins() {
        viewModelScope.launch {
            _event.emit(GameUiEvent.NotEnoughCoins)
        }
    }

    private fun changeBalance(delta: Int): Int {
        val player = storage.loadPlayer()
        val balance = player.balance + delta
        storage.persistPlayer(player.copy(balance = balance))
        return balance
    }

    private fun publishBoard() = update { copy(gems = board.gems()) }

    private inline fun update(block: GameUiState.() -> GameUiState) {
        _state.value = _state.value.block()
    }

    companion object {
        const val HINT_COST = 30
        const val SHUFFLE_COST = 60
        private const val POINTS_PER_GEM = 10
        private const val POINTS_FOR_FOUR = 50
        private const val POINTS_FOR_FIVE = 100
        private const val POINTS_PER_MOVE_LEFT = 50
        private const val COINS_PER_STAR = 50
        private const val COMBO_KING_CASCADES = 5
    }
}

object GameTimings {
    const val SWAP_MS = 180L
    const val MATCH_MS = 200L
    const val FALL_MS = 280L
}

data class GameUiState(
    val level: Int = 1,
    val rows: Int = 6,
    val cols: Int = 6,
    val gems: List<PlacedGem> = emptyList(),
    val score: Int = 0,
    val movesLeft: Int = 0,
    val goal: GoalProgress = GoalProgress(GemType.Red, 0),
    val balance: Int = 0,
    val selected: Cell? = null,
    val hintIds: Set<Long> = emptySet(),
    val matchedIds: Set<Long> = emptySet(),
    val isBusy: Boolean = false,
    val dialog: GameDialog? = null,
)

data class GoalProgress(
    val type: GemType,
    val target: Int,
    val collected: Int = 0,
) {
    val left: Int get() = (target - collected).coerceAtLeast(0)
    val isDone: Boolean get() = left == 0
}

sealed interface GameDialog {
    data object Pause : GameDialog
    data class Result(
        val score: Int,
        val stars: Int,
        val reward: Int,
        val hasNextLevel: Boolean,
        val isWin: Boolean,
    ) : GameDialog
}

sealed interface GameEvent {
    data class TapCell(val cell: Cell) : GameEvent
    data class SwapGems(val from: Cell, val to: Cell) : GameEvent
    data object Hint : GameEvent
    data object Shuffle : GameEvent
    data object Pause : GameEvent
    data object Resume : GameEvent
    data object Restart : GameEvent
    data object NextLevel : GameEvent
    data object OpenSettings : GameEvent
    data object NavigateHome : GameEvent
}

sealed class GameUiEvent {
    data object NavigateHome : GameUiEvent()
    data object NavigateToSettings : GameUiEvent()
    data object NotEnoughCoins : GameUiEvent()
}
