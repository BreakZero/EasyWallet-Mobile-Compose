package com.easy.assets.data.serializers

import com.easy.assets.data.model.remote.*
import com.easy.assets.data.model.remote.CallParameter
import com.easy.assets.data.model.remote.IntListParameter
import com.easy.assets.data.model.remote.Parameter
import com.easy.assets.data.model.remote.StringParameter
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

internal object ParameterSerialize : JsonContentPolymorphicSerializer<Parameter>(Parameter::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Parameter> {
        println("element: $element, obj: ${element.jsonObject}")

        return when {
            "content" in element.jsonObject -> StringParameterSerializer
            "items" in element.jsonObject -> IntListParameterSerializer
            else -> CallParameter.serializer()
        }
    }
}

internal object StringParameterSerializer : KSerializer<StringParameter> {
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

internal object IntListParameterSerializer : KSerializer<IntListParameter> {
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

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = BaseRpcRequest::class)
internal object RpcRequestBodySerializer : KSerializer<BaseRpcRequest> {
    override fun deserialize(decoder: Decoder): BaseRpcRequest {
        return decoder.decodeStructure(descriptor) {
            var rpc: String? = null
            var method: String? = null
            var params: List<Parameter> = emptyList()
            var id: Int? = null
            loop@ while (true) {
                when (decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break@loop

                    0 -> rpc = decodeStringElement(descriptor, 0)
                    1 -> method = decodeStringElement(descriptor, 1)
                    2 -> params = decodeSerializableElement(descriptor, 2, ListSerializer(ParameterSerialize))
                    3 -> id = decodeIntElement(descriptor, 3)
                }
            }
            BaseRpcRequest(
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

    override fun serialize(encoder: Encoder, value: BaseRpcRequest) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.jsonrpc)
            encodeStringElement(descriptor, 1, value.method)
            encodeSerializableElement(descriptor, 2, ListSerializer(ParameterSerialize), value.params)
            encodeIntElement(descriptor, 3, value.id)
        }
    }
}
