package com.example.kimkazandi.ui.holiday

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kimkazandi.adapter.MainAdapter
import com.example.kimkazandi.databinding.FragmentHolidayBinding
import com.example.kimkazandi.service.JsoupTask
import kotlinx.coroutines.launch


class HolidayFragment : Fragment() {

    private var _binding: FragmentHolidayBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHolidayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val url = "https://www.kimkazandi.com/cekilisler/tatil-kazan"
        val cekilis = JsoupTask()
        lifecycleScope.launch {
            binding.holidayRv.layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(cekilis.getCekilis(url),this@HolidayFragment)
            binding.holidayRv.adapter = adapter
        }

        return root
    }


}