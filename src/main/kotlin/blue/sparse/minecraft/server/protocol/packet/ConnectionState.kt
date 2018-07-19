package blue.sparse.minecraft.server.protocol.packet

import blue.sparse.minecraft.server.protocol.packet.input.initial.PacketInHandshake
import blue.sparse.minecraft.server.protocol.packet.input.status.PacketInPing
import blue.sparse.minecraft.server.protocol.packet.input.status.PacketInStatusRequest

enum class ConnectionState(val id: Int) {
	INITIAL(0) {
		init {
			registerIn(0x00, ::PacketInHandshake)
		}
	},
	STATUS(1) {
		init {
			registerIn(0x00, ::PacketInStatusRequest)
			registerIn(0x01, ::PacketInPing)
		}
	},
	LOGIN(2) {
		init {

		}
	},
	PLAY(3) {
		init {

		}
	}
	;

	private val registeredIn = Array<(() -> Packet.In)?>(0xFF) { null }

	internal fun registerIn(id: Int, constructor: () -> Packet.In) {
		registeredIn[id] = constructor
	}

	operator fun get(id: Int): Packet.In? {
		return registeredIn[id]?.invoke()
	}

	companion object {
		fun of(id: Int) = values().find { it.id == id }
	}
}