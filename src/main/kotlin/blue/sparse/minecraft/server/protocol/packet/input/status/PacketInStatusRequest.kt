package blue.sparse.minecraft.server.protocol.packet.input.status

import blue.sparse.minecraft.server.protocol.packet.ConnectionState
import blue.sparse.minecraft.server.protocol.packet.Packet
import simplenet.Client
import java.nio.ByteBuffer

class PacketInStatusRequest: Packet.In(0x00, ConnectionState.STATUS) {
	override fun receive(client: Client, data: ByteBuffer) {}

	override fun toString() = "PacketInStatusRequest()"
}