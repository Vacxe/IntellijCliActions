package io.github.vacxe.cliactions.configurations

import java.io.File

interface ConfigurationProvider {
    fun find(result: (Sequence<File>) -> Unit)
}