package com.example.comicsapp
//https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/kt/AsynchronousGet.kt
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    var newestComicNumber: Int = 0
    var currentComicNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupButtons()

        GlobalScope.launch {
            if (newestComicNumber == 0){
                displayComicAndTitle(getNewestComic()!!)
            }
            else{
                displayComicAndTitle(getAComic(currentComicNumber)!!)
            }
        }
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt("currentComicNumber", currentComicNumber)
        savedInstanceState.putInt("newestComicNumber", newestComicNumber)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentComicNumber = savedInstanceState.getInt("currentComicNumber")
        newestComicNumber = savedInstanceState.getInt("newestComicNumber")

    }

    private fun displayComicAndTitle(comicData: ComicData){
        runOnUiThread {
            Picasso.get().load(comicData.img).resize(mainComicImage.width, 9999).centerInside().into(
                mainComicImage
            )
            text_title.text = comicData.title
            text_info.text = comicData.alt

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
        button_forward_top.setOnClickListener(){getNextComic()}
        button_previous_top.setOnClickListener(){getPreviousComic()}
        button_random_top.setOnClickListener(){getRandomComic()}
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
