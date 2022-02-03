package lavsam.gb.testingl2.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import lavsam.gb.testingl2.AppState
import lavsam.gb.testingl2.repository.GitHubApi
import lavsam.gb.testingl2.repository.GitHubRepository
import lavsam.gb.testingl2.repository.RepositoryContract
import lavsam.gb.testingl2.scheduler.ISchedulerProvider
import lavsam.gb.testingl2.scheduler.SearchSchedulerProvider
import lavsam.gb.testingl2.view.MainActivity.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchViewModel(
    private val repository: RepositoryContract = GitHubRepository(
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(GitHubApi::class.java)
    ),
    private val appSchedulerProvider: ISchedulerProvider = SearchSchedulerProvider()
): ViewModel() {
    private val _liveData = MutableLiveData<AppState>()
    private val liveData: LiveData<AppState> = _liveData
    private var compositeDisposable = CompositeDisposable()
    private val viewModelCoroutinesScope = CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    })

    private fun handleError(error: Throwable) {
        _liveData.value = AppState.Error(Throwable(error.message ?: ERROR))
    }

    fun subscribeToLiveData() = liveData

    fun searchGitHub(searchQuery: String) {
        _liveData.value = AppState.Loading
        viewModelCoroutinesScope.launch {
            val searchResponse = repository.searchGithubAsync(searchQuery)
            val searchResults = searchResponse.searchResults
            val totalCount = searchResponse.totalCount
            if (searchResults != null && totalCount != null) {
                _liveData.postValue(AppState.Working(searchResponse))
            } else {
                _liveData.postValue(AppState.Error(Throwable(ERROR)))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelCoroutinesScope.coroutineContext.cancelChildren()
    }

    companion object {
        private const val ERROR = "Search results or total count are null"
        private const val ERROR_RESPONSE = "Response is null or unsuccessful"
    }
}