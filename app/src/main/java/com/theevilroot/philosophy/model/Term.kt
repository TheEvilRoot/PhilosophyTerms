package com.theevilroot.philosophy.model

import com.google.gson.JsonObject

data class Term(val term: String, val reference: String) {
    fun serialize(): JsonObject =
            JsonObject().apply {
                this.addProperty("term", term)
                this.addProperty("reference", reference)
            }
}