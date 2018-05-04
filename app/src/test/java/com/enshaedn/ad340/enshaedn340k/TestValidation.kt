package com.enshaedn.ad340.enshaedn340k

import org.junit.Assert.assertEquals
import org.junit.Test

class TestValidation {
    @Test
    fun validInput() {
        val mActivity = MainActivity()
        assertEquals(mActivity.phraseCheck("Test Phrase"), true)
        assertEquals(mActivity.phraseCheck(""), false)
    }
}