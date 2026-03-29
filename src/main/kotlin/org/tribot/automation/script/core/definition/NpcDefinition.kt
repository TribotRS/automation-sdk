package org.tribot.automation.script.core.definition

interface NpcDefinition {
    val id: Int
    val name: String
    val combatLevel: Int
    val size: Int
    val actions: List<String?>
    val minimapVisible: Boolean
    val interactable: Boolean
    val isFollower: Boolean
    val params: Map<Int, Any>
    val category: Int
}
