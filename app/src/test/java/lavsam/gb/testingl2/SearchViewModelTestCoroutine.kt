package lavsam.gb.testingl2

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SearchViewModelTestCoroutine {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: FakeGitHubRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchViewModel = SearchViewModel(repository, SchedulerProviderStub())
    }

    @Test
    fun coroutines_TestReturnValueIsNotNull() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<AppState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(1, listOf())
            )
            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                Assert.assertNotNull(liveData.value)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun coroutines_TestReturnValueIsError() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<AppState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(null, listOf())
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val value: AppState.Error = liveData.value as AppState.Error
                Assert.assertEquals(value.error.message, "Search results or total count are null")
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun coroutines_TestException() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<AppState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val value: AppState.Error = liveData.value as AppState.Error
                Assert.assertEquals(value.error.message, "Search results or total count are null")
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun coroutine_SuccessTest(){
        testCoroutineRule.runBlockingTest {
            val observer = Observer<AppState> {}
            val liveData = searchViewModel.subscribeToLiveData()
            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(
                    1,
                    listOf()
                )
            )
            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                val value = liveData.value as AppState.Working
                Assert.assertEquals(value.searchResponse, SearchResponse(1, listOf()))
            } finally {
                liveData.removeObserver(observer)
            }

        }
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
    }
}