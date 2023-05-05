package com.keepqueue.sparepark.ui.owner.spacedetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.keepqueue.sparepark.R
import com.keepqueue.sparepark.data.model.Booking
import com.keepqueue.sparepark.data.model.Space
import com.keepqueue.sparepark.data.parcelable
import com.keepqueue.sparepark.data.response.Result
import com.keepqueue.sparepark.databinding.ActivitySpaceDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.floor


private const val TAG = "BookSpaceActivity"

class SpaceDetailsActivity: AppCompatActivity(), OnMapReadyCallback {

    companion object {
        final val SPACE = "space"
        final val START_TIME = "start_time"
        final val END_TIME = "end_time"
    }

    private val listItemListener = object: SpaceBookingAdapter.Listener {
        override fun onAcceptClicked(booking: Booking) {
            viewModel.updateBookingStatus(booking.id, "accepted")
        }

        override fun onRejectClicked(booking: Booking) {
            viewModel.updateBookingStatus(booking.id, "rejected")
        }

        override fun onCompleteClicked(booking: Booking) {
            viewModel.updateBookingStatus(booking.id, "completed")
        }

    }

    private lateinit var viewModel: SpaceDetailsViewModel

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySpaceDetailsBinding
    private val adapter = SpaceBookingAdapter(listItemListener)
    private lateinit var space: Space

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpaceDetailsBinding.inflate(LayoutInflater.from(this))
        viewModel = ViewModelProvider(this).get(SpaceDetailsViewModel::class.java)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //R.layout.
        space = intent.parcelable(SPACE)!!
        val mapFragment = supportFragmentManager

            .findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.tvName.text = space.name
        binding.tvSpaceRate.text = "£${space.hourRate}/HR"
        binding.tvSpaceType.text = space.type
        binding.tvAddress.text = space.address
        binding.tvPostcode.text = space.postCode
        binding.tvDescription.text = space.description

        binding.ratingBar.rating = floor(space.rating).toFloat()


        viewModel.getBookings(space.id)

        viewModel.bookingsResult.observe(this) { result ->
            when(result) {
                is Result.Success -> {
                    adapter.setBookingList(result.data)
                }
                is Result.Error -> {
                    result.message?.let { message ->
                        Toast.makeText(this@SpaceDetailsActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    //show loading
                }
            }
        }
        viewModel.bookResult.observe(this) { result ->
            when (result) {
                is com.keepqueue.sparepark.data.response.Result.Success -> {
                    if (result.data) {
                        Toast.makeText(this@SpaceDetailsActivity, "Booked!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                is com.keepqueue.sparepark.data.response.Result.Loading -> {

                }
                is com.keepqueue.sparepark.data.response.Result.Error -> {
                    Toast.makeText(this@SpaceDetailsActivity, "error:${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "error:${result.message}")
                }
            }
        }

        binding.rvBookings.layoutManager = LinearLayoutManager(this)
        binding.rvBookings.adapter = adapter
        binding.ratingBar.rating = floor(space.rating).toFloat()

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val spaceLoc = LatLng(space.latitude, space.longitude)
        mMap.addMarker(MarkerOptions()
            .position(spaceLoc)
            .title(space.name))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spaceLoc, 17.0F))
    }


}