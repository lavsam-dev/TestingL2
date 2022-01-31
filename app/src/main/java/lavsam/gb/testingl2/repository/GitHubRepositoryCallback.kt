package lavsam.gb.testingl2.repository

import lavsam.gb.testingl2.model.SearchResponse
import retrofit2.Response

interface GitHubRepositoryCallback {
    fun handleGitHubResponse(response: Response<SearchResponse?>?)
    fun handleGitHubError()
}