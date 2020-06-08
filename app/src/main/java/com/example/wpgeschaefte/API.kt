package com.example.wpgeschaefte

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class API : AppCompatActivity() {

    //APIKey = "brf4e9nrh5rah2kpe7k0"
    private var BASE_URL = "https://finnhub.io/api/v1/"

    fun getValues() {


        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PaperAPI::class.java)

        //request
        service.loadPapers().enqueue(object: Callback<Paper> {
            override fun onResponse(call: Call<Paper>, response: Response<Paper>) {
                if(response.isSuccessful){
                    Log.e("Tag", "alles gucci")
                    val data = response.body()

                    // println("test")
                }
            }
            override fun onFailure(call: Call<Paper>, t: Throwable) {
                Log.e("Tag", "error")
            }
        })
    }
}

class PaperResponse(@SerializedName("request") val papers: Paper)

//response data
class Paper(
    @SerializedName("c")
    val currentPrice: String,
    @SerializedName("h")
    val highestPrice: String,
    @SerializedName("l")
    val lowestPrice: Double,
    @SerializedName("o")
    val openPrice: Double,
    @SerializedName("pc")
    val previousClosePrice: Double,
    @SerializedName("t")
    val targetMedian: Double
)
//API
interface PaperAPI {
    @GET("quote?symbol=VOE.VI&token=brf4e9nrh5rah2kpe7k0")
    fun loadPapers(): Call<Paper>
}