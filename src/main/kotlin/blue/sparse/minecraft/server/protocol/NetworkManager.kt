package blue.sparse.minecraft.server.protocol

import blue.sparse.minecraft.server.protocol.extension.*
import blue.sparse.minecraft.server.protocol.packet.*
import simplenet.Client
import simplenet.Server

object NetworkManager {

	private val connected = HashSet<Connection>()

	val server = Server().apply {
		bind("localhost", 25565)
		onConnect(::onConnect)
		onDisconnect(::onDisconnect)
	}

	fun getConnection(client: Client): Connection? {
		return connected.find { it.client == client }
	}

	fun isConnected(connection: Connection): Boolean {
		return connection in connected
	}

	private fun onDisconnect(client: Client) {
		if (!connected.removeAll { it.client == client })
			return
		println("Client disconnected")
	}

	private fun onConnect(client: Client) {
		println("Client connected")
		val connection = Connection(client)
		connected.add(connection)
		client.onDisconnect { onDisconnect(client) }

		client.launch {
			while (connection.isConnected) {
				val length = readVarInt()

				val data = read(length)
				val id = data.getVarInt()

				val packet = connection.state[id]
				if (packet == null) {
					println("Packet unknown packet: ${connection.state}[$id]")
					continue
				}

				packet.receive(client, data)
				println("Packet ${connection.state}[$id]: $packet")
				connection.receive(packet)
			}
		}
	}
}

