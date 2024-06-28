package uz.demo.dana.di.authInterceptor

import com.example.testapp.common.MySettings
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor :Interceptor {
    private val mySetting = MySettings

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        mySetting.getToken()?.let {
            requestBuilder.addHeader("Authorization","Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}