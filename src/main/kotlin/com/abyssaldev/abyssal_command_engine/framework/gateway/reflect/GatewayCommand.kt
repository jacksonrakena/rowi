package com.abyssaldev.abyssal_command_engine.framework.gateway.reflect

annotation class GatewayCommand(val name: String, val description: String, val isOwnerRestricted: Boolean = false, val requiredRole: String = "")