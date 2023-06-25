package com.example.kimkazandi.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.kimkazandi.adapter.MainAdapter
import com.example.kimkazandi.data.AppDatabase
import com.example.kimkazandi.databinding.FragmentFollowBinding
import kotlinx.coroutines.launch

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "app-database"
        ).build()


        adapter = MainAdapter(mutableListOf(), this@FollowFragment)
        binding.followRv.layoutManager = LinearLayoutManager(context)
        binding.followRv.adapter = adapter

        val cekilisData = db.cekilisDao().getAllData()
        cekilisData.observe(viewLifecycleOwner, Observer { cekilisList ->
            adapter.setData(cekilisList)
            adapter.notifyDataSetChanged()
            binding.followRv.adapter = adapter // Set the adapter again
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}