package com.mark.alphavantage.navigation

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mark.alphavantage.utils.RuntimeTypeAdapterFactory

abstract class Arguments {
    // used for Gson serialization
    private var type: String? = null

    fun serialize(): String {
        type = javaClass.simpleName
        val gson = GsonBuilder().registerTypeAdapterFactory(factory).create()
        return gson.toJson(this)
    }

    companion object {
        private val factory = RuntimeTypeAdapterFactory.of(Arguments::class.java)

        @JvmStatic
        fun <T : Arguments> registerSubclass(clazz: Class<T>) {
            factory.registerSubtype(clazz)
        }

        @JvmStatic
        fun <T : Arguments> deserialize(json: String): T {
            val gson = GsonBuilder().registerTypeAdapterFactory(factory).create()
            return gson.fromJson<T>(json, TypeToken.get(Arguments::class.java).type)
        }
    }
}
