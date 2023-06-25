package io.github.vacxe.cliactions.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val name: String,
    val commands: List<Command>
)