package com.example.comicsapp

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

class ComicsActivity {

    data class Feature(
        val name: String,
        val email: String
    ){
        class Deserializer: ResponseDeserializable<Array<Feature>> {
            override fun deserialize(content: String): Array<Feature>? = Gson().fromJson(content, Array<Feature>::class.java)
        }
    }

}