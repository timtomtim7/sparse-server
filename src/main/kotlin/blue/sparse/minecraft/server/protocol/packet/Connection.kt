package blue.sparse.minecraft.server.protocol.packet

import simplenet.Client

class Connection(val client: Client) {

	var state: Int = 0
		internal set

}