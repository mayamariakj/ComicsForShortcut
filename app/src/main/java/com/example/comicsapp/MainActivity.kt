package com.example.comicsapp
//https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/kt/AsynchronousGet.kt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.io.IOException
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    var newestComicNumber: Int = 0
    var currentComicNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupButtons()

        GlobalScope.launch {
            displayComicAndTitle(getNewestComic()!!)

        }
        // var comic2 = getAComic(comic!!.num-1
        //  getComicFromApi()
    }

    private fun displayComicAndTitle(comicData: ComicData){
        runOnUiThread {
            Picasso.get().load(comicData.img).fit().centerInside().into(mainComicImage)
        }
    }

    private fun getNextComic(){
        if (newestComicNumber == currentComicNumber)
            return
        else{
            currentComicNumber ++
            displayComicAndTitle(getAComic(currentComicNumber)!!)
        }

    }

    private fun getPreviousComic(){
        if (currentComicNumber == 0 )
            return
        else{
            currentComicNumber --
            displayComicAndTitle(getAComic(currentComicNumber)!!)
        }

    }

    private fun getRandomComic(){
        currentComicNumber = Random.nextInt(newestComicNumber)
        displayComicAndTitle(getAComic(currentComicNumber)!!)
    }

    private fun setupButtons() {
        button_forward.setOnClickListener(){getNextComic()}
        button_previous.setOnClickListener(){getPreviousComic()}
        button_random.setOnClickListener(){getRandomComic()}
    }

    fun getNewestComic(): ComicData?{
        var url: String = "https://xkcd.com/info.0.json"
        var newComicData = getComicFromApi(url)
        newestComicNumber = newComicData!!.num
        currentComicNumber = newestComicNumber

        return newComicData
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
