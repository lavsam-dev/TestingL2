package lavsam.gb.testingl2.view

import lavsam.gb.testingl2.model.SearchResult

internal interface ViewContract {
    fun displaySearchResults(
        searchResults: List<SearchResult>,
        totalCount: Int
    )

    fun displayError()
    fun displayError(error: String)
    fun displayLoading(show: Boolean)
}