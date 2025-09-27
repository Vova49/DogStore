package com.example.dogstore.data

/**
 * Sealed class representing different loading states
 */
sealed class LoadingState {
    object Idle : LoadingState()
    object Loading : LoadingState()
    data class Error(val message: String) : LoadingState()
}

/**
 * Data class to track loading progress for multiple items
 */
data class LoadingProgress(
    val totalItems: Int = 0,
    val loadedItems: Int = 0
)

/**
 * Utility class to manage loading states across the app
 */
class LoadingStateManager {
    private var currentState: LoadingState = LoadingState.Idle
    private var loadingProgress: LoadingProgress = LoadingProgress()

    fun startLoading(totalItems: Int = 0) {
        currentState = LoadingState.Loading
        loadingProgress = LoadingProgress(
            totalItems = totalItems
        )
    }
    
    fun updateItemsLoaded(count: Int = 1) {
        loadingProgress = loadingProgress.copy(
            loadedItems = loadingProgress.loadedItems + count
        )
    }
    
    fun setError(message: String) {
        currentState = LoadingState.Error(message)
    }
}