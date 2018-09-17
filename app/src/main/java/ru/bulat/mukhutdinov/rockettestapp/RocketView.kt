package ru.bulat.mukhutdinov.rockettestapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class RocketView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var speed = 1
    var cellsMatrix
        get() = _cellsMatrix
        set(value) {
            _cellsMatrix = Array(value.size) { value[it].copyOf() }
            xCellSize = measuredWidth / value.size
            yCellSize = measuredHeight / value.size
            invalidate()
            redrawingHandler.removeCallbacksAndMessages(null)
            isRedrawFinished = true
        }


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val redrawingHandler = Handler()

    private var _cellsMatrix = arrayOf<IntArray>()
    private var isRedrawFinished = true
    private var xCellSize = 0
    private var yCellSize = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (_cellsMatrix.isNotEmpty()) {
            paint.style = Paint.Style.FILL

            _cellsMatrix.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, value ->
                    if (value == 0) {
                        paint.color = Color.YELLOW
                    } else {
                        paint.color = Color.BLACK
                    }

                    canvas.drawRect(columnIndex.toFloat() * xCellSize, rowIndex.toFloat() * yCellSize,
                            (columnIndex + 1).toFloat() * xCellSize, (rowIndex + 1).toFloat() * yCellSize, paint)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        this.setMeasuredDimension(measuredWidth, measuredHeight)
    }

    fun redrawClosure(event: MotionEvent, selectedAlgorithm: AlgorithmType) {
        val clickedCell = (event.y / yCellSize).toInt() to (event.x / xCellSize).toInt()

        val selectedColor = _cellsMatrix[clickedCell.first][clickedCell.second]
        val remainingCells = mutableListOf(clickedCell.first to clickedCell.second)
        val markedCells = mutableListOf(clickedCell.first to clickedCell.second)

        while (remainingCells.isNotEmpty()) {
            val currentCell = when (selectedAlgorithm) {
                AlgorithmType.BFS -> remainingCells.removeAt(0)
                AlgorithmType.DFS -> remainingCells.removeAt(remainingCells.size - 1)
                AlgorithmType.HZ -> remainingCells.removeAt(0)
            }

            // top
            addToRemaining(remainingCells = remainingCells, markedCells = markedCells,
                    cellX = currentCell.first, cellY = currentCell.second + 1, selectedColor = selectedColor)
            // right
            addToRemaining(remainingCells = remainingCells, markedCells = markedCells,
                    cellX = currentCell.first + 1, cellY = currentCell.second, selectedColor = selectedColor)
            // bottom
            addToRemaining(remainingCells = remainingCells, markedCells = markedCells,
                    cellX = currentCell.first, cellY = currentCell.second - 1, selectedColor = selectedColor)
            // left
            addToRemaining(remainingCells = remainingCells, markedCells = markedCells,
                    cellX = currentCell.first - 1, cellY = currentCell.second, selectedColor = selectedColor)

        }
        redraw(markedCells, selectedColor)
    }

    private fun addToRemaining(remainingCells: MutableList<Pair<Int, Int>>,
                               markedCells: MutableList<Pair<Int, Int>>,
                               cellX: Int, cellY: Int, selectedColor: Int) {
        if (cellX >= 0 && cellX < _cellsMatrix.size
                && cellY >= 0 && cellY < _cellsMatrix[0].size
                && !markedCells.contains(cellX to cellY)) {
            if (_cellsMatrix[cellX][cellY] == selectedColor) {
                remainingCells.add(cellX to cellY)
                markedCells.add(cellX to cellY)
            }
        }
    }

    private fun redraw(markedCells: MutableList<Pair<Int, Int>>, selectedColor: Int) {
        val colorToRedraw = if (selectedColor == 1) 0 else 1
        if (isRedrawFinished) {
            isRedrawFinished = false

            markedCells.forEachIndexed { index, pair ->
                redrawingHandler.postDelayed({
                    if (index == markedCells.size - 1) {
                        isRedrawFinished = true
                    }

                    _cellsMatrix[pair.first][pair.second] = colorToRedraw
                    invalidate()
                }, index.toLong() * 10000 / speed)
            }
        }
    }
}