package com.example.twelveortwenty

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twelveortwenty.databinding.GameFragmentBinding

class GameFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = requireContext()

        val binding = GameFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        val viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        binding.viewModel = viewModel

        viewModel.playSound.observe(viewLifecycleOwner){
            when(it){
                1 -> activity as MainActivity
            }
        }

        val tileList = viewModel.tiles
        val groupList = viewModel.groups

        val squareList = listOf(
            binding.square01, binding.square02, binding.square03, binding.square04,
            binding.square05, binding.square06, binding.square07, binding.square08,
            binding.square09, binding.square10, binding.square11, binding.square12,
            binding.square13, binding.square14, binding.square15, binding.square16,
        )


        for(i in 0..15){
            val tile = tileList[i]
            val square = squareList[i]
            tile.selected.observe(viewLifecycleOwner){
                if(it){
                    square.setBackgroundColor( ContextCompat.getColor(context,R.color.accent_color) )
                } else {
                    square.setBackgroundColor( ContextCompat.getColor(context,R.color.main_color) )
                }
            }
            tile.numberLD.observe(viewLifecycleOwner){
                //TODO("May set image instead")
                if(tile.isOccupied){
                    square.text = it.toString()
                } else {
                    square.text = ""
                }
            }
        }

        viewModel.selectedTileCountLD.observe(viewLifecycleOwner){
            if( it > 4 ){
                binding.comboLabel.visibility = View.VISIBLE
            } else {
                binding.comboLabel.visibility = View.INVISIBLE
            }
        }

        viewModel.showGameOver.observe(viewLifecycleOwner){
            if(it){
                binding.gameOverBar.visibility = View.VISIBLE
                if( viewModel.points > viewModel.highScore ){
                    binding.newBest.visibility = View.VISIBLE
                } else {
                    binding.newBest.visibility = View.INVISIBLE
                }
            } else {
                binding.gameOverBar.visibility = View.GONE
            }
        }

        val incomingList = listOf(
            binding.incoming1, binding.incoming2, binding.incoming3, binding.incoming4, binding.incoming5, binding.incoming6
        )

        viewModel.updateQueue.observe(viewLifecycleOwner){
            if(it){
                for(i in 0..5){
                    val square = incomingList[i]
                    val targetIndex = viewModel.queueIndex+i
                    //Set incomings to appropriate queue values or hide them
                    if(targetIndex < 6){ //test if index is inside bounds
                        square.visibility = View.VISIBLE
                        square.text = viewModel.queue[targetIndex].toString()
                    } else {
                        square.visibility = View.INVISIBLE
                    }
                }
                viewModel.updateQueue.value = false
            }
        }

        val sumList = listOf(
            binding.sumCol1, binding.sumCol2, binding.sumCol3, binding.sumCol4,
            binding.sumRow1, binding.sumRow2, binding.sumRow3, binding.sumRow4,
            binding.sumTopLeft, binding.sumTopRight, binding.sumBottomLeft, binding.sumBottomRight
        )

        for(i in 0..11){
            val group = groupList[i]
            val label = sumList[i]

            group.tileSumLD.observe(viewLifecycleOwner){
                label.text = it.toString()
                // Hint a finishing move
                if( group.tileCount==3 && (group.tileSum + viewModel.currentValue == 12 || group.tileSum + viewModel.currentValue == 20) ){
                    label.setTextColor(ContextCompat.getColor(context,R.color.accent_color))
                } else {
                    label.setTextColor(ContextCompat.getColor(context,R.color.main_color))
                }
            }
        }

        viewModel.updateQueue.value = true

        return binding.root
    }
}