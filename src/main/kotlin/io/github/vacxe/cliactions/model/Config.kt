package io.github.vacxe.cliactions.model

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val groups: List<Group>
)