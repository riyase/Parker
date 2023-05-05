package com.keepqueue.sparepark.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepqueue.sparepark.data.SpareParkApi
import com.keepqueue.sparepark.data.model.Space
import com.keepqueue.sparepark.data.response.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"
class HomeViewModel : ViewModel() {

    private val _searchResult = MutableLiveData<Result<List<Space>>>()
    val searchResult = _searchResult

    private var searchJob: Job? = null

    fun searchSpaces(location: String, timeStart: String, timeEnd: String) {
        //Show loading
        _searchResult.value = Result.Loading
        //Cancel running search to start new searching
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _searchResult.value = try {
                val response = SpareParkApi.retrofitService.searchSpaces(location, timeStart, timeEnd)
                if (response.status) {
                    Result.Success(response.spaces)
                } else {
                    Result.Error(java.lang.RuntimeException(), "Space not available!")
                }
            } catch (e: Exception) {
                Log.e(TAG, "error:${e.message}" )
                Result.Error(e)
            }
        }
    }



}