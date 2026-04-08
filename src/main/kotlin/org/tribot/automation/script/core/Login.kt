package org.tribot.automation.script.core

/**
 * High-level access to the pre-game login flow: state inspection, credential entry,
 * automatic login retry, and world hopping. The in-game logout action lives on
 * [org.tribot.automation.script.core.tabs.Logout] since it is performed via the
 * Logout tab.
 */
interface Login {
    // --- State queries ---

    /** Coarse-grained current login state (login screen / welcome / in-game / unknown). */
    fun getState(): LoginState

    /** Convenience for `getState() == LoginState.INGAME`. */
    fun isLoggedIn(): Boolean

    /** Convenience for `getState() == LoginState.LOGIN_SCREEN`. */
    fun isOnLoginScreen(): Boolean

    /** Convenience for `getState() == LoginState.WELCOME_SCREEN`. */
    fun isOnWelcomeScreen(): Boolean

    /**
     * The current recognized login screen message, or null if no recognized message
     * is showing (e.g. when in-game, or when the login screen text doesn't match
     * any known case).
     */
    fun getLoginMessage(): LoginMessage?

    /** Full trimmed text from the three login response lines. */
    fun getLoginResponse(): String

    /**
     * The display name of the account currently selected for login. For Jagex
     * accounts this is the character id; for legacy accounts the username.
     */
    fun getLoginName(): String

    // --- Login / hop ---

    /**
     * Logs in using the account selected when the script was started, or relogs
     * with the cached credentials if no script-account is bound. Returns true on
     * a successful login.
     */
    fun login(): Boolean

    /** Logs in with explicit credentials. Pass `totpSecretKey` to handle authenticator prompts. */
    fun login(username: String, password: String, totpSecretKey: String? = null): Boolean
}
