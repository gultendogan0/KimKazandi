package com.example.kimkazandi.ui.phonetablet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kimkazandi.adapter.MainAdapter
import com.example.kimkazandi.databinding.FragmentPhoneTabletBinding
import com.example.kimkazandi.service.JsoupTask
import kotlinx.coroutines.launch


class PhoneTabletFragment : Fragment() {

    private var _binding: FragmentPhoneTabletBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneTabletBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val url = "https://www.kimkazandi.com/cekilisler/telefon-tablet-kazan"
        val cekilis = JsoupTask()
        lifecycleScope.launch {
            binding.ptRv.layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(cekilis.getCekilis(url),this@PhoneTabletFragment)
            binding.ptRv.adapter = adapter
        }

        return root
    }


}