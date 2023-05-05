package com.keepqueue.sparepark.ui.owner.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.keepqueue.sparepark.data.MyPreferences
import com.keepqueue.sparepark.data.model.Space
import com.keepqueue.sparepark.data.parcelable
import com.keepqueue.sparepark.databinding.ActivityAddSpaceBinding

import com.google.android.gms.maps.GoogleMap

private const val TAG = "AddSpaceActivity"

class AddSpaceActivity: AppCompatActivity() {

    companion object {
        final val SPACE = "space"
        final val START_TIME = "start_time"
        final val END_TIME = "end_time"
    }

    private lateinit var viewModel: AddSpaceViewModel

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityAddSpaceBinding
    private var space: Space? = null
    private lateinit var startDate: String
    private lateinit var startTime: String
    private lateinit var endDate: String
    private lateinit var endTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSpaceBinding.inflate(LayoutInflater.from(this))
        viewModel = ViewModelProvider(this).get(AddSpaceViewModel::class.java)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //R.layout.
        space = intent.parcelable(SPACE)


        //binding.tvName.text = space.name
        //binding.tvAddress.text = space.postCode
        //binding.tvTimeStart.text = intent.getStringExtra(START_TIME)
        //binding.tvTimeEnd.text = intent.getStringExtra(END_TIME)

        binding.bAdd.setOnClickListener {
            if (binding.etName.text.isEmpty()) {
                Toast.makeText(this@AddSpaceActivity, "Set a name!", Toast.LENGTH_SHORT).show()
            } else if (binding.etRate.text.isEmpty()) {
                Toast.makeText(this@AddSpaceActivity, "Set Space rate!", Toast.LENGTH_SHORT).show()
            } else if (binding.etAddress.text.isEmpty()) {
                Toast.makeText(this@AddSpaceActivity, "Set Space address!", Toast.LENGTH_SHORT).show()
            } else if (binding.etPostcode.text.isEmpty()) {
                Toast.makeText(this@AddSpaceActivity, "Set Space post code!", Toast.LENGTH_SHORT).show()
            } /*else if (binding.tvLocation.text.isEmpty()) {
                Toast.makeText(this@AddSpaceActivity, "Set Space location!", Toast.LENGTH_SHORT).show()
            } */else {
                viewModel.addSpace(
                    MyPreferences.getUserId(this@AddSpaceActivity),
                    binding.etName.text.toString(),
                    binding.etRate.text.toString().toDouble(),
                    binding.etAddress.text.toString(),
                    binding.etPostcode.text.toString(),
                    51.512091,
                    -0.071122
                )
            }
        }

        viewModel.addSpaceResult.observe(this) { result ->
            when (result) {
                is com.keepqueue.sparepark.data.response.Result.Success -> {
                    if (result.data) {
                        Toast.makeText(this@AddSpaceActivity, "Space Added!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                is com.keepqueue.sparepark.data.response.Result.Loading -> {

                }
                is com.keepqueue.sparepark.data.response.Result.Error -> {
                    Toast.makeText(
                        this@AddSpaceActivity,
                        "error:${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(TAG, "error:${result.message}")
                }
            }
        }
    }
}