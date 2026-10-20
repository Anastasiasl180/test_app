package com.example.testapp.ui.game.ui_components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import coil3.compose.AsyncImage
import com.example.testapp.ui.game.GameTimings
import com.example.testapp.ui.game.logic.Cell
import com.example.testapp.ui.game.logic.PlacedGem
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

private const val GEM_FILL = 0.9f

private const val SWIPE_THRESHOLD = 0.35f

@Composable
fun GameBoard(
    rows: Int,
    cols: Int,
    gems: List<PlacedGem>,
    selected: Cell?,
    hintIds: Set<Long>,
    matchedIds: Set<Long>,
    onTap: (Cell) -> Unit,
    onSwipe: (Cell, Cell) -> Unit,
) {
    val currentOnTap by rememberUpdatedState(onTap)
    val currentOnSwipe by rememberUpdatedState(onSwipe)

    Box(
        modifier = Modifier
            .fillMaxWidth(0.93f)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .aspectRatio(cols.toFloat() / rows)
                .clipToBounds()
                .pointerInput(rows, cols) {
                    val cellPx = size.width.toFloat() / cols
                    fun cellAt(offset: Offset) = Cell(
                        row = (offset.y / cellPx).toInt().coerceIn(0, rows - 1),
                        col = (offset.x / cellPx).toInt().coerceIn(0, cols - 1)
                    )
                    detectTapGestures { currentOnTap(cellAt(it)) }
                }
                .pointerInput(rows, cols) {
                    val cellPx = size.width.toFloat() / cols
                    var start = Cell(0, 0)
                    var drag = Offset.Zero
                    var swiped = false
                    detectDragGestures(
                        onDragStart = { offset ->
                            start = Cell(
                                row = (offset.y / cellPx).toInt().coerceIn(0, rows - 1),
                                col = (offset.x / cellPx).toInt().coerceIn(0, cols - 1)
                            )
                            drag = Offset.Zero
                            swiped = false
                        },
                        onDrag = { change, amount ->
                            change.consume()
                            if (swiped) return@detectDragGestures
                            drag += amount
                            if (drag.getDistance() < cellPx * SWIPE_THRESHOLD) return@detectDragGestures
                            val target = if (abs(drag.x) > abs(drag.y)) {
                                Cell(start.row, start.col + drag.x.sign.toInt())
                            } else {
                                Cell(start.row + drag.y.sign.toInt(), start.col)
                            }
                            swiped = true
                            currentOnSwipe(start, target)
                        }
                    )
                }
        ) {
            val cellSize = maxWidth / cols
            val cellPx = constraints.maxWidth.toFloat() / cols
            gems.forEach { placed ->
                key(placed.gem.id) {
                    BoardGem(
                        placed = placed,
                        cellSize = cellSize,
                        cellPx = cellPx,
                        isSelected = placed.cell == selected,
                        isHinted = placed.gem.id in hintIds,
                        isMatched = placed.gem.id in matchedIds,
                    )
                }
            }
        }
    }
}

@Composable
private fun BoardGem(
    placed: PlacedGem,
    cellSize: Dp,
    cellPx: Float,
    isSelected: Boolean,
    isHinted: Boolean,
    isMatched: Boolean,
) {
    val col by animateFloatAsState(
        targetValue = placed.cell.col.toFloat(),
        animationSpec = tween(GameTimings.SWAP_MS.toInt()),
        label = ""
    )
    val row = remember { Animatable(placed.gem.spawnRow.toFloat()) }
    LaunchedEffect(placed.cell.row) {
        row.animateTo(placed.cell.row.toFloat(), tween(GameTimings.FALL_MS.toInt()))
    }

    val scale by animateFloatAsState(
        targetValue = when {
            isMatched -> 0f
            isSelected -> 1.12f
            else -> 1f
        },
        animationSpec = tween(GameTimings.MATCH_MS.toInt()),
        label = ""
    )
    val hintPulse = if (isHinted) {
        val transition = rememberInfiniteTransition(label = "")
        val pulse by transition.animateFloat(
            initialValue = 1f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(tween(450), RepeatMode.Reverse),
            label = ""
        )
        pulse
    } else {
        1f
    }

    AsyncImage(
        model = placed.gem.type.image,
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .offset { IntOffset((col * cellPx).roundToInt(), (row.value * cellPx).roundToInt()) }
            .size(cellSize)
            .graphicsLayer {
                val total = GEM_FILL * scale * hintPulse
                scaleX = total
                scaleY = total
            }
    )
}
