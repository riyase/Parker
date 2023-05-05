package com.keepqueue.sparepark.data

import com.keepqueue.sparepark.data.response.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

//private const val BASE_URL = "http://localhost/spare_park/"
private const val BASE_URL = "http://10.0.2.2/spare_park/"
//private const val BASE_URL = "http://192.168.0.2/spare_park/"
//private const val BASE_URL = "http://10.160.68.85/spare_park/"

private val retrofit = Retrofit.Builder()
    //.addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface SpareParkApiService {

    @FormUrlEncoded
    @POST("api/auth/signin.php")
    @JvmSuppressWildcards
    suspend fun login(@Field("email") email: String,
                      @Field("password") password: String) : LoginResponse

    @FormUrlEncoded
    @POST("api/auth/signup.php")
    @JvmSuppressWildcards
    suspend fun register(@Field("name") userName: String,
                         @Field("email") email: String,
                         @Field("phone") phone: String,
                         @Field("password") password: String) : RegisterResponse

    @GET("api/auth/signout.php")
    suspend fun logout() : LogoutResponse

    @FormUrlEncoded
    @POST("api/space/add_space.php")
    suspend fun addSpace(@Field("userId") userId: Int,
                         @Field("name") name: String,
                         @Field("hour_rate") hour_rate: Double,
                         @Field("type") type: String,
                         @Field("address") address: String,
                         @Field("postcode") postCode: String,
                         @Field("latitude")  latitude: Double,
                         @Field("longitude") longitude: Double): AddSpaceResponse

    @GET("api/space/get_space.php")
    suspend fun getSpace(spaceId: Int)

    @POST("api/space/add_space.php")
    suspend fun updateSpace(id: Int,
                            name: String,
                            type:String,
                            hour_rate: String,
                            address: String,
                            postCode: String,
                            latitude: Double,
                            longitude: Double,
                            description: String)

    @POST("api/space/remove_space.php")
    suspend fun deleteSpace(id: Int)

    @GET("api/space/get_my_spaces.php")
    suspend fun getMySpaces(@Query("userId") userId: Int): GetSpacesResponse

    //@FormUrlEncoded
    @GET("api/space/search_space.php")
    suspend fun searchSpaces(
        @Query("location") location: String,
        @Query("timeFrom") timeFrom: String,
        @Query("timeTo") timeTo: String): SearchSpaceResponse

    @FormUrlEncoded
    @POST("api/booking/book_space.php")
    @JvmSuppressWildcards
    suspend fun bookSpace(@Field("userId") userId: Int,
                          @Field("spaceId") spaceId: Int,
                          @Field("timeFrom") timeStart: String,
                          @Field("timeTo") timeEnd: String): BookSpaceResponse

    @FormUrlEncoded
    @POST("api/booking/update_booking_status.php")
    @JvmSuppressWildcards
    suspend fun updateBookingStatus(@Field("id") bookingId: Int,
                                    @Field("status") status: String): UpdateBookingStatusResponse

    @GET("api/booking/get_my_bookings.php")
    suspend fun getMyBookings(@Query("userId") userId: Int): GetBookingsResponse

    @GET("api/booking/get_space_bookings.php")
    suspend fun getSpaceBookings(@Query("id") spaceId: Int): GetBookingsResponse

    @FormUrlEncoded
    @POST("api/booking/set_booking_review.php")
    @JvmSuppressWildcards
    suspend fun submitReview(@Field("id") bookingId: Int,
                             @Field("rating") rating: Int,
                             @Field("review") review: String): SubmitReviewResponse
}

object SpareParkApi {
    val retrofitService: SpareParkApiService by lazy {
        retrofit.create(SpareParkApiService::class.java)
    }
}
