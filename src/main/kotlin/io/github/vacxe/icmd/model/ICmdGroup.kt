package io.github.vacxe.icmd.model

import kotlinx.serialization.Serializable

@Serializable
data class ICmdGroup(
    val name: String,
    val commands: List<ICmdCommand>
)