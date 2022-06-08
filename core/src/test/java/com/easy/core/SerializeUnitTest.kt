package com.easy.core

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.junit.Test

private inline fun <T> KSerializer<List<Any>>.cast(): KSerializer<T> { return this as KSerializer<T> }

class SerializeUnitTest {
    @Test
    fun test() {
        val json = Json {
            useArrayPolymorphism = true
            prettyPrint = true
            ignoreUnknownKeys = true
            allowStructuredMapKeys = true
            serializersModule = SerializersModule {
                polymorphic(Any::class) {
                    subclass(Caller::class, Caller.serializer())
                    subclass(String::class, String.serializer())
                }
            }
        }

        val reqbody = BaseRpc(
            id = 1,
            jsonrpc = "2.0",
            method = "eth_call",
            params = listOf(
                Caller(
                    from = "from address",
                    to = "to address",
                    data = "0x70a08231000000000000000000000000"
                ),
                "latest"
            )
        )

        println(json.encodeToString(BaseRpc.serializer(), reqbody))

        val reqbody1 = BaseRpc(
            id = 1,
            jsonrpc = "2.0",
            method = "eth_call",
            params = listOf(
                Caller(
                    from = "from address",
                    to = "to address",
                    data = "0x70a08231000000000000000000000000"
                ),
                listOf(25, 50, 75),
                "latest"
            )
        )

        println(json.encodeToString(BaseRpc.serializer(), reqbody1))
    }
}


object NewParameterSerialize : JsonContentPolymorphicSerializer<Any>(Any::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Any> {
        return String.serializer()
    }
}

object BaseRpcSerializer : KSerializer<BaseRpc> {
    override fun deserialize(decoder: Decoder): BaseRpc {
        return decoder.decodeStructure(descriptor) {
            var rpc: String? = null
            var method: String? = null
            var params: List<Any> = emptyList()
            var id: Int? = null
            loop@ while (true) {
                when (decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break@loop

                    0 -> rpc = decodeStringElement(descriptor, 0)
                    1 -> method = decodeStringElement(descriptor, 1)
                    2 -> params =
                        decodeSerializableElement(descriptor, 2, ListSerializer(NewParameterSerialize))
                    3 -> id = decodeIntElement(descriptor, 3)
                }
            }
            BaseRpc(
                jsonrpc = rpc.orEmpty(),
                method = method.orEmpty(),
                params = params,
                id = id ?: 1
            )
        }
    }

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("BaseRpc") {
            this.element<String>("jsonrpc")
            this.element<String>("method")
            this.element<List<Parameter>>("params")
            this.element<Int>("id")
        }

    override fun serialize(encoder: Encoder, value: BaseRpc) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.jsonrpc)
            encodeStringElement(descriptor, 1, value.method)
            encodeSerializableElement(descriptor, 2, ListSerializer(NewParameterSerialize), value.params)
            encodeIntElement(descriptor, 3, value.id)
        }
    }
}

@Serializable(with = BaseRpcSerializer::class)
data class BaseRpc(
    val jsonrpc: String,
    val method: String,
    val params: List<@Polymorphic Any>,
    val id: Int
)

@Serializable
data class Caller(
    val data: String,
    val from: String,
    val to: String
) : Parameter