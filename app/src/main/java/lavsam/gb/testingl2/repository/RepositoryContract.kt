package lavsam.gb.testingl2.repository

import io.reactivex.Observable
import lavsam.gb.testingl2.model.SearchResponse

interface RepositoryContract {
    fun searchGithub(
        query: String,
        callback: GitHubRepositoryCallback
    )

    fun searchGithub(
        query: String
    ): Observable<SearchResponse>

    suspend fun searchGithubAsync(query: String): SearchResponse
}