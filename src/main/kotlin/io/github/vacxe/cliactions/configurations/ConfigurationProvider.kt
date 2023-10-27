package io.github.vacxe.cliactions.configurations

import java.io.File

interface ConfigurationProvider {
    fun subscribe(updateSubscription: (Sequence<File>) -> Unit)
    fun unsubscribe()
}