package com.easy.core

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
private inline fun <T> KSerializer<List<Any>>.cast(): KSerializer<T> {
    return this as KSerializer<T>
}

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test() {
        val json = Json {
            useArrayPolymorphism = true
            prettyPrint = true
            allowStructuredMapKeys = true
            serializersModule = SerializersModule {
                polymorphic(List::class) {
                    ListSerializer(ParameterSerialize)
                }
                polymorphic(Parameter::class) {
                    subclass(TestCaller::class, TestCaller.serializer())
                    subclass(StringParameter::class, StringParameterSerializer)
                    subclass(IntListParameter::class, ListParameterSerializer)
                }
            }
        }

        val request = TestBaseRpc(
            id = 1,
            jsonrpc = "2.0",
            method = "eth_call",
            params = listOf(
                TestCaller(
                    from = "from address",
                    to = "to address",
                    data = "0x70a08231000000000000000000000000"
                ),
                StringParameter("latest")
            )
        )

        val jsonContent =
            "{\"jsonrpc\":\"2.0\",\"method\":\"eth_call\",\"params\":[{\"data\":\"0x70a08231000000000000000000000000\",\"from\":\"from address\",\"to\":\"to address\"},{\"content\":\"latest\"}],\"id\":1}"
        val jsonS = json.encodeToString(RpcSerializer, request)
        println(jsonS)
        println(json.decodeFromString(RpcSerializer, jsonContent))

        val listReq = TestBaseRpc(
            id = 1,
            jsonrpc = "2.0",
            method = "eth_call",
            params = listOf(
                IntListParameter(
                    listOf(25, 50, 75)
                ),
                StringParameter("latest")
            )
        )
        println(json.encodeToString(RpcSerializer, listReq))
//        assertEquals("====", json.encodeToString(TestBaseRpc.serializer(), request))
    }
}

interface Parameter

object ParameterSerialize : JsonContentPolymorphicSerializer<Parameter>(Parameter::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Parameter> {
        println("element: $element, obj: ${element.jsonObject}")

        return when {
            "content" in element.jsonObject -> StringParameterSerializer
            "items" in element.jsonObject -> ListParameterSerializer
            else -> TestCaller.serializer()
        }
    }
}

object StringParameterSerializer : KSerializer<StringParameter> {
    override fun deserialize(decoder: Decoder): StringParameter {
        return decoder.decodeStructure(descriptor) {
            val content = decodeStringElement(descriptor, 0)
            StringParameter(content)
        }
    }

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("StringParameter") {
            this.element<String>("content")
        }

    override fun serialize(encoder: Encoder, value: StringParameter) {
        encoder.encodeString(value.content)
        /*encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.content)
        }*/
    }
}

object ListParameterSerializer : KSerializer<IntListParameter> {
    override fun deserialize(decoder: Decoder): IntListParameter {
        return decoder.decodeStructure(descriptor) {
            val items = decodeSerializableElement(descriptor, 0, ListSerializer(Int.serializer()))
            IntListParameter(
                items = items
            )
        }
    }

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("ListParameter") {
            this.element<List<Int>>("items")
        }

    override fun serialize(encoder: Encoder, value: IntListParameter) {
        encoder.encodeSerializableValue(ListSerializer(Int.serializer()), value.items)
    }
}

object RpcSerializer : KSerializer<TestBaseRpc> {
    override fun deserialize(decoder: Decoder): TestBaseRpc {
        return decoder.decodeStructure(descriptor) {
            var rpc: String? = null
            var method: String? = null
            var params: List<Parameter> = emptyList()
            var id: Int? = null
            loop@ while (true) {
                when (decodeElementIndex(descriptor)) {
                    DECODE_DONE -> break@loop

                    0 -> rpc = decodeStringElement(descriptor, 0)
                    1 -> method = decodeStringElement(descriptor, 1)
                    2 -> params = decodeSerializableElement(descriptor, 2, ListSerializer(ParameterSerialize))
                    3 -> id = decodeIntElement(descriptor, 3)
                }
            }
            TestBaseRpc(
                jsonrpc = rpc.orEmpty(),
                method = method.orEmpty(),
                params = params,
                id = id ?: 1
            )
        }
    }

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("TestBaseRpc") {
            this.element<String>("jsonrpc")
            this.element<String>("method")
            this.element<List<Parameter>>("params")
            this.element<Int>("id")
        }

    override fun serialize(encoder: Encoder, value: TestBaseRpc) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.jsonrpc)
            encodeStringElement(descriptor, 1, value.method)
            encodeSerializableElement(descriptor, 2, ListSerializer(ParameterSerialize), value.params)
            encodeIntElement(descriptor, 3, value.id)
        }
    }
}

@Serializable
data class TestBaseRpc(
    val jsonrpc: String,
    val method: String,
    val params: List<Parameter>,
    val id: Int
)

@Serializable
data class StringParameter(
    val content: String
) : Parameter

@Serializable
data class TestCaller(
    val data: String,
    val from: String,
    val to: String
) : Parameter

@Serializable
data class IntListParameter(
    val items: List<Int>
) : Parameter
