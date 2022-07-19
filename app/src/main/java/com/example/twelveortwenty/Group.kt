package com.example.twelveortwenty

import androidx.lifecycle.MutableLiveData

class Group {
    var tileCount = 0

    private var __tileSum = 0
    var tileSum:Int
        get() = __tileSum
        set(v) {
            __tileSum = v
            tileSumLD.value = __tileSum
        }
    var tileSumLD = MutableLiveData(0)

    val elements = mutableListOf<Tile>()

}