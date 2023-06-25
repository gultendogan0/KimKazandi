package com.example.kimkazandi.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.kimkazandi.R
import com.example.kimkazandi.data.AppDatabase
import com.example.kimkazandi.data.model.Cekilis
import com.example.kimkazandi.databinding.FragmentDetailBinding
import com.example.kimkazandi.databinding.FragmentHomeBinding
import com.example.kimkazandi.service.JsoupDetailTask
import com.example.kimkazandi.service.JsoupTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Normalizer
import java.util.Locale


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var title: String
    private lateinit var gift: String
    private lateinit var price: String
    private lateinit var image: String
    private lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        title = arguments?.getString("title").toString()
        gift = arguments?.getString("gift").toString()
        price = arguments?.getString("price").toString()
        image = arguments?.getString("image").toString()
        date = arguments?.getString("date").toString()


        val convertedTitle = StringBuilder()
        for (char in title) {
            val convertedChar = when (char) {
                'ı' -> 'i'
                'İ' -> 'i'
                else -> char
            }
            convertedTitle.append(convertedChar)
        }
        val slug = convertToSlug(convertedTitle.toString())
        val url = "https://www.kimkazandi.com/kampanya/${slug}"

        val cekilis = JsoupDetailTask()

        lifecycleScope.launch {
            val cekilisList = cekilis.getCekilis(url)
            val cekilisDetail = cekilisList.firstOrNull()

            context?.let {
                Glide.with(it)
                    .load(cekilisDetail?.imageUrl)
                    .into(binding.detailImage)
            }
            binding.detailTitle.setText(cekilisDetail?.title)
            binding.baslangicTarihi.setText(cekilisDetail?.baslangicTarihi)
            binding.sonKatilimTarihi.setText(cekilisDetail?.sonKatilimTarihi)
            binding.cekilisTarihi.setText(cekilisDetail?.cekilisTarihi)
            binding.ilanTarihi.setText(cekilisDetail?.ilanTarihi)
            binding.minHarcama.setText(cekilisDetail?.minHarcamaTutari)
            binding.toplamHediye.setText(cekilisDetail?.toplamHediyeDegeri)
            binding.toplamHediyeSayisi.setText(cekilisDetail?.toplamHediyeSayisi)
        }

        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "app-database"
        ).build()

        lifecycleScope.launch {
            val existingCekilis = withContext(Dispatchers.IO) {
                db.cekilisDao().getCekilisByTitle(title)
            }

            if (existingCekilis != null) {
                binding.takipButton.visibility = View.GONE
            } else {
                binding.takipButton.setOnClickListener {
                    val cekilis = Cekilis(0,title = title,date,gift,price,image)
                    lifecycleScope.launch {
                        db.cekilisDao().insert(cekilis)
                        Toast.makeText(requireContext(), "Çekiliş Takibe Alındı", Toast.LENGTH_SHORT).show()
                        binding.takipButton.visibility = View.GONE
                    }
                }
            }
        }

        return root
    }

    fun convertToSlug(input: String): String {
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
        val slug = normalized
            .replace("[^\\p{ASCII}]".toRegex(), "")
            .replace("\\s+".toRegex(), "-")
            .replace("'", "-")
            .replace("ı", "i")
            .replace("İ", "i")
            .toLowerCase(Locale.getDefault())
        return slug
    }

}