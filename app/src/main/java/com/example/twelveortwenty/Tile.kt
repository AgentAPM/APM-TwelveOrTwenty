package com.example.twelveortwenty

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class Tile() {

    val numberLD = MutableLiveData(-1)
    var selected = MutableLiveData(false)
    val isOccupied:Boolean get() = number!=-1
    private var __number = -1
    var number:Int
        get() = __number
        set(v){
            // Only poke if value does not change
            val difference = if(v==number) 0 else 1

            if (v!=-1) {
                for (group in this.groups) {
                    group.tileCount += 1 * difference
                    group.tileSum += v * difference
                }
            } else {
                for (group in this.groups) {
                    group.tileCount -= 1 * difference
                    group.tileSum -= __number * difference
                }
            }
            __number = v
            numberLD.value = __number
        }


    val groups = mutableListOf<Group>()

    fun addToGroup(group:Group):Tile{
        group.elements.add(this)
        groups.add(group)
        return this
    }
    fun removeFromGroup(group:Group):Tile{
        group.elements.remove(this)
        groups.remove(group)
        return this
    }
}