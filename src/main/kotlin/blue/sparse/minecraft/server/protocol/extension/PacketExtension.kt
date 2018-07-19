package blue.sparse.minecraft.server.protocol.extension

import simplenet.packet.Packet

inline fun packet(body: Packet.() -> Unit): Packet {
	return Packet.builder().apply(body)
}

fun Packet.putVarInt(varInt: Int) {
	var value = varInt

	while (true) {
		if ((value and -128) == 0) {
			putByte(value)
			return
		}

		putByte(((value and VARINT_PART) or 0x80))
		value = value shr 7
	}
}

fun Packet.putVarIntString(string: String) {
	val bytes = string.toByteArray()
	putVarInt(bytes.size)
	//TODO: This copies the entire byte array.
	//Maybe yell at Jacob about this?
	putBytes(*bytes)
}