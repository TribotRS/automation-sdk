package org.tribot.automation.script.core.tabs

interface Combat {
    fun getAttackStyleIndex(): Int
    fun setAttackStyle(index: Int): Boolean
    fun isAutoRetaliateOn(): Boolean
    fun setAutoRetaliate(enabled: Boolean): Boolean
    fun getWeaponName(): String?
}