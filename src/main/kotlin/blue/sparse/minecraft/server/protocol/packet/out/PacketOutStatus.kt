package blue.sparse.minecraft.server.protocol.packet.out

import blue.sparse.minecraft.server.extension.add
import blue.sparse.minecraft.server.protocol.extension.putVarIntString
import blue.sparse.minecraft.server.protocol.packet.Packet
import com.google.gson.JsonObject
import simplenet.Client
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.Base64
import javax.imageio.ImageIO

data class PacketOutStatus(
		var versionString: String,
		var protocol: Int,
		var maxPlayers: Int,
		var onlinePlayers: Int,
	//TODO: Implement sample players
		var description: String,
		var icon: BufferedImage?
): Packet.Out(0x00) {

	override fun serialize(client: Client, data: ByteBuffer) {
		val json = JsonObject()

		json.add("version") {
			addProperty("name", versionString)
			addProperty("protocol", protocol)
		}

		json.add("players") {
			addProperty("max", maxPlayers)
			addProperty("online", onlinePlayers)
		}

		json.add("description") {
			addProperty("text", description)
		}

		val icon = iconToBase64()
		if(icon != null) {
			json.addProperty("favicon", "data:image/png;base64,$icon")
		}

		data.putVarIntString(json.toString())
	}

	private fun iconToBase64(): String? {
		val icon = icon ?: return null
		val out = ByteArrayOutputStream()
		ImageIO.write(icon, "PNG", out)

		return Base64.getEncoder().encodeToString(out.toByteArray())
	}

}