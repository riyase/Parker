package com.keepqueue.sparepark.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepqueue.sparepark.data.SpareParkApi
import com.keepqueue.sparepark.data.response.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "BookSpaceViewModel"

class BookSpaceViewModel: ViewModel() {

    private val _bookResult = MutableLiveData<Result<Boolean>>()
    val bookResult = _bookResult

    private var bookJob: Job? = null

    fun bookSpace(userId: Int, spaceId: Int, timeStart: String, timeEnd: String) {
        //Show loading
        _bookResult.value = Result.Loading
        //Cancel running job and start new one
        bookJob?.cancel()
        bookJob = viewModelScope.launch {
            _bookResult.value = try {
                val response = SpareParkApi.retrofitService.bookSpace(userId, spaceId, timeStart, timeEnd)
                if (response.status) {
                    Result.Success(true)
                } else {
                    Result.Error(java.lang.RuntimeException(), "message:${response.message}!")
                }
            } catch (e: Exception) {
                Log.e(TAG, "error:${e.message}" )
                Result.Error(e)
            }
        }
    }
}