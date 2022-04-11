package com.easy.wallet

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.easy.core.common.AbiParameter
import com.easy.core.common.AbiParameterType
import com.easy.core.common.PayloadUtil
import com.easy.core.ext.toHex
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import wallet.core.jni.EthereumAbi

@RunWith(AndroidJUnit4::class)
class PayloadUtilTest {
    init {
        System.loadLibrary("TrustWalletCore")
    }

    @Test
    fun testFFunction() {
        val function = PayloadUtil.generate(
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
        )
        assertEquals("f(uint[],uint32[],bytes10,bytes)", function.type)
        assertEquals(
            "0x8be6524600000000000000000000000000000000000000000000000000000000000001230000000000000000000000000000000000000000000000000000000000000080313233343536373839300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e0000000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000004560000000000000000000000000000000000000000000000000000000000000789000000000000000000000000000000000000000000000000000000000000000d48656c6c6f2c20776f726c642100000000000000000000000000000000000000",
            EthereumAbi.encode(function).toHex(true)
        )
    }

    @Test
    fun testSamFunction() {
        val function = PayloadUtil.generate(
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
        )
        assertEquals("sam(bytes,bool,uint256[])", function.type)
//        assertEquals(
//            "0xa5643bf2" +
//                    "0000000000000000000000000000000000000000000000000000000000000060" +
//                    "0000000000000000000000000000000000000000000000000000000000000001" +
//                    "00000000000000000000000000000000000000000000000000000000000000a0" +
//                    "0000000000000000000000000000000000000000000000000000000000000004" +
//                    "6461766500000000000000000000000000000000000000000000000000000000" +
//                    "0000000000000000000000000000000000000000000000000000000000000003" +
//                    "0000000000000000000000000000000000000000000000000000000000000001" +
//                    "0000000000000000000000000000000000000000000000000000000000000002" +
//                    "0000000000000000000000000000000000000000000000000000000000000003",
//            EthereumAbi.encode(function).toHex(true)
//        )
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
}