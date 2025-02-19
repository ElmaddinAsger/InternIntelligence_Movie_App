package com.elmaddinasger.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elmaddinasger.movieapp.databinding.ItemVideoBinding
import com.elmaddinasger.movieapp.models.ResultX
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class VideoAdapter(val videList: List<ResultX>):RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return videList.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videList[position]
        holder.bind(video)
    }
    inner class VideoViewHolder(val binding: ItemVideoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (video: ResultX) {
            binding.videoTitle.text = video.name
            binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(video.key, 0f)
                }
            }
            )
        }
    }
}