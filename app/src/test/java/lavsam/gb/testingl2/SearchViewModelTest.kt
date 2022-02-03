package lavsam.gb.testingl2

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import lavsam.gb.testingl2.model.SearchResponse
import lavsam.gb.testingl2.presenter.SchedulerProviderStub
import lavsam.gb.testingl2.presenter.SearchViewModel
import lavsam.gb.testingl2.repository.FakeGitHubRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SearchViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: FakeGitHubRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchViewModel = SearchViewModel(repository, SchedulerProviderStub())
    }

    @Test
    fun search_Test() {
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(1, listOf()))
        )
        searchViewModel.searchGitHub(SEARCH_QUERY)
        verify(repository, times(1)).searchGithub(SEARCH_QUERY)
    }

    @Test
    fun search_TestReturnError() {
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(Observable.error(Throwable(ERROR_TEXT)))
        searchViewModel.searchGitHub(SEARCH_QUERY)
        verify(repository, times(1)).searchGithub(SEARCH_QUERY)
    }

    @Test
    fun liveData_TestReturnValueIsNotNull() {
        val observer = Observer<AppState> {}
        val liveData = searchViewModel.subscribeToLiveData()

        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(1, listOf()))
        )
        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)
            Assert.assertNotNull(liveData.value)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test
    fun liveData_TestReturnValueIsError() {
        val observer = Observer<AppState>{}
        val liveData = searchViewModel.subscribeToLiveData()
        val error = Throwable(ERROR_TEXT)
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(Observable.error(error))
        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)
            val value: AppState.Error = liveData.value as AppState.Error
            Assert.assertEquals(value.error.message, error.message)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
    }
}