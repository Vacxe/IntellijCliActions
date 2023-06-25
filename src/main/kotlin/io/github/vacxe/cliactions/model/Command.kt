package io.github.vacxe.cliactions.model

import kotlinx.serialization.Serializable

@Serializable
data class Command(
    val name: String,
    val command: String,
    val prompt: Boolean = false
)