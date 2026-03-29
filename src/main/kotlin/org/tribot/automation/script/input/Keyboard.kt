package org.tribot.automation.script.input

import java.awt.event.InputEvent
import java.awt.event.KeyEvent

interface Keyboard {

    /** Type a single character (press + release with appropriate key code). */
    fun type(char: Char)

    /** Type a named key (press + release). */
    fun type(key: Key)

    /** Type each character in [text] sequentially with natural inter-key delays. */
    fun type(text: String)

    /**
     * Hold a key down, repeatedly sending press events, until [stopWhen] returns `true`.
     * Automatically releases the key when done.
     */
    fun hold(key: Key, stopWhen: () -> Boolean)

    /** Hold a character key until [stopWhen] returns `true`. */
    fun hold(char: Char, stopWhen: () -> Boolean)

    /**
     * Set a modifier key (SHIFT, CTRL, ALT) as held or released.
     * While held, the modifier applies to all subsequent input events.
     */
    fun holdModifier(key: Key, held: Boolean)

    /** Release all currently held keys. */
    fun releaseAll()
}

/** Keys that are not represented by a single obvious character. */
enum class Key(val code: Int, val char: Char, val mask: Int? = null) {
    SHIFT(KeyEvent.VK_SHIFT, KeyEvent.CHAR_UNDEFINED, InputEvent.SHIFT_DOWN_MASK),
    CONTROL(KeyEvent.VK_CONTROL, KeyEvent.CHAR_UNDEFINED, InputEvent.CTRL_DOWN_MASK),
    ALT(KeyEvent.VK_ALT, KeyEvent.CHAR_UNDEFINED, InputEvent.ALT_DOWN_MASK),
    BACKSPACE(KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED),
    TAB(KeyEvent.VK_TAB, '\t'),
    ESCAPE(KeyEvent.VK_ESCAPE, '\u001B'),
    SPACE(KeyEvent.VK_SPACE, ' '),
    ENTER(KeyEvent.VK_ENTER, '\n'),
    UP(KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED),
    DOWN(KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED),
    LEFT(KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED),
    RIGHT(KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED),
    F1(KeyEvent.VK_F1, KeyEvent.CHAR_UNDEFINED),
    F2(KeyEvent.VK_F2, KeyEvent.CHAR_UNDEFINED),
    F3(KeyEvent.VK_F3, KeyEvent.CHAR_UNDEFINED),
    F4(KeyEvent.VK_F4, KeyEvent.CHAR_UNDEFINED),
    F5(KeyEvent.VK_F5, KeyEvent.CHAR_UNDEFINED),
    F6(KeyEvent.VK_F6, KeyEvent.CHAR_UNDEFINED),
    F7(KeyEvent.VK_F7, KeyEvent.CHAR_UNDEFINED),
    F8(KeyEvent.VK_F8, KeyEvent.CHAR_UNDEFINED),
    F9(KeyEvent.VK_F9, KeyEvent.CHAR_UNDEFINED),
    F10(KeyEvent.VK_F10, KeyEvent.CHAR_UNDEFINED),
    F11(KeyEvent.VK_F11, KeyEvent.CHAR_UNDEFINED),
    F12(KeyEvent.VK_F12, KeyEvent.CHAR_UNDEFINED),
}
