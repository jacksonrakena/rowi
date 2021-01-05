package com.abyssaldev.rowi.core

abstract class RowiIntegration {
    abstract fun onInstall(builder: CommandEngine.Builder)
}