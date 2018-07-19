package blue.sparse.minecraft.server.protocol.packet

import simplenet.Client
import java.nio.ByteBuffer

sealed class Packet(val id: Int) {
	abstract class In(id: Int) : Packet(id) {
		abstract fun receive(client: Client, data: ByteBuffer)
	}

	abstract class Out(id: Int) : Packet(id) {
		abstract fun serialize(client: Client, data: ByteBuffer)
	}
}