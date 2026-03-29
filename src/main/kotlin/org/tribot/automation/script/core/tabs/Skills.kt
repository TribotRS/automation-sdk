package org.tribot.automation.script.core.tabs

import net.runelite.api.Skill

interface Skills {
    fun getLevel(skill: Skill): Int
    fun getBoostedLevel(skill: Skill): Int
    fun getXp(skill: Skill): Int
    fun getXpToNextLevel(skill: Skill): Int
    fun getXpToLevel(skill: Skill, targetLevel: Int): Int
    fun getPercentToNextLevel(skill: Skill): Int
}