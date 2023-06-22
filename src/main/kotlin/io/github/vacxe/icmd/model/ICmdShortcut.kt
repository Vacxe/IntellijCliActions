package io.github.vacxe.icmd.model

import kotlinx.serialization.Serializable

@Serializable
data class ICmdShortcut(
    val name: String,
    val command: String,
    val description: String? = null
)