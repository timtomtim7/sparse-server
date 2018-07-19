package blue.sparse.minecraft.server.protocol.packet

import blue.sparse.minecraft.server.protocol.packet.`in`.PacketInHandshake

object PacketRegistry {

	private val registeredIn = Array<(() -> Packet.In)?>(0xFF) { null }

	init {
		registerIn(0x00, ::PacketInHandshake)
	}

	internal fun registerIn(id: Int, constructor: () -> Packet.In) {
		registeredIn[id] = constructor
	}

	operator fun get(id: Int): Packet.In? {
		return registeredIn[id]?.invoke()
	}

//	companion object {
//
//		val input = PacketRegistry<Packet.In>()
//		val output = PacketRegistry<Packet.Out>()
//
//		init {
//			input.register(0x00, ::PacketInHandshake)
//		}
//
//	}

}