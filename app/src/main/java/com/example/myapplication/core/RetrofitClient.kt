package com.example.myapplication.core

import com.example.myapplication.model.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import com.example.myapplication.R
import okhttp3.OkHttpClient
import java.io.InputStream
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object RetrofitClient {
    private const val BASE_URL = "https://ec2-43-202-48-222.ap-northeast-2.compute.amazonaws.com:8000/"

    fun create(context: Context): AuthService {
        val okHttpClient = createOkHttpClient(context)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }
}

fun createOkHttpClient(context: Context): OkHttpClient {
    val certificateInputStream: InputStream = context.resources.openRawResource(R.raw.server)
    val certificateFactory = java.security.cert.CertificateFactory.getInstance("X.509")
    val certificate = certificateFactory.generateCertificate(certificateInputStream)
    certificateInputStream.close()

    val keyStore = java.security.KeyStore.getInstance("PKCS12")
    keyStore.load(null, null)
    keyStore.setCertificateEntry("server", certificate)

    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(keyStore)

    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustManagerFactory.trustManagers, java.security.SecureRandom())

    return OkHttpClient.Builder()
        .sslSocketFactory(sslContext.socketFactory, trustManagerFactory.trustManagers[0] as X509TrustManager)
        .build()
}
