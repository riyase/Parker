package com.keepqueue.sparepark.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepqueue.sparepark.data.SpareParkApi
import com.keepqueue.sparepark.data.response.RegisterResponse
import com.keepqueue.sparepark.data.response.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "RegisterViewModel"
class RegisterViewModel: ViewModel() {

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult = _registerResult

    private var registerJob: Job? = null

    fun register(userName: String, email: String, phone: String, password: String) {
        //Show loading
        _registerResult.value = Result.Loading

        registerJob?.cancel()
        registerJob = viewModelScope.launch {
            _registerResult.value = try {
                val response = SpareParkApi.retrofitService.register(userName, email, phone, password)
                if (response.status) {
                    Result.Success(response)
                } else {
                    Result.Error(java.lang.RuntimeException(), "Error: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "error:${e.message}" )
                Result.Error(e)
            }
        }
    }
}