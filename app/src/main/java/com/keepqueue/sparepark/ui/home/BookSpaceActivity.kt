package com.keepqueue.sparepark.ui.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.keepqueue.sparepark.R
import com.keepqueue.sparepark.data.MyPreferences
import com.keepqueue.sparepark.data.model.Space
import com.keepqueue.sparepark.data.parcelable
import com.keepqueue.sparepark.databinding.ActivityBookSpaceBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import kotlin.math.floor


private const val TAG = "BookSpaceActivity"

class BookSpaceActivity: AppCompatActivity(), OnMapReadyCallback {

    companion object {
        final val SPACE = "space"
        final val START_TIME = "start_time"
        final val END_TIME = "end_time"
    }

    private lateinit var viewModel: BookSpaceViewModel

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityBookSpaceBinding
    private lateinit var space: Space
    private lateinit var startDate: String
    private lateinit var startTime: String
    private lateinit var endDate: String
    private lateinit var endTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookSpaceBinding.inflate(LayoutInflater.from(this))
        viewModel = ViewModelProvider(this).get(BookSpaceViewModel::class.java)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //R.layout.
        space = intent.parcelable(SPACE)!!
        val mapFragment = supportFragmentManager

            .findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //binding.tvName.text = space.name
        //binding.tvAddress.text = space.postCode
        //binding.tvTimeStart.text = intent.getStringExtra(START_TIME)
        //binding.tvTimeEnd.text = intent.getStringExtra(END_TIME)

        binding.bStart.setOnClickListener {
            //Get today's date and set it to the date picker!
            val calendar = Calendar.getInstance()
            val yearToday = calendar.get(Calendar.YEAR)
            val monthToday = calendar.get(Calendar.MONTH)
            val dayToday = calendar.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog(this@BookSpaceActivity, object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
                    startDate = "$year-${String.format("%02d", month)}-${String.format("%02d", day)}"
                    val calendar = Calendar.getInstance()
                    val hourNow = calendar.get(Calendar.HOUR_OF_DAY)
                    val minuteNow = calendar.get(Calendar.MINUTE)
                    TimePickerDialog(this@BookSpaceActivity, object: TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
                            startTime = "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
                            if (!startTime.isNullOrBlank() && !startDate.isNullOrBlank()) {
                                binding.etStartTime.setText("$startDate $startTime")
                            }
                        }

                    }, hourNow, minuteNow, DateFormat.is24HourFormat(this@BookSpaceActivity)).show()
                }
            }, yearToday, monthToday, dayToday).show()
        }

        binding.bEnd.setOnClickListener {
            //Get today's date and set it to the date picker!
            val calendar = Calendar.getInstance()
            val yearToday = calendar.get(Calendar.YEAR)
            val monthToday = calendar.get(Calendar.MONTH)
            val dayToday = calendar.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog(this, object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
                    endDate = "$year-${String.format("%02d", month)}-${String.format("%02d", day)}"
                    val calendar = Calendar.getInstance()
                    val hourNow = calendar.get(Calendar.HOUR_OF_DAY)
                    val minuteNow = calendar.get(Calendar.MINUTE)
                    TimePickerDialog(this@BookSpaceActivity, object: TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
                            endTime = "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
                            if (!endDate.isNullOrBlank() && !endTime.isNullOrBlank()) {
                                binding.etEndTime.setText("$endDate $endTime")
                            }
                        }

                    }, hourNow, minuteNow, DateFormat.is24HourFormat(this@BookSpaceActivity)).show()
                }
            }, yearToday, monthToday, dayToday).show()
        }

        binding.bBook.setOnClickListener {
            if (!MyPreferences.isLoggedIn(this@BookSpaceActivity)) {
                Toast.makeText(this@BookSpaceActivity, "Please login to book a space!", Toast.LENGTH_SHORT).show()
            } else if (binding.etStartTime.text.isEmpty()) {
                Toast.makeText(this@BookSpaceActivity, "Set start time!", Toast.LENGTH_SHORT).show()
            } else if (binding.etEndTime.text.isEmpty()) {
                Toast.makeText(this@BookSpaceActivity, "Set end time!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.bookSpace(
                    MyPreferences.getUserId(this@BookSpaceActivity),
                    space.id,
                    binding.etStartTime.text.toString(),
                    binding.etEndTime.text.toString()
                )
            }
        }

        viewModel.bookResult.observe(this) { result ->
            when (result) {
                is com.keepqueue.sparepark.data.response.Result.Success -> {
                    if (result.data) {
                        Toast.makeText(this@BookSpaceActivity, "Booked!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                is com.keepqueue.sparepark.data.response.Result.Loading -> {

                }
                is com.keepqueue.sparepark.data.response.Result.Error -> {
                    Toast.makeText(this@BookSpaceActivity, "error:${result.message}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "error:${result.message}")
                }
            }
        }

        binding.tvName.text = space.name
        binding.tvSpaceRate.text = "£${space.hourRate}/HR"
        binding.tvSpaceType.text = space.type
        binding.tvAddress.text = space.address
        binding.tvPostcode.text = space.postCode
        binding.tvDescription.text = space.description

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