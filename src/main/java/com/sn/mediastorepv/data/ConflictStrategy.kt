package com.sn.mediastorepv.data

/**
 * File operations that can be performed in case of conflict
 */
enum class ConflictStrategy(val value: Int) {
    SKIP(0), KEEP_BOTH(1), OVERWRITE(2);

    companion object {
        fun getByValue(value: Int): ConflictStrategy? {
            return values().find { it.value == value }
        }
    }
}