package com.example.testapp.ui.game.logic

import kotlin.random.Random

class Board(
    val rows: Int = 6,
    val cols: Int = 6,
    private val random: Random = Random.Default,
) {
    private val grid = Array(rows) { arrayOfNulls<Gem>(cols) }
    private var nextId = 0L

    fun fill() {
        do {
            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    grid[row][col] = newGem(typeWithoutMatch(row, col), spawnRow = row - rows)
                }
            }
        } while (!hasPossibleMove())
    }

    fun gems(): List<PlacedGem> = buildList {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                grid[row][col]?.let { add(PlacedGem(it, Cell(row, col))) }
            }
        }
    }

    fun contains(cell: Cell): Boolean =
        cell.row in 0 until rows && cell.col in 0 until cols

    fun swap(a: Cell, b: Cell) {
        val gem = grid[a.row][a.col]
        grid[a.row][a.col] = grid[b.row][b.col]
        grid[b.row][b.col] = gem
    }

    fun findMatches(): Set<Cell> = findLines().flatten().toSet()

    fun findLines(): List<List<Cell>> {
        val result = mutableListOf<List<Cell>>()
        for (row in 0 until rows) {
            collectLines(length = cols, result = result) { i -> Cell(row, i) }
        }
        for (col in 0 until cols) {
            collectLines(length = rows, result = result) { i -> Cell(i, col) }
        }
        return result
    }

    fun remove(cells: Set<Cell>): List<GemType> = cells.mapNotNull { cell ->
        val gem = grid[cell.row][cell.col]
        grid[cell.row][cell.col] = null
        gem?.type
    }

    fun collapse() {
        for (col in 0 until cols) {
            var targetRow = rows - 1
            for (row in rows - 1 downTo 0) {
                val gem = grid[row][col] ?: continue
                grid[row][col] = null
                grid[targetRow][col] = gem
                targetRow--
            }
            val missing = targetRow + 1
            for (row in 0..targetRow) {
                grid[row][col] = newGem(GemType.entries.random(random), spawnRow = row - missing)
            }
        }
    }

    fun findPossibleMove(): Pair<Cell, Cell>? {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val cell = Cell(row, col)
                for (neighbour in listOf(Cell(row, col + 1), Cell(row + 1, col))) {
                    if (!contains(neighbour)) continue
                    swap(cell, neighbour)
                    val hasMatch = findMatches().isNotEmpty()
                    swap(cell, neighbour)
                    if (hasMatch) return cell to neighbour
                }
            }
        }
        return null
    }

    fun hasPossibleMove(): Boolean = findPossibleMove() != null

    fun shuffle() {
        val all = grid.flatMap { it.filterNotNull() }
        do {
            val shuffled = all.shuffled(random)
            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    grid[row][col] = shuffled[row * cols + col]
                }
            }
        } while (findMatches().isNotEmpty() || !hasPossibleMove())
    }

    private inline fun collectLines(length: Int, result: MutableList<List<Cell>>, cellAt: (Int) -> Cell) {
        var start = 0
        while (start < length) {
            val type = typeAt(cellAt(start))
            var end = start + 1
            while (end < length && type != null && typeAt(cellAt(end)) == type) end++
            if (type != null && end - start >= 3) {
                result.add((start until end).map(cellAt))
            }
            start = end
        }
    }

    private fun typeAt(cell: Cell): GemType? = grid[cell.row][cell.col]?.type

    private fun typeWithoutMatch(row: Int, col: Int): GemType {
        val banned = mutableSetOf<GemType>()
        if (col >= 2 && grid[row][col - 1]?.type == grid[row][col - 2]?.type) {
            grid[row][col - 1]?.type?.let(banned::add)
        }
        if (row >= 2 && grid[row - 1][col]?.type == grid[row - 2][col]?.type) {
            grid[row - 1][col]?.type?.let(banned::add)
        }
        return (GemType.entries - banned).random(random)
    }

    private fun newGem(type: GemType, spawnRow: Int) = Gem(id = nextId++, type = type, spawnRow = spawnRow)
}
