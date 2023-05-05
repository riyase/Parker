package com.keepqueue.sparepark.ui.home

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.keepqueue.sparepark.data.model.Space
import com.keepqueue.sparepark.data.response.Result
import com.keepqueue.sparepark.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val itemClickListener = object: HomeAdapter.Listener {

        override fun onSpaceClick(space: Space) {
            val intent = Intent(activity, BookSpaceActivity::class.java)
            intent.putExtra(BookSpaceActivity.SPACE, space)
            intent.putExtra(BookSpaceActivity.START_TIME, binding.etStartTime.text.toString())
            intent.putExtra(BookSpaceActivity.END_TIME, binding.etEndTime.text.toString())
            startActivity(intent)
        }

    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var startDate: String? = null
    private var startTime: String? = null
    private var endDate: String? = null
    private var endTime: String? = null

    private lateinit var viewModel: HomeViewModel
    lateinit var loading: ProgressDialog

    private val homeAdapter = HomeAdapter(itemClickListener)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        loading = ProgressDialog(requireContext())
        loading.setMessage("Wait...")

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.bStart.setOnClickListener {
            //Get today's date and set it to the date picker!
            val calendar = Calendar.getInstance()
            val yearToday = calendar.get(Calendar.YEAR)
            val monthToday = calendar.get(Calendar.MONTH)
            val dayToday = calendar.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog(requireContext(), object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
                    startDate = "$year-${String.format("%02d", month)}-${String.format("%02d", day)}"
                    val calendar = Calendar.getInstance()
                    val hourNow = calendar.get(Calendar.HOUR_OF_DAY)
                    val minuteNow = calendar.get(Calendar.MINUTE)
                    TimePickerDialog(activity, object: TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
                            startTime = "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
                            if (!startDate.isNullOrBlank() && !startTime.isNullOrBlank()) {
                                binding.etStartTime.setText("$startDate $startTime")
                            }
                        }

                    }, hourNow, minuteNow, DateFormat.is24HourFormat(activity)).show()
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
            DatePickerDialog(requireContext(), object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
                    endDate = "$year-${String.format("%02d", month)}-${String.format("%02d", day)}"
                    val calendar = Calendar.getInstance()
                    val hourNow = calendar.get(Calendar.HOUR_OF_DAY)
                    val minuteNow = calendar.get(Calendar.MINUTE)
                    TimePickerDialog(activity, object: TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
                            endTime = "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
                            if (!endDate.isNullOrBlank() && !endTime.isNullOrBlank()) {
                                binding.etEndTime.setText("$endDate $endTime")
                            }
                        }

                    }, hourNow, minuteNow, DateFormat.is24HourFormat(activity)).show()
                }
            }, yearToday, monthToday, dayToday).show()
        }
        binding.bSearch.setOnClickListener {
            viewModel.searchSpaces(binding.etSearch.text.toString(),
                binding.etStartTime.text.toString(), binding.etEndTime.text.toString())
        }

        binding.rvSpaces.adapter = homeAdapter
        binding.rvSpaces.layoutManager = LinearLayoutManager(activity)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.searchResult.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Success -> {
                    homeAdapter.setSpaceList(result.data)
                    loading.dismiss()
                }
                is Result.Error -> {
                    loading.dismiss()
                    result.message?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> loading.show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        /*activity?.let {
            if (MyPreferences.isLoggedIn(it)) {
                binding.bLogin.text = "Logout"
            } else {
                binding.bLogin.text = "Login"
            }
        } ?: {
            binding.bLogin.text = "Error"
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}