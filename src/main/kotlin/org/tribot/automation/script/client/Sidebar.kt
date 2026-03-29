package org.tribot.automation.script.client

import java.awt.image.BufferedImage
import javax.swing.JPanel

interface Sidebar {
    /**
     * Adds a sidebar tab with a Swing JPanel that appears in RuneLite's sidebar.
     * The tab is automatically removed when the script ends.
     *
     * @param tabName Unique name for the tab (used as tooltip and key for removal)
     * @param icon Optional 16x16 icon for the sidebar button; a default is used if null
     * @param panel The JPanel to display when the tab is selected
     */
    fun addSidebarTab(tabName: String, icon: BufferedImage?, panel: JPanel)

    /**
     * Removes a sidebar tab by name.
     *
     * @param tabName The name of the tab to remove
     */
    fun removeSidebarTab(tabName: String)
}