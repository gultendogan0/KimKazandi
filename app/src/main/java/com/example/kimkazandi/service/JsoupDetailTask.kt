package com.example.kimkazandi.service

import com.example.kimkazandi.data.model.CekilisDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class JsoupDetailTask {

    suspend fun getCekilis(url:String): MutableList<CekilisDetail> {
        val arr = mutableListOf<CekilisDetail>()

        val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate>? = null
        })

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCertificates, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
        val trustAllHostnames = HostnameVerifier { _, _ -> true }

        HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames)

        withContext(Dispatchers.IO) {
            try {
                val document = Jsoup.connect(url).get()
                val baslangicTarihi = document.select("div.kalanSure h4:contains(Başlangıç Tarihi)").text().split(":")[1].trim()
                val sonKatilimTarihi = document.select("div.kalanSure h4:contains(Son Katılım Tarihi)").text().split(":")[1].trim()
                val cekilisTarihi = document.select("div.kalanSure h4:contains(Çekiliş Tarihi)").text().split(":")[1].trim()
                val ilanTarihi = document.select("div.kalanSure h4:contains(İlan Tarihi)").text().split(":")[1].trim()
                val minHarcamaTutari = document.select("div.kalanSure h4:contains(Min. Harcama Tutarı)").text().split(":")[1].trim()
                val toplamHediyeDegeri = document.select("div.kalanSure h4:contains(Toplam Hediye Değeri)").text().split(":")[1].trim()
                val toplamHediyeSayisi = document.select("div.kalanSure h4:contains(Toplam Hediye Sayısı)").text().split(":")[1].trim()
                val imageUrl = "https://www.kimkazandi.com" + document.select("img[src^=/userfiles/resimler]").attr("src")
                val title = document.select("div.breadcrumb span[itemprop=name]").text()

                var cekilis = CekilisDetail(
                    baslangicTarihi,
                    sonKatilimTarihi,
                    cekilisTarihi,
                    ilanTarihi,
                    minHarcamaTutari,
                    toplamHediyeDegeri,
                    toplamHediyeSayisi,
                    imageUrl,
                    title
                )
                arr.add(cekilis)

            } catch (e: Exception) {
                println("Hata oluştu: ${e.message}")
            }
        }

        return arr
    }
}