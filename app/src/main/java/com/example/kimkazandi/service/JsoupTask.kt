package com.example.kimkazandi.service

import com.example.kimkazandi.data.model.Cekilis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class JsoupTask {

    suspend fun getCekilis(url:String): MutableList<Cekilis> {

        val arr = mutableListOf<Cekilis>()

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


        //val url = "https://www.kimkazandi.com/cekilisler"

        withContext(Dispatchers.IO) {
            try {
                val document = Jsoup.connect(url).get()
                val elements: Elements = document.select(".row > .col-sm-3")

                if (elements != null && elements.isNotEmpty()) {
                    for (element: Element in elements) {

                        val title: String = element.select("h4").text()
                        val date: String = element.select(".date").first()?.text() ?: ""
                        val gift: String = element.select(".date").eq(1).text()
                        val price: String = element.select(".kosul_fiyat").text()
                        val imageUrl: String = element.select("img").attr("src")

                        if (!date.isNullOrEmpty()){

                            val cekilis = Cekilis(0,title,date,gift,price,imageUrl)
                            arr.add(cekilis)

                        }
                    }
                } else {
                    println("VERI GELMIYOR")
                }
            } catch (e: Exception) {
                println("Hata oluştu: ${e.message}")
            }
        }

        return arr
    }
}