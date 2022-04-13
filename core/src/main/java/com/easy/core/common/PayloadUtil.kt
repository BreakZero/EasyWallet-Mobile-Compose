package com.easy.core.common

import androidx.annotation.Keep
import com.easy.core.ext.toHexBytes
import wallet.core.jni.EthereumAbiFunction

@Keep
data class AbiParameter(
    val type: AbiParameterType,
    val index: Int,
    val value: Any,
    val size: Int = 0 // use for byte fix
)

enum class AbiParameterType {
    ADDRESS, UINT, STRING, BOOLEAN, UINT256, BYTEFIX, ARR_BYTE, ARR_UINT32, ARR_UINT256, ARR_ADDRESS
}

object PayloadUtil {
    fun generate(
        fnName: String,
        params: List<AbiParameter>
    ): EthereumAbiFunction {
        val function = EthereumAbiFunction(fnName)
        for (param in params) {
            when(param.type) {
                AbiParameterType.UINT, AbiParameterType.UINT256 -> {
                    function.addParamUInt256((param.value as String).toHexBytes(), false)
                }
                AbiParameterType.ADDRESS -> {
                    function.addParamAddress((param.value as String).toHexBytes(), false)
                }
                AbiParameterType.STRING -> {
                    function.addParamString(param.value as String, false)
                }
                AbiParameterType.BYTEFIX -> {
                    function.addParamBytesFix(
                        param.size,
                        (param.value as String).toByteArray(),
                        false
                    )
                }
                AbiParameterType.ARR_ADDRESS -> {
                    function.addParamArray(false)
                    (param.value as List<*>).forEach {
                        function.addInArrayParamAddress(param.index, (it as String).toHexBytes())
                    }
                }
                AbiParameterType.ARR_UINT256 -> {
                    function.addParamArray(false)
                    (param.value as List<*>).forEach {
                        function.addInArrayParamUInt256(param.index, (it as String).toHexBytes())
                    }
                }
                AbiParameterType.ARR_UINT32 -> {
                    function.addParamArray(false)
                    (param.value as List<*>).forEach {
                        function.addInArrayParamUInt32(param.index, (it as Int))
                    }
                }
                AbiParameterType.ARR_BYTE -> {
                    function.addParamBytes((param.value as String).toByteArray(), false)
                }
                AbiParameterType.BOOLEAN -> {
                    function.addParamBool(param.value as Boolean, false)
                }
            }
        }
        return function
    }
}