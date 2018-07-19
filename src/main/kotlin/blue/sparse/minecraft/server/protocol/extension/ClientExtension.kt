package blue.sparse.minecraft.server.protocol.extension

import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.launch
import simplenet.Client
import java.io.EOFException
import java.nio.ByteBuffer
import kotlin.coroutines.experimental.suspendCoroutine

suspend fun Client.read(length: Int) = suspendCoroutine<ByteBuffer> { cont ->
	read(length) { cont.resume(it) }
}

suspend fun Client.readByte() = suspendCoroutine<Byte> { cont ->
	readByte { cont.resume(it) }
}

suspend fun Client.readChar() = suspendCoroutine<Char> { cont ->
	readChar { cont.resume(it) }
}

suspend fun Client.readDouble() = suspendCoroutine<Double> { cont ->
	readDouble { cont.resume(it) }
}

suspend fun Client.readFloat() = suspendCoroutine<Float> { cont ->
	readFloat { cont.resume(it) }
}

suspend fun Client.readInt() = suspendCoroutine<Int> { cont ->
	readInt { cont.resume(it) }
}

suspend fun Client.readLong() = suspendCoroutine<Long> { cont ->
	readLong { cont.resume(it) }
}

suspend fun Client.readShort() = suspendCoroutine<Short> { cont ->
	readShort { cont.resume(it) }
}

suspend fun Client.readString() = suspendCoroutine<String> { cont ->
	readString { cont.resume(it) }
}

suspend fun Client.readVarIntString(): String {
	val length = readVarInt()
	val buffer = read(length)
	val bytes = if (buffer.hasArray()) buffer.array() else run {
		val array = ByteArray(length)
		buffer.get(array)
		array
	}

	return String(bytes)
}

//const val VARINT_PART = 0x7F

suspend fun Client.readVarInt(): Int {
	var size = 0
	var value = 0

	while (true) {
		val byte = readByte().toInt()
		if (byte == -1) throw EOFException()
		value = value or ((byte and VARINT_PART) shl (size++ * 7))

		if (size > 5)
			throw RuntimeException("VarInt size greater than 5.")

		if ((byte and 0x80) == 0)
			break
	}

	return value
}

fun Client.launch(body: suspend Client.() -> Unit) {
	launch(Unconfined) { body() }
}
