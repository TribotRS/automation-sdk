package org.tribot.automation.script.core.definition

interface Definitions {
    fun getItem(id: Int): ItemDefinition?
    fun getNpc(id: Int): NpcDefinition?
    fun getObject(id: Int): ObjectDefinition?
    fun getEnum(id: Int): EnumDefinition?
}
