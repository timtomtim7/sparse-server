package blue.sparse.minecraft.server.protocol.packet.`in`

import blue.sparse.minecraft.server.protocol.extension.getVarInt
import blue.sparse.minecraft.server.protocol.extension.getVarIntString
import blue.sparse.minecraft.server.protocol.packet.Packet
import simplenet.Client
import java.nio.ByteBuffer

data class PacketInHandshake(
		var version: Int,
		var address: String,
		var port: Short,
		var state: State
): Packet.In(0x00) {

	constructor(): this(-1, "", -1, State.UNKNOWN)

	override fun receive(client: Client, data: ByteBuffer) {
		version = data.getVarInt()
		address = data.getVarIntString()
		port = data.short
		state = State.of(data.getVarInt())!!
	}

	enum class State(val id: Int) {
		UNKNOWN(0),
		STATUS(1),
		LOGIN(2);

		companion object {
			fun of(id: Int) = values().find { it.id == id }
		}
	}
}