package org.tribot.automation.script.core.tabs

interface Equipment {
    fun getItems(): List<EquippedItem>
    fun getItemIn(slot: EquipmentSlot): EquippedItem?
    fun isEquipped(itemId: Int): Boolean
    fun clickSlot(slot: EquipmentSlot, option: String? = null): Boolean
}

data class EquippedItem(val id: Int, val quantity: Int, val slot: EquipmentSlot)

enum class EquipmentSlot(val containerIndex: Int) {
    HELMET(0),
    CAPE(1),
    AMULET(2),
    WEAPON(3),
    BODY(4),
    SHIELD(5),
    LEGS(7),
    GLOVES(9),
    BOOTS(10),
    RING(12),
    ARROW(13)
}
