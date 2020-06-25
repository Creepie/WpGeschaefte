package com.example.wpgeschaefte

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.recreate
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.math.RoundingMode

class API{
    /**
     * Base URL of Share-API which all requests are sent to
     */
    private val BASE_URL = "https://finnhub.io/api/v1/"

    /**
     * Gets all data of a share depending of its symbol.
     * Invokes a toast if the Symbol is incorrect or the HTTP-connection failes
     */
    fun getValues(symbol: String, listener: SymbolAvailable) {
        var url = "quote?symbol=${symbol}&token=brf4e9nrh5rah2kpe7k0"

        ShareSingleton.validSymbol = false
        if (symbol.length >= 3){
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(PaperAPI::class.java)

            service.loadPapers(url).enqueue(object: Callback<Paper> {
                override fun onResponse(call: Call<Paper>, response: Response<Paper>) {
                    if(response.isSuccessful){
                        val data = response.body()
                        if (data?.currentPrice  != null) {
                            ShareSingleton.validSymbol = true
                            listener.available()
                            ShareSingleton.currentPrice = data.currentPrice.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                        } else {
                            listener.notAvailable()
                        }

                    }
                }
                override fun onFailure(call: Call<Paper>, t: Throwable) {
                    listener.notAvailable()
                }
            })
        }

    }

    companion object {
        /**
         * static method to refresh all shares via HTTP. Invokes a toast if HTTP-Connection failes
         */
        fun getValuesOnRefresh(adapter:MyRecyclerAdapter, context:Context, listener: SymbolAvailable){
            val baseURL = "https://finnhub.io/api/v1/"
            for(aktie in ShareSingleton.shareList){
                var url = "quote?symbol=${aktie.buyData.symbol}&token=brf4e9nrh5rah2kpe7k0"
                //Refresh data if stock hasn't been sold yet
                if(!aktie.sold){
                    val retrofit = Retrofit.Builder().baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val service = retrofit.create(PaperAPI::class.java)
                    service.loadPapers(url).enqueue(object: Callback<Paper> {
                        override fun onResponse(call: Call<Paper>, response: Response<Paper>) {
                            if(response.isSuccessful){
                                val data = response.body()
                                //if current has changed -> refresh share in list and notify the adapter & and create a new JSON File
                                if( data != null && aktie.currentPrice != data.currentPrice.toDouble()){
                                    aktie.currentPrice = data.currentPrice.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                                    aktie.buyData.value = aktie.currentPrice * aktie.buyData.amount
                                    adapter.notifyDataSetChanged()
                                    listener.available()
                                    HomeScreen.createJSONFromStocks("myStocks.json", context)
                                }
                            }
                        }
                        override fun onFailure(call: Call<Paper>, t: Throwable) {
                            listener.notAvailable()
                        }
                    })
                }
            }

        }
    }

}

/**
 * Represents the response class for the HTTP-Request via Retrofit2-library.
 * Changes the parameter name of the HTTP-Response to custom names
 */
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

/**
 * Functional Interface which provides method-construct to the Retrofit builder in order to create a service for the actual request
 */
interface PaperAPI {
    @GET()
    fun loadPapers(@Url url: String): Call<Paper>
}