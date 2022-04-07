package com.easy.assets.data

import com.easy.core.ext._16toNumber
import com.easy.core.ext.clearHexPrefix
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val num = "0x00000000000000000000000000000000000000000000000031a771b165d5d6c7"
        println(num._16toNumber())
        assertEquals(4, 2 + 2)
    }
}