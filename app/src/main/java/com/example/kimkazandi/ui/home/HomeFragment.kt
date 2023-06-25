package com.example.kimkazandi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kimkazandi.adapter.MainAdapter
import com.example.kimkazandi.service.JsoupTask
import com.example.kimkazandi.data.model.Cekilis
import com.example.kimkazandi.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val url = "https://www.kimkazandi.com/cekilisler"
        val cekilis = JsoupTask()
        lifecycleScope.launch {
            binding.tcRv.layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(cekilis.getCekilis(url),this@HomeFragment)
            binding.tcRv.adapter = adapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}