package io.github.vacxe.icmd.model

import kotlinx.serialization.Serializable

@Serializable
data class ICmdCommand(
    val name: String,
    val command: String,
    val prompt: Boolean = false
)