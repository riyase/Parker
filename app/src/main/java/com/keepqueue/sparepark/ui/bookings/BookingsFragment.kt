package com.keepqueue.sparepark.ui.bookings


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.keepqueue.sparepark.R

import com.keepqueue.sparepark.data.Prefs
import com.keepqueue.sparepark.data.model.Booking
import com.keepqueue.sparepark.data.response.Result
import com.keepqueue.sparepark.databinding.FragmentBookingsBinding


class BookingsFragment : Fragment() {

    private var _binding: FragmentBookingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: BookingsViewModel
    private val itemClickListener = object: BookingsAdapter.Listener {
        override fun onCancelClicked(booking: Booking) {
            val adb: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            //adb.setView(R.layout.dialog_rating)
            adb.setTitle("Cancel")
            adb.setMessage("Are you sure to cancel booking?")

            adb.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                //Toast.makeText(this@MainActivity, "Ok clicked!", Toast.LENGTH_SHORT).show()

                viewModel.cancelBooking(booking.id)
            })
            adb.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, which -> {

                } })

            adb.show()
        }

        override fun onReviewClicked(booking: Booking) {
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_rating)
            val ratingBar = dialog.findViewById(R.id.ratingBar) as RatingBar
            val etReview = dialog.findViewById(R.id.etReview) as EditText

            val bOkay: Button = dialog.findViewById(R.id.bOkay) as Button
            val bCancel: Button = dialog.findViewById(R.id.bCancel) as Button
            bOkay.setOnClickListener(View.OnClickListener {
                if (ratingBar.numStars > 0 && etReview.text.length > 0) {
                    viewModel.submitReview(booking.id, ratingBar.numStars, etReview.text.toString())
                }
            })
            bCancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })

            dialog.show()
            //Toast.makeText(requireContext(), "onReviewClicked!", Toast.LENGTH_SHORT).show()
        }


    }
    private val adapter = BookingsAdapter(itemClickListener)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(BookingsViewModel::class.java)

        _binding = FragmentBookingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvBookings.adapter = adapter
        binding.rvBookings.layoutManager = LinearLayoutManager(activity)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getMyBookings(Prefs.getUserId(requireActivity()))
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMyBookings(Prefs.getUserId(requireActivity()))
        viewModel.getBookingsResult.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Success -> {
                    adapter.setBookingList(result.data)
                    binding.swipeRefresh.isRefreshing = false
                }
                is Result.Error -> {
                    binding.swipeRefresh.isRefreshing = false
                    result.message?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    binding.swipeRefresh.isRefreshing = true
                }
            }
        }
        viewModel.submitReviewResult.observe(viewLifecycleOwner) {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}