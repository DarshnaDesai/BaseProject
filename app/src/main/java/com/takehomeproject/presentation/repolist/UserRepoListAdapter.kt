package com.takehomeproject.presentation.repolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.takehomeproject.databinding.ItemUserRepoListBinding
import com.takehomeproject.data.model.UserRepoResponse

class UserRepoListAdapter() :
    RecyclerView.Adapter<UserRepoListAdapter.UserRepoListViewHolder>() {
    private var onClickListener: OnItemClickListener? = null
    private var repoList: ArrayList<UserRepoResponse> = ArrayList()

    class UserRepoListViewHolder(private val binding: ItemUserRepoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindCell(userRepo: UserRepoResponse) {
            binding.tvRepoName.text = userRepo.name
            binding.tvRepoDesc.text = userRepo.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRepoListViewHolder {
        val binding =
            ItemUserRepoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserRepoListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return repoList.size
    }

    override fun onBindViewHolder(holder: UserRepoListViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onClickListener?.onItemClick(repoList[position]) }
        holder.bindCell(repoList[position])
    }

    fun updateData(data: ArrayList<UserRepoResponse>) {
        repoList.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        repoList.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onClickListener: OnItemClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(userRepo: UserRepoResponse)
    }
}