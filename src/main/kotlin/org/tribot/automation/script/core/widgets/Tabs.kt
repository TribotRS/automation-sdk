package org.tribot.automation.script.core.widgets

interface Tabs {
    fun open(tab: GameTab): Boolean
    fun getCurrent(): GameTab?
    fun isOpen(tab: GameTab): Boolean
}

enum class GameTab {
    COMBAT, SKILLS, QUESTS, INVENTORY, EQUIPMENT,
    PRAYER, MAGIC, CLAN, FRIENDS, ACCOUNT,
    LOGOUT, SETTINGS, EMOTES, MUSIC
}
