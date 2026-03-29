package org.tribot.automation.script.core.definition

interface EnumDefinition {
    val id: Int
    val size: Int
    val keys: IntArray?
    val intVals: IntArray?
    val stringVals: Array<String?>?
    val defaultInt: Int
    val defaultString: String
    fun getIntValue(key: Int): Int
    fun getStringValue(key: Int): String?
}
