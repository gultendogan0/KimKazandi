package com.example.kimkazandi.ui.free

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kimkazandi.adapter.MainAdapter
import com.example.kimkazandi.databinding.FragmentSlideshowBinding
import com.example.kimkazandi.service.JsoupTask
import kotlinx.coroutines.launch

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val url = "https://www.kimkazandi.com/cekilisler/bedava-katilim"
        val cekilis = JsoupTask()
        lifecycleScope.launch {
            binding.freeRv.layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(cekilis.getCekilis(url),this@SlideshowFragment)
            binding.freeRv.adapter = adapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}