package com.example.comicsapp
//https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/kt/AsynchronousGet.kt
//https://kotlinlang.org/docs/object-declarations.html#companion-objects
//https://www.c-sharpcorner.com/article/how-to-add-the-share-option-in-android-application/
import android.content.Intent
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

    // This is similar to static in java. had to do this so global launch would not create a copy.
    companion object ComicIndexes {
        var newestComicNumber: Int = 0
        var currentComicNumber: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupButtons()

        // waiting for the network call created weird errors(globalScope to the rescue!!)
        GlobalScope.launch {
            if (newestComicNumber == 0){
                displayComicAndTitle(getNewestComic()!!)
            }
            else{
                displayComicAndTitle(getAComic(currentComicNumber)!!)
            }
        }
    }

    // saving state so i can turn the screen and still see the same comic.
    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt("currentComicNumber", currentComicNumber)
        savedInstanceState.putInt("newestComicNumber", newestComicNumber)
    }

    // restoring state when returning to app
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

    private fun onClickNextComic(){
        if (newestComicNumber == currentComicNumber)
            return
        else{
            currentComicNumber ++
            displayComicAndTitle(getAComic(currentComicNumber)!!)
        }
    }

    private fun onClickPreviousComic(){
        if (currentComicNumber == 1 )
            return
        else{
            currentComicNumber --
            displayComicAndTitle(getAComic(currentComicNumber)!!)
        }
    }

    private fun onClickRandomComic(){
        currentComicNumber = Random.nextInt(newestComicNumber)
        displayComicAndTitle(getAComic(currentComicNumber)!!)
    }

    private fun onClickFastForward(){
        currentComicNumber = newestComicNumber
        displayComicAndTitle(getAComic(currentComicNumber)!!)
    }

    private fun onClickFirst(){
        currentComicNumber = 1
        displayComicAndTitle(getAComic(currentComicNumber)!!)
    }

    private fun onClickShareComic(){
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT,"https://xkcd.com/"+ currentComicNumber)
        sendIntent.type = "text/plain"
        Intent.createChooser(sendIntent, "Share via")
        startActivity(sendIntent)
    }

    private fun setupButtons() {
        button_forward.setOnClickListener(){onClickNextComic()}
        button_previous.setOnClickListener(){onClickPreviousComic()}
        button_random.setOnClickListener(){onClickRandomComic()}
        button_forward_top.setOnClickListener(){onClickNextComic()}
        button_previous_top.setOnClickListener(){onClickPreviousComic()}
        button_random_top.setOnClickListener(){onClickRandomComic()}
        button_share.setOnClickListener(){onClickShareComic()}
        button_fastForward_top.setOnClickListener(){onClickFastForward()}
        button_fastForward.setOnClickListener(){onClickFastForward()}
        button_first_top.setOnClickListener(){onClickFirst()}
        button_first.setOnClickListener(){onClickFirst()}
    }

    fun getNewestComic(): ComicData?{
        var url = "https://xkcd.com/info.0.json"
        var newComicData = getComicFromApi(url)
        newestComicNumber = newComicData!!.num
        currentComicNumber = newestComicNumber

        return newComicData
    }

    fun getAComic(number: Int): ComicData?{
        var url = "https://xkcd.com/$number/info.0.json"
        return getComicFromApi(url)
    }

    private fun getComicFromApi(url: String): ComicData?{
        var data = ""
        val httpGet =  url.httpGet().responseString() { _, _, result ->
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
