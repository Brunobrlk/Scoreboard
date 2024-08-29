package com.example.scoreboardbrlk.ui.score
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.example.scoreboardbrlk.R
//import com.example.scoreboardbrlk.domain.Score
//
//class ScoreAdapter: ListAdapter<Score, ScoreAdapter.ScoreViewHolder>(ScoreDiffCallback()) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ScoreViewHolder.from(parent)
//
//    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.bind(item)
//    }
//
//    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        fun bind(item: Score){
//
//        }
//        companion object {
//            fun from(parent: ViewGroup): ScoreViewHolder {
//                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_score, parent, false)
//                return ScoreViewHolder(view)
//            }
//        }
//    }
//}
//
//class ScoreDiffCallback: DiffUtil.ItemCallback<Score>(){
//    override fun areItemsTheSame(oldItem: Score, newItem: Score) = oldItem == newItem
//    override fun areContentsTheSame(oldItem: Score, newItem: Score) = oldItem == newItem
//}