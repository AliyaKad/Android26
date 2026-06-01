package ru.itis.android26.model

enum class UiVariant {
    CONTROL,
    ROUNDED,
    ICON;

    companion object {
        fun fromString(value: String): UiVariant = when (value.lowercase()) {
            "rounded" -> ROUNDED
            "icon" -> ICON
            else -> CONTROL
        }
    }
}