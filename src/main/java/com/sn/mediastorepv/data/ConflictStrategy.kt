package com.sn.mediastorepv.data

/**
 * File operations that can be performed in case of conflict
 */
enum class ConflictStrategy {
    SKIP, KEEP_BOTH, OVERWRITE
}