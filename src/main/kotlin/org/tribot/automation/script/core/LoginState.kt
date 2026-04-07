package org.tribot.automation.script.core

/**
 * Coarse-grained login state used by [Login] to describe what screen the
 * client is currently showing.
 */
enum class LoginState {
    /** Sitting on the credentials/login screen (or any of its sub-screens). */
    LOGIN_SCREEN,

    /** Logged in and showing the post-login "Click here to play" welcome screen. */
    WELCOME_SCREEN,

    /** Fully in-game. */
    INGAME,

    /** Could not be determined. */
    UNKNOWN,
}
