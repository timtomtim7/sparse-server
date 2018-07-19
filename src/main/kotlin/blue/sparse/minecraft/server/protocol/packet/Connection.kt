package blue.sparse.minecraft.server.protocol.packet

import blue.sparse.minecraft.server.SparseServer
import blue.sparse.minecraft.server.protocol.extension.putVarInt
import blue.sparse.minecraft.server.protocol.packet.input.initial.PacketInHandshake
import blue.sparse.minecraft.server.protocol.packet.input.status.PacketInPing
import blue.sparse.minecraft.server.protocol.packet.input.status.PacketInStatusRequest
import blue.sparse.minecraft.server.protocol.packet.output.status.PacketOutPong
import blue.sparse.minecraft.server.protocol.packet.output.status.PacketOutStatus
import blue.sparse.utils.*
import simplenet.Client
import java.awt.*
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import kotlin.math.PI

class Connection(val client: Client) {

	var state = ConnectionState.INITIAL
		internal set

	val isConnected
		get() = SparseServer.network.isConnected(this)

	fun receive(packet: Packet.In) {
		if (packet is PacketInHandshake) {
			state = packet.nextState
		}
		if(packet is PacketInStatusRequest) {
			val icon = BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)
			val gfx = icon.createGraphics()
			gfx.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
			gfx.font = Font("Comic Sans MS", Font.BOLD, 16)
			gfx.rotate(PI / 4)
			gfx.color = Color(Color.HSBtoRGB(threadLocalRandom.nextFloat(), 1f, 1f))
			gfx.drawString("Sparse", 16, 0)
			gfx.drawString("Server", 16, 16)
			gfx.dispose()
			send(PacketOutStatus(
					"SparseServer",
					393,
					100,
					5,
					"${Math.random()}",
					icon
			))
		}
		if(packet is PacketInPing) {
			send(PacketOutPong(packet.value))
		}
	}

	fun send(packet: Packet.Out) {
		dataBuffer.clear()
		dataBuffer.putVarInt(packet.id)
		packet.serialize(client, dataBuffer)
		dataBuffer.flip()

		lengthBuffer.clear()
		lengthBuffer.putVarInt(dataBuffer.limit())
		lengthBuffer.flip()

		client.outgoingPackets.offer(lengthBuffer)
		client.flush()
		client.outgoingPackets.offer(dataBuffer)
		client.flush()
	}

	companion object {
		private val lengthBuffer by threadLocal { ByteBuffer.allocate(5) }
		private val dataBuffer by threadLocal { ByteBuffer.allocate(0xFFFF) }
	}

}