package blue.sparse.minecraft.server.protocol.packet.output.status

import blue.sparse.minecraft.server.protocol.packet.ConnectionState
import blue.sparse.minecraft.server.protocol.packet.Packet
import simplenet.Client
import java.nio.ByteBuffer

data class PacketOutPong(var value: Long): Packet.Out(0x01, ConnectionState.STATUS) {

	override fun serialize(client: Client, data: ByteBuffer) {
		data.putLong(value)
	}

	override fun toString() = "PacketInPing()"
}