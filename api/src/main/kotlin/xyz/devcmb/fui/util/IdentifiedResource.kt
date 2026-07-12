package xyz.devcmb.fui.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = IdentifiedResourceSerializer::class)
data class IdentifiedResource(
    val path: String
) {
    @Transient
    val namespace: String = path.substringBefore(':')

    @Transient
    val resourcePath: ResourcePath = ResourcePath(path.substringAfter(":"))

}

object IdentifiedResourceSerializer : KSerializer<IdentifiedResource> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("IdentifiedResource", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: IdentifiedResource
    ) {
        encoder.encodeString(value.path)
    }

    override fun deserialize(
        decoder: Decoder
    ): IdentifiedResource {
        return IdentifiedResource(decoder.decodeString())
    }
}