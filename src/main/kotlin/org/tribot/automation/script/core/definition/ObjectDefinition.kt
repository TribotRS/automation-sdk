package org.tribot.automation.script.core.definition

interface ObjectDefinition {
    val id: Int
    val name: String
    val sizeX: Int
    val sizeY: Int
    val actions: List<String?>
    val isWalkable: Boolean
    val blocksProjectile: Boolean
    val animationId: Int
    val params: Map<Int, Any>
    val category: Int
}
