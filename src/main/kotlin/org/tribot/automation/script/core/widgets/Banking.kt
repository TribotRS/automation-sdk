package org.tribot.automation.script.core.widgets

interface Banking {
    fun isOpen(): Boolean
    fun close(): Boolean
    fun getItems(): List<BankItem>
    fun contains(itemId: Int): Boolean
    fun getCount(itemId: Int): Int
    fun withdraw(itemId: Int, amount: Int): Boolean
    fun withdrawAll(itemId: Int): Boolean
    fun deposit(itemId: Int, amount: Int): Boolean
    fun depositAll(itemId: Int): Boolean
    fun depositAllInventory(): Boolean
    fun depositAllEquipment(): Boolean
    fun enterPin(pin: String): Boolean
    fun isPinScreenOpen(): Boolean
}

data class BankItem(val id: Int, val quantity: Int)
