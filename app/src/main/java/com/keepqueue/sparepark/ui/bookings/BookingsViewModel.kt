package com.keepqueue.sparepark.ui.bookings

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepqueue.sparepark.data.ParkingApi
import com.keepqueue.sparepark.data.model.Booking
import com.keepqueue.sparepark.data.response.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "BookingsViewModel"

class BookingsViewModel : ViewModel() {

    private val _getBookingsResult = MutableLiveData<Result<List<Booking>>>()
    val getBookingsResult = _getBookingsResult

    private var getBookingsJob: Job? = null

    fun getMyBookings(userId: Int) {
        //Show loading
        _getBookingsResult.value = Result.Loading
        //Cancel running search to start new searching
        getBookingsJob?.cancel()
        getBookingsJob = viewModelScope.launch {
            _getBookingsResult.value = try {
                val response = ParkingApi.retrofitService.getMyBookings(userId)
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

    private val _cancelBookingResult = MutableLiveData<Result<Boolean>>()
    val cancelBookingResult = _cancelBookingResult

    private var cancelBookingsJob: Job? = null

    fun cancelBooking(bookingId: Int) {
        //Show loading
        _cancelBookingResult.value = Result.Loading
        cancelBookingsJob?.cancel()
        cancelBookingsJob = viewModelScope.launch {
            _cancelBookingResult.value = try {
                val response = ParkingApi.retrofitService.updateBookingStatus(bookingId, "cancelled")
                if (response.status) {
                    Result.Success(true)
                } else {
                    Result.Error(java.lang.RuntimeException(), "Bookings not available!")
                }
            } catch (e: Exception) {
                Log.e(TAG, "error:${e.message}" )
                Result.Error(e)
            }
        }
    }

    private val _submitReviewResult = MutableLiveData<Result<Boolean>>()
    val submitReviewResult = _submitReviewResult

    private var submitReviewJob: Job? = null

    fun submitReview(bookingId: Int, rating: Int, review: String) {
        //Show loading
        _submitReviewResult.value = Result.Loading
        submitReviewJob?.cancel()
        submitReviewJob = viewModelScope.launch {
            _submitReviewResult.value = try {
                val response = ParkingApi.retrofitService.submitReview(bookingId, rating, review)
                if (response.status) {
                    Result.Success(true)
                } else {
                    Result.Error(java.lang.RuntimeException(), "Bookings not available!")
                }
            } catch (e: Exception) {
                Log.e(TAG, "error:${e.message}" )
                Result.Error(e)
            }
        }
    }


}