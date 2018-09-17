package ru.bulat.mukhutdinov.rockettestapp

import android.os.Bundle
import android.view.MotionEvent
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_size.*


class MainActivity : AppCompatActivity() {
    // TODO create 3rd algorithm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // TODO set size from editText
        var xSize = 5
        var ySize = 5

        size?.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
                    .setTitle(R.string.size)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        xSize = dialogSizeWidth?.editText?.text.toString().toInt()
                        ySize = dialogSizeHeight?.editText?.text.toString().toInt()
                    }
                    .setNegativeButton(android.R.string.cancel) { _, _ -> }
            val dialogView = layoutInflater.inflate(R.layout.dialog_size, null)
            dialogBuilder.setView(dialogView)
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }

        generate.setOnClickListener {
            if (xSize > 0 && ySize > 0) {
                generate(cellsAmountX = xSize, cellsAmountY = ySize)
            }
        }

        firstImage.setOnTouchListener { _, event ->
            onRocketViewClick(event)
        }

        secondImage.setOnTouchListener { _, event ->
            onRocketViewClick(event)
        }

        speed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                firstImage.speed = progress + 1
                secondImage.speed = progress + 1
            }
        })
    }

    private fun onRocketViewClick(event: MotionEvent) =
            if (event.action == MotionEvent.ACTION_DOWN) {
                firstImage.redrawClosure(event, fromIntValue(firstAlgorithm.selectedItemPosition))
                secondImage.redrawClosure(event, fromIntValue(secondAlgorithm.selectedItemPosition))
                true
            } else {
                false
            }

    fun generate(cellsAmountX: Int, cellsAmountY: Int) {
        val randomCellsMatrix = Array(cellsAmountX) { IntArray(cellsAmountY) { (0..1).random() } }
        (firstImage as RocketView).cellsMatrix = randomCellsMatrix
        (secondImage as RocketView).cellsMatrix = randomCellsMatrix
    }
}