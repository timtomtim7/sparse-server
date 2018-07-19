package blue.sparse.minecraft.server.protocol.packet.input.status

import blue.sparse.minecraft.server.protocol.packet.ConnectionState
import blue.sparse.minecraft.server.protocol.packet.Packet
import simplenet.Client
import java.nio.ByteBuffer

data class PacketInPing(var value: Long): Packet.In(0x01, ConnectionState.STATUS) {
	constructor(): this(0L)

	override fun receive(client: Client, data: ByteBuffer) {
		value = data.long
	}
}