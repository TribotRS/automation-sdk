package org.tribot.automation.script.core

interface LoginScreen {
    fun isLoggedIn(): Boolean
    fun isOnLoginScreen(): Boolean
    fun isOnWelcomeScreen(): Boolean
    fun login(): Boolean
    fun logout(): Boolean
    fun getCurrentWorld(): Int
    fun hopWorld(worldNumber: Int): Boolean
}
