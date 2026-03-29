package org.tribot.automation.script.input

import net.runelite.api.MenuEntry

interface ChooseOption {
    fun isOpen(): Boolean
    fun getEntries(): List<MenuEntry>
    fun select(option: String): Boolean
    fun select(filter: (MenuEntry) -> Boolean): Boolean
    fun close()
}