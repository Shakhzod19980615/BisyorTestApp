package uz.demo.dana.di.authInterceptor

import android.util.Log
import com.example.testapp.common.MySettings
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.Request

/*
class AuthInterceptor :Interceptor {
    private val mySetting = MySettings

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token = mySetting.getToken()
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            Log.d("AuthInterceptor", "Authorization header added: Bearer $token")
        } else {
            //Log.d("AuthInterceptor", "No token available")
        }
        val request = requestBuilder.build()

        // Log the URL and headers
        Log.d("AuthInterceptor", "Request URL: ${request.url}")
        request.headers.names().forEach { headerName ->
            Log.d("AuthInterceptor", "$headerName: ${request.header(headerName)}")
        }

        return chain.proceed(request)
    }
}*/

import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import java.nio.charset.Charset

class AuthInterceptor : Interceptor {
    private val mySetting = MySettings

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // Add authorization header if the token is available
        val token = mySetting.getToken()
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            Log.d("AuthInterceptor", "Authorization header added: Bearer $token")
        }

        val request = requestBuilder.build()

        // Log headers
        Log.d("AuthInterceptor", "Request URL: ${request.url}")
        request.headers.names().forEach { headerName ->
            Log.d("AuthInterceptor", "$headerName: ${request.header(headerName)}")
        }

        // Log request body if present
        if (request.body != null) {
            val requestBody = bodyToString(request)
            Log.d("AuthInterceptor", "Request Body: $requestBody")
        }

        return chain.proceed(request)
    }

    // Helper function to convert request body to string
    private fun bodyToString(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body?.writeTo(buffer)
            buffer.readString(Charset.forName("UTF-8"))
        } catch (e: Exception) {
            "Error reading request body: ${e.message}"
        }
    }
}
