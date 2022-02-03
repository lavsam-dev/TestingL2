package lavsam.gb.testingl2

import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import lavsam.gb.testingl2.model.SearchResponse
import lavsam.gb.testingl2.presenter.SchedulerProviderStub
import lavsam.gb.testingl2.presenter.SearchPresenter
import lavsam.gb.testingl2.repository.GitHubRepository
import lavsam.gb.testingl2.view.ViewSearchContract
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SearchPresenterTestRx {
    private lateinit var presenter: SearchPresenter

    @Mock
    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var viewContract: ViewSearchContract

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        presenter = SearchPresenter(viewContract, repository, SchedulerProviderStub())
    }

    @Test
    fun searchGitHub_Test() {
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(1, listOf()))
        )
        presenter.searchGitHub(SEARCH_QUERY)
        verify(repository, times(1)).searchGithub(SEARCH_QUERY)
    }

    @Test
    fun handleRequestError_Test() {
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.error(
                Throwable(
                    ERROR_TEXT
                )
            )
        )
        presenter.searchGitHub(SEARCH_QUERY)
        verify(viewContract, times(1)).displayError("error")
    }

    @Test
    fun handleResponseError_TotalCountIsNull_ViewContractMethodOrder() {
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(null, listOf()))
        )
        presenter.searchGitHub(SEARCH_QUERY)
        val inOrder = inOrder(viewContract)
        inOrder.verify(viewContract).displayLoading(true)
        inOrder.verify(viewContract).displayError("Search results or total count are null")
        inOrder.verify(viewContract).displayLoading(false)
    }

    @Test
    fun handleResponseSuccess() {
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(SEARCH_TOTAL_COUNT, listOf()))
        )
        presenter.searchGitHub(SEARCH_QUERY)
        verify(viewContract, times(1)).displaySearchResults(listOf(), SEARCH_TOTAL_COUNT)
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
        private const val SEARCH_TOTAL_COUNT = 42
    }
}