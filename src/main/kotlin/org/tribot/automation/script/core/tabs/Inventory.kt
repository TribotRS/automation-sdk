package org.tribot.automation.script.core.tabs

interface Inventory {
    fun getItems(): List<InventoryItem>
    fun contains(itemId: Int): Boolean
    fun containsAll(vararg itemIds: Int): Boolean
    fun getCount(itemId: Int): Int
    fun isFull(): Boolean
    fun isEmpty(): Boolean
    fun emptySlots(): Int
    fun clickSlot(slotIndex: Int, option: String? = null): Boolean
    fun clickItem(itemId: Int, option: String? = null): Boolean
}

data class InventoryItem(val id: Int, val quantity: Int, val slotIndex: Int)
