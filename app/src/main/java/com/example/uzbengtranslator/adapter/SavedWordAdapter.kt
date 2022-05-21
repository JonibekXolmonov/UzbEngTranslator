package com.example.uzbengtranslator.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uzbengtranslator.databinding.ItemSavedWordBinding
import com.example.uzbengtranslator.helper.ItemTouchHelperAdapter
import com.example.uzbengtranslator.model.SavedWord
import java.util.*

class SavedWordAdapter(private val onItemSwipedToRemove: ((Int) -> Unit)) :
    RecyclerView.Adapter<SavedWordAdapter.VH>(), ItemTouchHelperAdapter {

    private val savedWords = ArrayList<SavedWord>()

    inner class VH(val view: ItemSavedWordBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemSavedWordBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.view.apply {
            tvUzbek.text = savedWords[position].uzWord
            tvEnglish.text = savedWords[position].enWord
        }
    }

    override fun getItemCount(): Int = savedWords.size

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(savedWords, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(savedWords, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemSwipe(position: Int) {
        onItemSwipedToRemove.invoke(savedWords[position].id!!)
        savedWords.removeAt(position)
        notifyItemRemoved(position)
    }

    fun submitData(savedWords: ArrayList<SavedWord>) {
        this.savedWords.clear()
        this.savedWords.addAll(savedWords)
        notifyDataSetChanged()
    }
}