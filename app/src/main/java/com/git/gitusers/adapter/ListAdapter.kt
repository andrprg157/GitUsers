package com.git.gitusers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.git.gitusers.model.Users
import com.git.gitusers.util.loadImage
import kotlinx.android.synthetic.main.question_item.view.*


class ListAdapter(val context: Context, var mQuestions: List<Users>, private val mRowLayout: Int, val clickListener: (Users) -> Unit) : RecyclerView.Adapter<ListAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(mRowLayout, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(mQuestions[position],clickListener)
    }

    override fun getItemCount(): Int {
        return mQuestions.size
    }

    class QuestionViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
        fun bind(part:Users,clickListener: (Users) -> Unit)
        {
            itemView.title.text = "User Name : "+part.login
            itemView.link.text = "Type : "+ part.type
            itemView.mImgvs.loadImage(part.avatar_url)
            itemView.mImgvs.setOnClickListener{ clickListener(part)}
        }
    }
}
