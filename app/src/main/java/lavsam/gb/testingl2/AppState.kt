package lavsam.gb.testingl2

import lavsam.gb.testingl2.model.SearchResponse

sealed class AppState {
    object Loading: AppState()
    data class Working(val searchResponse: SearchResponse) : AppState()
    data class Error(val error: Throwable) : AppState()
}