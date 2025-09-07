package com.example.dogstore.data

/**
 * Sealed class representing different loading states
 */
sealed class LoadingState {
    object Idle : LoadingState()
    object Loading : LoadingState()
    object Success : LoadingState()
    data class Error(val message: String) : LoadingState()
}

/**
 * Data class to track loading progress for multiple items
 */
data class LoadingProgress(
    val totalItems: Int = 0,
    val loadedItems: Int = 0,
    val isLoadingImages: Boolean = false,
    val loadedImages: Int = 0,
    val totalImages: Int = 0
) {
    val isComplete: Boolean
        get() = loadedItems >= totalItems && loadedImages >= totalImages && !isLoadingImages
    
    val progress: Float
        get() = if (totalItems + totalImages == 0) 0f 
                else (loadedItems + loadedImages).toFloat() / (totalItems + totalImages).toFloat()
}

/**
 * Utility class to manage loading states across the app
 */
class LoadingStateManager {
    private var currentState: LoadingState = LoadingState.Idle
    private var loadingProgress: LoadingProgress = LoadingProgress()
    
    fun getCurrentState(): LoadingState = currentState
    fun getProgress(): LoadingProgress = loadingProgress
    
    fun startLoading(totalItems: Int = 0, totalImages: Int = 0) {
        currentState = LoadingState.Loading
        loadingProgress = LoadingProgress(
            totalItems = totalItems,
            totalImages = totalImages
        )
    }
    
    fun updateItemsLoaded(count: Int = 1) {
        loadingProgress = loadingProgress.copy(
            loadedItems = loadingProgress.loadedItems + count
        )
    }
    
    fun updateImagesLoaded(count: Int = 1) {
        loadingProgress = loadingProgress.copy(
            loadedImages = loadingProgress.loadedImages + count
        )
    }
    
    fun setLoadingImages(isLoading: Boolean) {
        loadingProgress = loadingProgress.copy(
            isLoadingImages = isLoading
        )
    }
    
    fun completeLoading() {
        currentState = LoadingState.Success
        loadingProgress = loadingProgress.copy(
            loadedItems = loadingProgress.totalItems,
            loadedImages = loadingProgress.totalImages,
            isLoadingImages = false
        )
    }
    
    fun setError(message: String) {
        currentState = LoadingState.Error(message)
    }
    
    fun isLoadingComplete(): Boolean {
        return currentState is LoadingState.Success || 
               (currentState is LoadingState.Loading && loadingProgress.isComplete)
    }
}