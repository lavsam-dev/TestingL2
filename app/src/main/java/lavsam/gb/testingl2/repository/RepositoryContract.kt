package lavsam.gb.testingl2.repository

internal interface RepositoryContract {
    fun searchGithub(
        query: String,
        callback: GitHubRepositoryCallback
    )
}