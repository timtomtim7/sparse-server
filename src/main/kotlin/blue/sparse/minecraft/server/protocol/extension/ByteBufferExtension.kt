package blue.sparse.minecraft.server.protocol.extension

import java.io.EOFException
import java.nio.ByteBuffer

fun ByteBuffer.putVarInt(varInt: Int) {
	var value = varInt

	while (true) {
		if ((value and -128) == 0) {
			put(value.toByte())
			return
		}

		put(((value and VARINT_PART) or 0x80).toByte())
		value = value ushr 7
	}
}

fun ByteBuffer.putVarIntString(string: String) {
	val bytes = string.toByteArray()
	putVarInt(bytes.size)
	put(bytes)
}

fun ByteBuffer.getVarIntString(): String {
	val length = getVarInt()
	val bytes = ByteArray(length)
	get(bytes)
	return String(bytes)
}

const val VARINT_PART = 0x7F

fun ByteBuffer.getVarInt(): Int {
	var size = 0
	var value = 0

	while (true) {
		val byte = get().toInt()
		if (byte == -1) throw EOFException()
		value = value or ((byte and VARINT_PART) shl (size++ * 7))

		if (size > 5)
			throw RuntimeException("VarInt size greater than 5.")

		if ((byte and 0x80) == 0)
			break
	}

	return value
}