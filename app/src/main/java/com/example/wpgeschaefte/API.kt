package com.example.wpgeschaefte

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

class API : AppCompatActivity() {
    //APIKey = "brf4e9nrh5rah2kpe7k0"
    private var BASE_URL = "https://finnhub.io/api/v1/"

    var liste = AktieSingleton.aktieListe

    fun getValues(symbol: String) {
        var url = "quote?symbol=${symbol}&token=brf4e9nrh5rah2kpe7k0"

        AktieSingleton.validSymbol = false
        if (symbol.length >= 3){
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(PaperAPI::class.java)

            //request
            service.loadPapers(url).enqueue(object: Callback<Paper> {
                override fun onResponse(call: Call<Paper>, response: Response<Paper>) {

                    if(response.isSuccessful){
                        Log.e("Tag", "alles gucci")
                        val data = response.body()
                        if (data?.currentPrice  != null) {
                            AktieSingleton.validSymbol = true
                            AktieSingleton.currentPrice = String.format("%.2f", data.currentPrice).toDouble()
                        }
                    }
                }
                override fun onFailure(call: Call<Paper>, t: Throwable) {
                    Log.e("Tag", "error")
                }
            })
        }

    }
    companion object {
        fun getValuesOnRefresh(adapter:MyRecyclerAdapter){
            val baseURL = "https://finnhub.io/api/v1/"
            for(a in AktieSingleton.aktieListe){
                var url = "quote?symbol=${a.kauf.symbol}&token=brf4e9nrh5rah2kpe7k0"
                //Refresh data if stock hasn't been sold yet
                if(!a.sold){
                    val retrofit = Retrofit.Builder().baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val service = retrofit.create(PaperAPI::class.java)

                    //request
                    service.loadPapers(url).enqueue(object: Callback<Paper> {
                        override fun onResponse(call: Call<Paper>, response: Response<Paper>) {
                            if(response.isSuccessful){
                                Log.e("Tag", "alles gucci")
                                val data = response.body()
                                //if current has changed -> refresh stock in list and notify the adapter
                                if( data != null && a.currentPrice != data.currentPrice.toDouble()){
                                    a.currentPrice = String.format("%.2f", data.currentPrice).toDouble()
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                        override fun onFailure(call: Call<Paper>, t: Throwable) {
                            Log.e("Tag", "error")
                        }
                    })
                }
            }
        }
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
    @GET()
    fun loadPapers(@Url url: String): Call<Paper>
}