package com.easy.wallet

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.easy.core.common.AbiParameter
import com.easy.core.common.AbiParameterType
import com.easy.core.common.PayloadUtil
import com.easy.core.ext.clearHexPrefix
import com.easy.core.ext.toHex
import com.easy.core.ext.toHexBytes
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.*
import wallet.core.jni.EthereumAbi
import java.math.BigInteger


@RunWith(AndroidJUnit4::class)
class PayloadUtilTest {
    init {
        System.loadLibrary("TrustWalletCore")
    }

    @Test
    fun testFFunction() {
        /*val function = PayloadUtil.generate(
            fnName = "f", params = listOf(
                AbiParameter(
                    type = AbiParameterType.UINT,
                    index = 0,
                    value = "0x123"
                ),
                AbiParameter(
                    type = AbiParameterType.ARR_UINT32,
                    index = 1,
                    value = listOf(1110, 1929)
                ),
                AbiParameter(
                    type = AbiParameterType.BYTEFIX,
                    index = 2,
                    value = "1234567890",
                    size = 10
                ),
                AbiParameter(
                    type = AbiParameterType.ARR_BYTE,
                    index = 3,
                    value = "Hello, world!"
                )
            )
        )*/
        val function = Function(
            "f",
            listOf(
                Uint("291".toBigInteger()),
                DynamicStruct(Uint32("1110".toBigInteger()), Uint32("1929".toBigInteger())),
                Bytes10("1234567890".toByteArray()),
                DynamicBytes("Hello, world!".toByteArray())
            ),
            listOf(
                object : TypeReference<Uint256>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Uint256>() {}
            )
        )
        assertEquals(
            "",
            FunctionEncoder.encode(function)
        )
    }

    @Test
    fun testSamFunction() {
        /*val function = PayloadUtil.generate(
            fnName = "sam",
            params = listOf(
                AbiParameter(
                    type = AbiParameterType.ARR_BYTE,
                    index = 0,
                    value = "dave"
                ),
                AbiParameter(
                    type = AbiParameterType.BOOLEAN,
                    index = 1,
                    value = true
                ),
                AbiParameter(
                    type = AbiParameterType.ARR_UINT256,
                    index = 2,
                    value = listOf(1.toString(16),2.toString(16),3.toString(16))
                )
            )
        )*/
        val function = Function(
            "sam",
            listOf(
                DynamicBytes("dave".toByteArray()),
                Bool(true),
                DynamicStruct(Uint256(BigInteger("1")), Uint256(BigInteger("2")), Uint256(BigInteger("3")))
            ),
            emptyList()
        )

        assertEquals(
            "0xa5643bf2" +
                    "0000000000000000000000000000000000000000000000000000000000000060" +
                    "0000000000000000000000000000000000000000000000000000000000000001" +
                    "00000000000000000000000000000000000000000000000000000000000000a0" +
                    "0000000000000000000000000000000000000000000000000000000000000004" +
                    "6461766500000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000003" +
                    "0000000000000000000000000000000000000000000000000000000000000001" +
                    "0000000000000000000000000000000000000000000000000000000000000002" +
                    "0000000000000000000000000000000000000000000000000000000000000003",
            FunctionEncoder.encode(function)
        )
    }

    @Test
    fun testMint() {
        val function = PayloadUtil.generate(
            fnName = "mint",
            params = listOf(
                AbiParameter(
                    type = AbiParameterType.UINT256,
                    index = 0,
                    value = String.format("%#X", 5479200000)
                )
            )
        )

        assertEquals(
            "0xa0712d68000000000000000000000000000000000000000000000000000000014695f500",
            EthereumAbi.encode(function).toHex(true)
        )
    }

    @Test
    fun testBorrow() {
        val function = PayloadUtil.generate(
            fnName = "borrow",
            params = listOf(
                AbiParameter(
                    type = AbiParameterType.UINT256,
                    index = 0,
                    value = String.format("%#X", 100000000)
                )
            )
        )

        assertEquals(
            "0xc5ebeaec0000000000000000000000000000000000000000000000000000000005f5e100",
            EthereumAbi.encode(function).toHex(true)
        )
    }

    @Test
    fun testEnterMarkets() {
        val function = PayloadUtil.generate(
            fnName = "enterMarkets",
            params = listOf(
                AbiParameter(
                    type = AbiParameterType.ARR_ADDRESS,
                    index = 0,
                    value = listOf("0xe1c4c56f772686909c28c319079d41adfd6ec89b".clearHexPrefix())
                )
            )
        )
        println("======= ${function.type}")
        assertEquals(
            "0xc299823800000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000001000000000000000000000000e1c4c56f772686909c28c319079d41adfd6ec89b",
            EthereumAbi.encode(function).toHex(true)
        )
    }

    @Test
    fun testAccountLiquidity() {
        val function = PayloadUtil.generate(
            fnName = "getAccountLiquidity",
            params = listOf(
                AbiParameter(
                    type = AbiParameterType.ADDRESS,
                    index = 0,
                    value = "0x81080a7e991bcDdDBA8C2302A70f45d6Bd369Ab5".clearHexPrefix()
                )
            )
        )
        assertEquals(
            "0x5ec88c7900000000000000000000000081080a7e991bcdddba8c2302a70f45d6bd369ab5",
            EthereumAbi.encode(function).toHex(true)
        )
    }

    @Test
    fun testDecoder() {
        val function = Function(
            "getAccountLiquidity",
            listOf(Address("0x81080a7e991bcDdDBA8C2302A70f45d6Bd369Ab5")),
            listOf(
                object : TypeReference<Uint256>() {},
                object : TypeReference<Uint256>() {},
                object : TypeReference<Uint256>() {}
            )
        )
        assertEquals(
            "0x5ec88c7900000000000000000000000081080a7e991bcdddba8c2302a70f45d6bd369ab5",
            FunctionEncoder.encode(function)
        )
        /*val result = FunctionReturnDecoder.decode(
            "0x0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000307acbe5abed448f0000000000000000000000000000000000000000000000000000000000000000",
            function.outputParameters
        )
        assertEquals("", result[1].value)*/
    }
}