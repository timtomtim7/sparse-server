package blue.sparse.minecraft.server.extension

import com.google.gson.JsonObject

inline fun JsonObject.add(name: String, body: JsonObject.() -> Unit) {
	add(name, JsonObject().apply(body))
}