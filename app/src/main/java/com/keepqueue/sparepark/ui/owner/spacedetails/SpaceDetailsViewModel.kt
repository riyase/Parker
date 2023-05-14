package com.keepqueue.sparepark.ui.owner.spacedetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepqueue.sparepark.data.ParkingApi
import com.keepqueue.sparepark.data.model.Booking
import com.keepqueue.sparepark.data.response.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "BookSpaceViewModel"

class SpaceDetailsViewModel: ViewModel() {


    private val _getBookingsResult = MutableLiveData<Result<List<Booking>>>()
    val bookingsResult = _getBookingsResult

    private var getBookingsJob: Job? = null

    fun getBookings(spaceId: Int) {
        //Show loading
        _getBookingsResult.value = Result.Loading
        //Cancel running search to start new searching
        getBookingsJob?.cancel()
        getBookingsJob = viewModelScope.launch {
            _getBookingsResult.value = try {
                val response = ParkingApi.retrofitService.getSpaceBookings(spaceId)
                if (response.status) {
                    Result.Success(response.bookings)
                } else {
                    Result.Error(java.lang.RuntimeException(), "Bookings not available!")
                }
            } catch (e: Exception) {
                Log.e(TAG, "error:${e.message}" )
                Result.Error(e)
            }
        }
    }

    private val _bookResult = MutableLiveData<Result<Boolean>>()
    val bookResult = _bookResult

    private var bookJob: Job? = null

    fun updateBookingStatus(bookingId: Int, status: String) {
        //Show loading
        _bookResult.value = Result.Loading
        //Cancel running job and start new one
        bookJob?.cancel()
        bookJob = viewModelScope.launch {
            _bookResult.value = try {
                val response = ParkingApi.retrofitService.updateBookingStatus(bookingId, status)
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