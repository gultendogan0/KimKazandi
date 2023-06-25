package com.example.kimkazandi.ui.car

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kimkazandi.R
import com.example.kimkazandi.adapter.MainAdapter
import com.example.kimkazandi.databinding.FragmentCarBinding
import com.example.kimkazandi.databinding.FragmentHomeBinding
import com.example.kimkazandi.service.JsoupTask
import kotlinx.coroutines.launch

class CarFragment : Fragment() {

    private var _binding: FragmentCarBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val url = "https://www.kimkazandi.com/cekilisler/araba-kazan"
        val cekilis = JsoupTask()
        lifecycleScope.launch {
            binding.carRv.layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(cekilis.getCekilis(url),this@CarFragment)
            binding.carRv.adapter = adapter
        }

        return root
    }

}