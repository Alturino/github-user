package com.onirutla.githubuser.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onirutla.githubuser.databinding.IntroItemBinding

class IntroAdapter(private val introItems: List<IntroModel>) :
    RecyclerView.Adapter<IntroAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = IntroItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = introItems[position]
    }

    override fun getItemCount(): Int = introItems.size

    inner class ViewHolder(val binding: IntroItemBinding) : RecyclerView.ViewHolder(binding.root)
}
