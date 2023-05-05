package com.keepqueue.sparepark.ui.owner

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepqueue.sparepark.data.SpareParkApi
import com.keepqueue.sparepark.data.model.Space
import com.keepqueue.sparepark.data.response.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "OwnerViewModel";
class OwnerViewModel : ViewModel() {

    private val _getSpacesResult = MutableLiveData<Result<List<Space>>>()
    val getSpacesResult = _getSpacesResult

    private var getSpacesJob: Job? = null

    fun getSpaces(userId: Int) {
        //Show loading
        _getSpacesResult.value = Result.Loading
        //Cancel running search to start new searching
        getSpacesJob?.cancel()
        getSpacesJob = viewModelScope.launch {
            _getSpacesResult.value = try {
                val response = SpareParkApi.retrofitService.getMySpaces(userId)
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