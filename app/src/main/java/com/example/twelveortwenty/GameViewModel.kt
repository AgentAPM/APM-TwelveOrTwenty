package com.example.twelveortwenty

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.floor

class GameViewModel: ViewModel() {

    val modeLD = MutableLiveData(0)
    private var __mode:Int = 0
    var mode get()=__mode
        set(v){

            //showGameOver.value = v==100

            __mode = v
            modeLD.value = __mode
        }
    /** mode 0 - ?
        [mode1] - game
        [mode2] - cheat
        [mode100] - gameOver
    */

    val showGameOver = MutableLiveData(false)



    private var __points = 0
    var points:Int
        get() = __points
        private set(v){
            __points = v
            pointsLD.value = __points
    }
    var pointsLD = MutableLiveData(0)
    var incrementLD = MutableLiveData("")

    var __highScore = 0
    var highScore:Int get()=__highScore
        set(v){
             __highScore = v
             highScoreLD.value = __highScore
         }
    var highScoreLD = MutableLiveData(0)

    var queue = mutableListOf<Int>(0,0,0,0,0,0)
    private var __queueIndex=0
    val updateQueue = MutableLiveData(false)
    val currentValue:Int get() = if( queueIndex>=0 && queueIndex<queue.size )
            queue[queueIndex] else -1

    var queueIndex:Int
        get() = __queueIndex
        private set(v){
            __queueIndex = v
            updateQueue.value = true
        }



    fun rollNewQueue(){
        SoundPlayer.play(SoundPlayer.sfxRoll)

        for( i in 0..5 ){
            queue[i] = floor(Math.random()*6+1).toInt()
        }
        queueIndex = 0
    }

    var groups:List<Group> //All groups
    var tiles:List<Tile> //All tiles
    val selectedTiles = mutableListOf<Tile>() //Cache of tiles with selected=true

    val selectedTileCountLD = MutableLiveData(0)

    fun onClickTile(tileID:Int){
        when(mode) {
            1->{ // game state logic
                if( tileID<0 || tileID>=16) return
                val clickedTile = tiles[tileID]

                if (clickedTile.isOccupied) return

                SoundPlayer.play(SoundPlayer.sfxPlace)

                clickedTile.number = currentValue

                // Mark tiles to clear
                val matchTiles = mutableListOf<Tile>()
                for (group in clickedTile.groups) {
                    if (group.tileCount == 4 && (group.tileSum == 12 || group.tileSum == 20)) {
                        // group passed test
                        for (tile in group.elements) {
                            if (!matchTiles.contains(tile)) {
                                matchTiles.add(tile)
                                selectedTiles.add(tile)
                                tile.selected.value = true
                            }
                        }
                    }
                }
                // Clear tiles and claim points
                var gain = 0
                for (tile in matchTiles) {
                    gain += tile.number
                    tile.number = -1
                }
                points += gain
                if( gain>0 )
                    incrementLD.value = "+${gain}"
                else
                    incrementLD.value = ""


                if (matchTiles.size > 0) { // if there were matches, play sound
                    if(selectedTiles.size in 1..4)
                        SoundPlayer.play(SoundPlayer.sfxCombo4)
                    if(selectedTiles.size in 5..7)
                        SoundPlayer.play(SoundPlayer.sfxCombo5)
                    if(selectedTiles.size in 8..10)
                        SoundPlayer.play(SoundPlayer.sfxCombo8)
                    if(selectedTiles.size in 11..15)
                        SoundPlayer.play(SoundPlayer.sfxCombo12)
                    if(selectedTiles.size>=16)
                        SoundPlayer.play(SoundPlayer.sfxCombo16)
                } else {    // if there were no matches, clear highlights
                    for (tile in selectedTiles) {
                        tile.selected.value = false
                    }
                    selectedTiles.clear()
                }
                selectedTileCountLD.value = selectedTiles.size

                // Advance queue
                queueIndex += 1
                // Reroll when empty
                if (queueIndex >= queue.size) {
                    rollNewQueue()
                }

                // Check lose condition
                if( groups[0].tileCount+groups[1].tileCount+groups[2].tileCount+groups[3].tileCount == 16 ) {
                    gameOver()
                }

                //Poke all groups to refresh hints
                for (group in groups) {
                    group.tileSum += 0
                }
            } // game state logic
            2->{ // cheat state logic
                val clickedTile = tiles[tileID]

                if( clickedTile.number==-1 )
                    clickedTile.number = 1
                else if( clickedTile.number >=6 )
                    clickedTile.number = -1
                else
                    clickedTile.number += 1

            } // cheat state logic
        }
    } // onClickTile

    var playSound = MutableLiveData(-1)

    init {

        val col1 = Group()
        val col2 = Group()
        val col3 = Group()
        val col4 = Group()
        val row1 = Group()
        val row2 = Group()
        val row3 = Group()
        val row4 = Group()
        val box1 = Group()
        val box2 = Group()
        val box3 = Group()
        val box4 = Group()

        val tile11 = Tile().addToGroup(row1).addToGroup(col1).addToGroup(box1)
        val tile12 = Tile().addToGroup(row1).addToGroup(col2).addToGroup(box1)
        val tile13 = Tile().addToGroup(row1).addToGroup(col3).addToGroup(box2)
        val tile14 = Tile().addToGroup(row1).addToGroup(col4).addToGroup(box2)
        val tile21 = Tile().addToGroup(row2).addToGroup(col1).addToGroup(box1)
        val tile22 = Tile().addToGroup(row2).addToGroup(col2).addToGroup(box1)
        val tile23 = Tile().addToGroup(row2).addToGroup(col3).addToGroup(box2)
        val tile24 = Tile().addToGroup(row2).addToGroup(col4).addToGroup(box2)
        val tile31 = Tile().addToGroup(row3).addToGroup(col1).addToGroup(box3)
        val tile32 = Tile().addToGroup(row3).addToGroup(col2).addToGroup(box3)
        val tile33 = Tile().addToGroup(row3).addToGroup(col3).addToGroup(box4)
        val tile34 = Tile().addToGroup(row3).addToGroup(col4).addToGroup(box4)
        val tile41 = Tile().addToGroup(row4).addToGroup(col1).addToGroup(box3)
        val tile42 = Tile().addToGroup(row4).addToGroup(col2).addToGroup(box3)
        val tile43 = Tile().addToGroup(row4).addToGroup(col3).addToGroup(box4)
        val tile44 = Tile().addToGroup(row4).addToGroup(col4).addToGroup(box4)

        tiles = listOf(
            tile11, tile12, tile13, tile14,
            tile21, tile22, tile23, tile24,
            tile31, tile32, tile33, tile34,
            tile41, tile42, tile43, tile44
        )
        groups = listOf(
            col1,col2,col3,col4,
            row1,row2,row3,row4,
            box1,box2,box3,box4
        )


        // Init queue
        newGame()
    }

    fun newGame(){
        if( points > highScore ) {
            highScore = points
        }

        points = 0
        incrementLD.value = ""

        for(tile in tiles){
            tile.number = -1
            tile.selected.value = false
        }
        selectedTiles.clear()

        mode = 1

        rollNewQueue()
    }

    fun gameOver(){
        mode = 100

        showGameOver.value = true
    }

    fun onClickTryAgain(){
        showGameOver.value = false

        newGame()
    }
    fun onClickReturn(){
        //showGameOver.value = false

    }
}