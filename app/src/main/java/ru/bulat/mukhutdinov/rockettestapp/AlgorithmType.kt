package ru.bulat.mukhutdinov.rockettestapp

import java.lang.RuntimeException

enum class AlgorithmType {
    BFS,
    DFS,
    RANDOM;
}

fun fromIntValue(value: Int) =
        when (value) {
            0 -> AlgorithmType.BFS
            1 -> AlgorithmType.DFS
            2 -> AlgorithmType.RANDOM
            else -> throw RuntimeException("Cannot create AlgorithmType from int value $value")
        }