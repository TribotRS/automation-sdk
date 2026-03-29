package org.tribot.automation.script.core.definition

interface ItemDefinition {
    val id: Int
    val name: String
    val cost: Int
    val members: Boolean
    val tradable: Boolean
    val stackable: Boolean
    val weight: Int
    val inventoryActions: List<String?>
    val groundActions: List<String?>
    val params: Map<Int, Any>
    val category: Int
    fun isNoted(): Boolean
    fun getNotedId(): Int
    fun getUnnotedId(): Int
}
