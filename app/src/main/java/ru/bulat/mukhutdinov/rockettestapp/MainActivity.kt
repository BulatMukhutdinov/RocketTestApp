package ru.bulat.mukhutdinov.rockettestapp

import android.os.Bundle
import android.view.MotionEvent
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var xSize: Int = 0
    private var ySize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            xSize = savedInstanceState.getInt(STATE_X_SIZE)
            ySize = savedInstanceState.getInt(STATE_Y_SIZE)
        }

        setUpSizeView()

        setUpSpeedView()

        generate.setOnClickListener {
            if (xSize > 0 && ySize > 0) {
                generate(cellsAmountX = xSize, cellsAmountY = ySize)
            } else {
                toast(getString(R.string.generate_error))
            }
        }

        firstImage.setOnTouchListener { _, event ->
            onRocketViewClick(event)
        }

        secondImage.setOnTouchListener { _, event ->
            onRocketViewClick(event)
        }

    }

    private fun setUpSpeedView() {
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

    private fun setUpSizeView() {
        findViewById<TextInputLayout>(R.id.sizeHeight)?.editText?.setText(ySize.toString())
        findViewById<TextInputLayout>(R.id.sizeWidth)?.editText?.setText(xSize.toString())

        size?.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_size, null)

            val dialogSizeHeight = dialogView.findViewById<TextInputLayout>(R.id.dialogSizeHeight)?.editText
            val dialogSizeWidth = dialogView.findViewById<TextInputLayout>(R.id.dialogSizeWidth)?.editText
            dialogSizeHeight?.setText(ySize.toString())
            dialogSizeWidth?.setText(xSize.toString())

            val dialogBuilder = AlertDialog.Builder(this)
                    .setTitle(R.string.size)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        ySize = if (dialogSizeHeight?.text.isNullOrBlank()) {
                            0
                        } else {
                            dialogSizeHeight?.text.toString().toInt()
                        }

                        xSize = if (dialogSizeWidth?.text.isNullOrBlank()) {
                            0
                        } else {
                            dialogSizeWidth?.text.toString().toInt()
                        }
                    }
                    .setNegativeButton(android.R.string.cancel) { _, _ -> }
                    .setView(dialogView)
            val alertDialog = dialogBuilder.create()

            alertDialog.show()
        }
    }

    private fun onRocketViewClick(event: MotionEvent) =
            if (event.action == MotionEvent.ACTION_DOWN) {
                firstImage.redrawClosure(event, fromIntValue(firstAlgorithm.selectedItemPosition))
                secondImage.redrawClosure(event, fromIntValue(secondAlgorithm.selectedItemPosition))
                true
            } else {
                false
            }

    private fun generate(cellsAmountX: Int, cellsAmountY: Int) {
        val randomCellsMatrix = Array(cellsAmountY) { IntArray(cellsAmountX) { (0..1).random() } }
        (firstImage as RocketView).cellsMatrix = randomCellsMatrix
        (secondImage as RocketView).cellsMatrix = randomCellsMatrix
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val sizeHeight = findViewById<TextInputLayout>(R.id.sizeHeight)?.editText
        val sizeWidth = findViewById<TextInputLayout>(R.id.sizeWidth)?.editText
        if (sizeHeight != null && sizeWidth != null) {
            ySize = sizeHeight.text.toString().toIntOrNull() ?: 0
            xSize = sizeWidth.text.toString().toIntOrNull() ?: 0
        }

        outState.putInt(STATE_X_SIZE, xSize)
        outState.putInt(STATE_Y_SIZE, ySize)
    }

    companion object {
        private const val STATE_X_SIZE = "STATE_X_SIZE"
        private const val STATE_Y_SIZE = "STATE_Y_SIZE"
    }
}