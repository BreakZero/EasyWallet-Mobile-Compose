package com.easy.core

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
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
                    subclass(StringParameter::class, StringParameter.serializer())
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
                ), StringParameter("latest")
            )
        )

        println(json.encodeToString(TestBaseRpc.serializer(), request))
//        assertEquals("====", json.encodeToString(TestBaseRpc.serializer(), request))
    }
}

interface Parameter {

}

object ParameterSerialize: JsonContentPolymorphicSerializer<Parameter>(Parameter::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Parameter> {
        return when {
            "content" in element.jsonObject -> StringParameter.serializer()
            else -> TestCaller.serializer()
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
): Parameter

@Serializable
data class TestCaller(
    val data: String,
    val from: String,
    val to: String
): Parameter
