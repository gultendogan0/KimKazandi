package com.example.kimkazandi.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.kimkazandi.data.AppDatabase
import com.example.kimkazandi.data.model.Cekilis
import com.example.kimkazandi.databinding.ItemBinding
import com.example.kimkazandi.ui.beginners.GalleryFragment
import com.example.kimkazandi.ui.beginners.GalleryFragmentDirections
import com.example.kimkazandi.ui.car.CarFragment
import com.example.kimkazandi.ui.car.CarFragmentDirections
import com.example.kimkazandi.ui.follow.FollowFragment
import com.example.kimkazandi.ui.follow.FollowFragmentDirections
import com.example.kimkazandi.ui.free.SlideshowFragment
import com.example.kimkazandi.ui.free.SlideshowFragmentDirections
import com.example.kimkazandi.ui.holiday.HolidayFragment
import com.example.kimkazandi.ui.holiday.HolidayFragmentDirections
import com.example.kimkazandi.ui.home.HomeFragment
import com.example.kimkazandi.ui.home.HomeFragmentDirections
import com.example.kimkazandi.ui.phonetablet.PhoneTabletFragment
import com.example.kimkazandi.ui.phonetablet.PhoneTabletFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainAdapter(val postList: MutableList<Cekilis>, private val fragment: Fragment): RecyclerView.Adapter<MainAdapter.PostHolder>() {

    inner class PostHolder(val binding: ItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Cekilis) {
            binding.card.setOnClickListener {
                val action = when (fragment) {
                    is HomeFragment -> HomeFragmentDirections.actionNavHomeToDetailFragment()
                    is GalleryFragment -> GalleryFragmentDirections.actionNavGalleryToDetailFragment()
                    is SlideshowFragment -> SlideshowFragmentDirections.actionNavSlideshowToDetailFragment()
                    is CarFragment -> CarFragmentDirections.actionCarFragmentToDetailFragment()
                    is PhoneTabletFragment -> PhoneTabletFragmentDirections.actionPhoneTabletFragmentToDetailFragment()
                    is HolidayFragment -> HolidayFragmentDirections.actionHolidayFragmentToDetailFragment()
                    is FollowFragment -> FollowFragmentDirections.actionFollowFragmentToDetailFragment()
                    else -> null
                }
                action?.let {
                    val args = Bundle()
                    args.putString("title", item.title)
                    args.putString("gift", item.gift)
                    args.putString("price", item.price)
                    args.putString("image", item.imageUrl)
                    args.putString("date", item.date)
                    binding.card.findNavController().navigate(it.actionId, args)
                }
            }

            if (fragment is FollowFragment) {
                binding.takipBirakButton.visibility = View.VISIBLE
            } else {
                binding.takipBirakButton.visibility = View.GONE
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context))
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        //https://www.kimkazandi.com/userfiles/resimler/johnsons_cekilis_2023_L.jpg
        holder.binding.date.text = postList.get(position).date
        holder.binding.title.text = postList.get(position).title
        holder.binding.gift.text = postList.get(position).gift
        holder.binding.price.text = postList.get(position).price
        Glide.with(holder.itemView)
            .load("https://www.kimkazandi.com"+postList.get(position).imageUrl)
            .into(holder.binding.image)
        holder.bind(postList[position])

        val db = Room.databaseBuilder(
            holder.itemView.context,
            AppDatabase::class.java,
            "app-database"
        ).build()

        val item = postList[position]

        holder.binding.takipBirakButton.setOnClickListener {
            GlobalScope.launch {
                db.cekilisDao().deleteItem(item)
                withContext(Dispatchers.Main) {
                    val itemPosition = postList.indexOf(item)
                    if (itemPosition != -1) {
                        postList.removeAt(itemPosition)
                        notifyItemRemoved(itemPosition)
                        notifyItemRangeChanged(itemPosition, postList.size)
                    }
                }
            }
        }
    }

    fun setData(newCekilisList: List<Cekilis>) {
        postList.clear()
        postList.addAll(newCekilisList)
        notifyDataSetChanged()
    }

}