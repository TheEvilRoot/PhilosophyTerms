package com.theevilroot.philosophy.model

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.net.URL

fun handleRaw(text: String): Observable<Term> = Observable.create<Term> {
    "\\n\\s+\\n".toRegex()
            .split(text)
            .asSequence()
            .filter(String::isNotBlank)
            .map(::parse)
            .forEach(it::onNext)
    it.onComplete()
}.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread())


@SuppressLint("CheckResult")
fun loadTerms(context: Context, url: URL, fileName: String): Observable<Term> = Observable.create<Term> {
    val file = File(context.filesDir, fileName)
    if (file.exists()) {
        val jsonArray = JsonParser().parse(file.readText()).asJsonArray
        jsonArray.map(JsonElement::getAsJsonObject).map(::parseJson).forEach(it::onNext)
        return@create it.onComplete()
    }
    try {
        val text = url.readText()
        val jsonArray = JsonArray()
        handleRaw(text).observeOn(Schedulers.newThread()).subscribe({ term ->
            it.onNext(term)
            jsonArray.add(term.serialize())
        }, Throwable::printStackTrace) {
            file.createNewFile()
            file.writeText(GsonBuilder().create().toJson(jsonArray))
            it.onComplete()
        }
    } catch (e: Exception) {
        it.onError(e)
    }
}.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread())

fun parse(string: String): Term {
    return try {
        val arr = string.trim().split("\\s-\\s".toRegex(), limit = 2)
        Term(arr[0].trim().replace("[^а-яА-Я-().,;\\s+]".toRegex(), ""), arr[1].trim())
    } catch (e: Exception) {
        if (e !is ArrayIndexOutOfBoundsException)
            throw e
        Term("", string)
    }
}

fun parseJson(jsonObject: JsonObject): Term =
        Term(jsonObject["term"].asString, jsonObject["reference"].asString)