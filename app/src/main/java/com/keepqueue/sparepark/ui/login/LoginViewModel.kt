package com.keepqueue.sparepark.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepqueue.sparepark.data.SpareParkApi
import com.keepqueue.sparepark.data.response.LoginResponse
import com.keepqueue.sparepark.data.response.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "LoginViewModel"

class LoginViewModel: ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult = _loginResult

    private var loginJob: Job? = null

    fun login(email: String, password: String) {
        //Show loading
        _loginResult.value = Result.Loading
        //Cancel running search to start new searching
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            _loginResult.value = try {
                val response = SpareParkApi.retrofitService.login(email, password)
                if (response.status) {
                    Result.Success(response)
                } else {
                    Result.Error(java.lang.RuntimeException(), "Error: ${response.status}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "error:${e.message}" )
                Result.Error(e)
            }
        }
    }
}