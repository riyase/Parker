package com.keepqueue.sparepark.ui.owner.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepqueue.sparepark.data.ParkingApi
import com.keepqueue.sparepark.data.response.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.RuntimeException

private const val TAG = "AddSpaceViewModel"
class AddSpaceViewModel: ViewModel() {

    private val _addSpaceResult = MutableLiveData<Result<Boolean>>()
    val addSpaceResult = _addSpaceResult

    private var addSpaceJob: Job? = null

    fun addSpace(userId: Int, name: String, rate: Double,
                 address: String, postCode: String, latitude: Double, longitude: Double) {
        //Show loading
        _addSpaceResult.value = Result.Loading
        //Cancel running job and start new one
        addSpaceJob?.cancel()
        addSpaceJob = viewModelScope.launch {
            _addSpaceResult.value = try {
                val response = ParkingApi.retrofitService.addSpace(
                    userId, name, rate, "car", address,
                    postCode, latitude, longitude)
                if (response.status) {
                    Result.Success(true)
                } else {
                    Result.Error(RuntimeException(), "message: ${response.message}!")
                }
            } catch (e: Exception) {
                Log.e(TAG, "error:${e.message}")
                Result.Error(e)
            }
        }
    }
}