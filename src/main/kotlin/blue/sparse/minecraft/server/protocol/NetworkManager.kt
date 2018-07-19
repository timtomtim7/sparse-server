package blue.sparse.minecraft.server.protocol

import blue.sparse.minecraft.server.protocol.extension.*
import blue.sparse.minecraft.server.protocol.packet.Packet
import blue.sparse.minecraft.server.protocol.packet.PacketRegistry
import blue.sparse.minecraft.server.protocol.packet.`in`.PacketInHandshake
import blue.sparse.minecraft.server.protocol.packet.out.PacketOutStatus
import blue.sparse.utils.*
import simplenet.Client
import simplenet.Server
import java.awt.*
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import kotlin.math.PI

object NetworkManager {

	private val connected = HashSet<Client>()

	val server = Server().apply {
		bind("localhost", 25565)
		onConnect(::onConnect)
		onDisconnect(::onDisconnect)
	}

	private fun onDisconnect(client: Client) {
		println("Client disconnected")
		connected.remove(client)
	}

	private fun onConnect(client: Client) {
		println("Client connected (${connected.size})")
		connected.add(client)

		client.launch {
			while(this in connected) {
				val length = readVarInt()
				val id = readVarInt()

				val data = read(length)

				val packet = PacketRegistry[id]
				if(packet == null) {
					println("Packet received with unknown id $id")
					continue
				}

				packet.receive(client, data)
				println(packet)
				if(packet is PacketInHandshake && packet.state == PacketInHandshake.State.STATUS) {

					for(i in 1..100) {
						val hue = (i / 20f) % 1f
						val icon = BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)
						val gfx = icon.createGraphics()
						gfx.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
						gfx.font = Font("Comic Sans MS", Font.BOLD, 16)
						gfx.rotate(PI / 4)
						gfx.color = Color(Color.HSBtoRGB(hue, 1f, 1f))
						gfx.drawString("Sparse", 16, 0)
						gfx.drawString("Server", 16, 16)
						gfx.dispose()

						send(client, PacketOutStatus(
								"1.13",
								47,
								100,
								i,
								"${Math.random()}",
								icon
						))

						Thread.sleep(50)
					}
				}
			}
		}
	}

	private val lengthBuffer by threadLocal { ByteBuffer.allocate(0xFF) }
	private val dataBuffer by threadLocal { ByteBuffer.allocate(0xFFFF) }

	fun send(client: Client, packet: Packet.Out) {
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
}

