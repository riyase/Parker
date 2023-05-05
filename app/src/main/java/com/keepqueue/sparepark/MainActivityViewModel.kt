package com.keepqueue.sparepark

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepqueue.sparepark.data.SpareParkApi
import com.keepqueue.sparepark.data.response.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "MainActivityViewModel"

class MainActivityViewModel: ViewModel() {

    private val _logoutResult = MutableLiveData<Result<Boolean>>()
    val logoutResult = _logoutResult

    private var logoutJob: Job? = null

    fun logout() {
        //Show loading
        _logoutResult.value = Result.Loading

        //Cancel running search to start new searching
        logoutJob?.cancel()
        logoutJob = viewModelScope.launch {
            delay(1000)
            _logoutResult.value = try {
                val response = SpareParkApi.retrofitService.logout()
                if (response.status) {
                    Result.Success(true)
                } else {
                    Result.Error(java.lang.RuntimeException(), "Error: ${response.status}")
                }
                Result.Success(true)
            } catch (e: Exception) {
                Log.e(TAG, "error:${e.message}" )
                Result.Error(e)
            }
        }
    }
}