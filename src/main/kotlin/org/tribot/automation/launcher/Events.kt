package org.tribot.automation.launcher

data class ClientEvent(
    val clientId: String,
    val eventName: String,
    val eventData: String
)
