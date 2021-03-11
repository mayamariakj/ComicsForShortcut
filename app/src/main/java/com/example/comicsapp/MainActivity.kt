package com.example.comicsapp
//https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/kt/AsynchronousGet.kt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.io.IOException
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch {
            var comic = getNewestComic()
            runOnUiThread {
                Picasso.get().load(comic!!.img).fit().centerInside().into(mainComicImage)
                var test = 2
            }
        }
       // var comic2 = getAComic(comic!!.num-1
      //  getComicFromApi()
    }

    fun getNewestComic(): ComicData?{
        var url: String = "https://xkcd.com/info.0.json"
        return getComicFromApi(url)
    }

    fun getAComic(number: Int): ComicData?{
        var url: String = "https://xkcd.com/" + number + "/info.0.json"
        return getComicFromApi(url)
    }

    private fun getComicFromApi(url: String): ComicData?{
        var data: String = ""
        val httpGet =  url.httpGet().responseString() { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    throw ex
                }
                is Result.Success -> {
                    data = result.get()
                }
            }
        }


        httpGet.join()

        return try {
            Gson().fromJson(data, ComicData::class.java)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}
