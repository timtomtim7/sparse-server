package blue.sparse.minecraft.server.protocol.packet.input.initial

import blue.sparse.minecraft.server.protocol.extension.getVarInt
import blue.sparse.minecraft.server.protocol.extension.getVarIntString
import blue.sparse.minecraft.server.protocol.packet.*
import simplenet.Client
import java.nio.ByteBuffer

data class PacketInHandshake(
		var protocol: Int,
		var address: String,
		var port: Short,
		var nextState: ConnectionState
): Packet.In(0x00, ConnectionState.INITIAL) {

	constructor(): this(-1, "", -1, ConnectionState.INITIAL)

	override fun receive(client: Client, data: ByteBuffer) {
		protocol = data.getVarInt()
		address = data.getVarIntString()
		port = data.short
		nextState = ConnectionState.of(data.getVarInt())!!
	}


}