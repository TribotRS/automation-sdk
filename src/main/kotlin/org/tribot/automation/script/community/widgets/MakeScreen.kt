package org.tribot.automation.script.community.widgets

import net.runelite.api.gameval.InterfaceID
import net.runelite.api.gameval.VarClientID
import net.runelite.api.widgets.Widget
import org.tribot.automation.script.ScriptContext

class MakeScreen(private val ctx: ScriptContext) {

    private val makeScreenTitleId = InterfaceID.Skillmulti.TITLE
    private val makeScreenInstructionsId = InterfaceID.Skillmulti.INSTRUCTIONS
    private val makeScreenXId = InterfaceID.Skillmulti.X

    fun isVisible(): Boolean {
        return getUnhiddenWidget(makeScreenTitleId) != null
    }

    fun make(itemId: Int): Boolean {
        val bottomWidget = getMakeScreenItems()
            .firstOrNull { it.makeScreenItemId == itemId } ?: return false

        return ctx.interaction.click(bottomWidget)
    }

    /**
     * Gets the quantity of items to make. ALL is represented as Int.MAX_VALUE.
     *
     * -1 is returned if the quantity cannot be determined, such as when the make screen is not visible.
     */
    fun getQuantity(): Int {
        val makeAllWidget = getUnhiddenWidget(InterfaceID.Skillmulti.ALL) ?: return -1
        if (makeAllWidget.actions.isNullOrEmpty()) { return Int.MAX_VALUE }

        val make1Widget = getUnhiddenWidget(InterfaceID.Skillmulti._1) ?: return -1
        if (make1Widget.actions.isNullOrEmpty()) { return 1 }
        val make5Widget = getUnhiddenWidget(InterfaceID.Skillmulti._5) ?: return -1
        if (make5Widget.actions.isNullOrEmpty()) { return 5 }
        val make10Widget = getUnhiddenWidget(InterfaceID.Skillmulti._10) ?: return -1
        if (make10Widget.actions.isNullOrEmpty()) { return 10 }

        val makeOtherWidget = getUnhiddenWidget(InterfaceID.Skillmulti.OTHER) ?: return -1
        if (makeOtherWidget.actions.isNullOrEmpty()) { return ctx.client.getVarcIntValue(VarClientID.SKILLMULTI_QUANTITY) }

        return -1
    }

    /**
     * Sets the quantity of items to make. ALL is represented as Int.MAX_VALUE.
     */
    fun setQuantity(quantity: Int): Boolean {
        if (quantity <= 0) { error("MakeScreen Quantity must be greater than 0") }
        if (quantity == getQuantity()) { return true }

        if (quantity == 1) {
            val make1Widget = getUnhiddenWidget(InterfaceID.Skillmulti._1) ?: return false
            if (make1Widget.actions.isNullOrEmpty()) { return true }
            ctx.interaction.click(make1Widget)
        } else if (quantity == 5) {
            val make5Widget = getUnhiddenWidget(InterfaceID.Skillmulti._5) ?: return false
            if (make5Widget.actions.isNullOrEmpty()) { return true }
            ctx.interaction.click(make5Widget)
        } else if (quantity == 10) {
            val make10Widget = getUnhiddenWidget(InterfaceID.Skillmulti._10) ?: return false
            if (make10Widget.actions.isNullOrEmpty()) { return true }
            ctx.interaction.click(make10Widget)
        } else if (quantity == Int.MAX_VALUE) {
            val makeAllWidget = getUnhiddenWidget(InterfaceID.Skillmulti.ALL) ?: return false
            if (makeAllWidget.actions.isNullOrEmpty()) { return true }
            ctx.interaction.click(makeAllWidget)
        } else {
            if (!ctx.enterAmount.isOpen()) {
                val makeXWidget = getUnhiddenWidget(makeScreenXId) ?: return false
                ctx.interaction.click(makeXWidget)
                val isOpen = ctx.waiting.sleepFramesUntil(40) { ctx.enterAmount.isOpen() }
                if (!isOpen) { return false }
            }
            ctx.enterAmount.enter(quantity)
            return ctx.waiting.sleepFramesUntil(30) { getQuantity() == quantity }
        }

        return false
    }

    /**
     * Gets the title text of the make screen, if it's visible.
     */
    fun getTitleText(): String? {
        // Using regular "client" over "clientRaw" since these involve client thread safety that I don't want to code manually
        val titleWidget = getUnhiddenWidget(makeScreenTitleId) ?: return null
        return titleWidget.text
    }

    /**
     * Gets the instructions text of the make screen, if it's visible.
     */
    fun getInstructionsText(): String? {
        val instructionsWidget = getUnhiddenWidget(makeScreenInstructionsId) ?: return null
        return instructionsWidget.text
    }

    fun getMakeScreenItems(): List<MakeScreenItem> {
        return itemWidgetIds
            .mapNotNull { getUnhiddenWidget(it) }
            .mapNotNull {
                // The widget that holds the item is a child of the whole
                val children = it.children ?: return@mapNotNull null
                var itemId = -1
                var itemQuantity = -1
                for (child in children) {
                    if (child.itemId != -1) {
                        itemId = child.itemId
                        itemQuantity = child.itemQuantity
                    }
                }

                MakeScreenItem(it, itemId, itemQuantity)
            }
    }

    private fun getUnhiddenWidget(id: Int): Widget? {
        return ctx.client.getWidget(id)
            ?.takeIf { !it.isHidden }
    }

    private val itemWidgetIds = listOf(
        InterfaceID.Skillmulti.A,
        InterfaceID.Skillmulti.B,
        InterfaceID.Skillmulti.C,
        InterfaceID.Skillmulti.D,
        InterfaceID.Skillmulti.E,
        InterfaceID.Skillmulti.F,
        InterfaceID.Skillmulti.G,
        InterfaceID.Skillmulti.H,
        InterfaceID.Skillmulti.I,
        InterfaceID.Skillmulti.J,
        InterfaceID.Skillmulti.K,
        InterfaceID.Skillmulti.L,
        InterfaceID.Skillmulti.M,
        InterfaceID.Skillmulti.N,
        InterfaceID.Skillmulti.O,
        InterfaceID.Skillmulti.P,
        InterfaceID.Skillmulti.Q,
        InterfaceID.Skillmulti.R,
    )
}

class MakeScreenItem(val widget: Widget, val makeScreenItemId: Int, val makeScreenQuantity: Int): Widget by widget