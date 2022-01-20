package lavsam.gb.testingl2.presenter

import lavsam.gb.testingl2.model.SearchResponse
import lavsam.gb.testingl2.repository.GitHubRepositoryCallback
import lavsam.gb.testingl2.repository.RepositoryContract
import lavsam.gb.testingl2.view.ViewContract
import lavsam.gb.testingl2.view.ViewSearchContract
import retrofit2.Response

const val SEARCH_RESULTS_NULL = "Search results or total count are null"
const val RESPONSE_NULL = "Response is null or unsuccessful"

internal class SearchPresenter internal constructor(
    private val viewContract: ViewSearchContract,
    private val repository: RepositoryContract,
) : PresenterSearchContract, GitHubRepositoryCallback {

    private var view: ViewContract? = null

    fun getView() = view

    override fun searchGitHub(searchQuery: String) {
        viewContract.displayLoading(true)
        repository.searchGithub(searchQuery, this)
    }

    override fun onAttach(view: ViewContract?) {
        if (this.view != view) {
            this.view = view
        }
    }

    override fun onDetach() {
        view = null
    }

    override fun handleGitHubResponse(response: Response<SearchResponse?>?) {
        viewContract.displayLoading(false)
        if (response != null && response.isSuccessful) {
            val searchResponse = response.body()
            val searchResults = searchResponse?.searchResults
            val totalCount = searchResponse?.totalCount
            if (searchResults != null && totalCount != null) {
                viewContract.displaySearchResults(
                    searchResults,
                    totalCount
                )
            } else {
                viewContract.displayError(SEARCH_RESULTS_NULL)
            }
        } else {
            viewContract.displayError(RESPONSE_NULL)
        }
    }

    override fun handleGitHubError() {
        viewContract.displayLoading(false)
        viewContract.displayError()
    }
}