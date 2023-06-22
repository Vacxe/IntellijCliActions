package io.github.vacxe.icmd.model

import kotlinx.serialization.Serializable

@Serializable
data class ICmdConfig(
    val groups: List<ICmdGroup>
)