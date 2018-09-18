package ru.bulat.mukhutdinov.rockettestapp

import android.content.Context
import android.widget.Toast
import java.util.*

fun ClosedRange<Int>.random() =
        Random().nextInt((endInclusive + 1) - start) + start

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}