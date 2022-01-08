package lavsam.gb.testingl2.presenter

import lavsam.gb.testingl2.model.SearchResponse
import lavsam.gb.testingl2.repository.GitHubRepository
import lavsam.gb.testingl2.view.ViewContract
import retrofit2.Response

const val SEARCH_RESULTS_NULL = "Search results or total count are null"
const val RESPONSE_NULL = "Response is null or unsuccessful"

internal class SearchPresenter internal constructor(
    private val viewContract: ViewContract,
    private val repository: GitHubRepository
) : PresenterContract, GitHubRepository.GitHubRepositoryCallback {

    override fun searchGitHub(searchQuery: String) {
        viewContract.displayLoading(true)
        repository.searchGithub(searchQuery, this)
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