package com.keepqueue.sparepark.ui.bookings

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keepqueue.sparepark.R
import com.keepqueue.sparepark.data.model.Booking
import com.keepqueue.sparepark.databinding.ListItemBookingBinding

class BookingsAdapter(private val listener: Listener): RecyclerView.Adapter<BookingsAdapter.BookingViewHolder>() {

    interface Listener {
        fun onCancelClicked(booking: Booking)
        fun onReviewClicked(booking: Booking)
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    var bookings: List<Booking> = emptyList()

    class BookingViewHolder(val binding: ListItemBookingBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    fun setBookingList(bookings: List<Booking>) {
        this.bookings = bookings
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BookingViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = ListItemBookingBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        val holder = BookingViewHolder(binding)
        binding.ibAction.setOnClickListener {
            val booking = bookings[holder.adapterPosition]
            if (booking.status == "requested") {
                listener.onCancelClicked(booking)
            } else {
                listener.onReviewClicked(booking)
            }
        }
        return holder
    }

    override fun onBindViewHolder(viewHolder: BookingViewHolder, position: Int) {

        val booking = bookings[position]
        viewHolder.binding.tvTitle.text = booking.spaceName
        viewHolder.binding.tvTime.text = "${booking.timeFrom}\n${booking.timeTo}"
        viewHolder.binding.tvStatus.text = booking.status.capitalize()

        when (booking.status) {
            "requested" -> {
                viewHolder.binding.tvStatus.setTextColor(Color.BLUE)
                viewHolder.binding.ibAction.setImageResource(R.drawable.close_circle_outline)
                viewHolder.binding.ibAction.visibility = View.VISIBLE
            }
            "accepted" -> {
                viewHolder.binding.tvStatus.setTextColor(Color.parseColor("#339933"))
                viewHolder.binding.ibAction.visibility = View.INVISIBLE
            }
            "rejected" -> {
                viewHolder.binding.tvStatus.setTextColor(Color.RED)
                viewHolder.binding.ibAction.visibility = View.INVISIBLE
            }
            "cancelled" -> {
                viewHolder.binding.tvStatus.setTextColor(Color.LTGRAY)
                viewHolder.binding.ibAction.visibility = View.INVISIBLE
            }
            "completed" -> {
                viewHolder.binding.tvStatus.setTextColor(Color.BLACK)
                viewHolder.binding.ibAction.setImageResource(R.drawable.chatbox_ellipses_outline)
                viewHolder.binding.ibAction.visibility = View.VISIBLE
            }
        }

        var spaceTypeImg = R.drawable.vehicle_car
        when(booking.type) {
            "motor-cycle" -> {
                spaceTypeImg = R.drawable.vehicle_motor_cycle
            }
            "van" -> {
                spaceTypeImg = R.drawable.vehicle_van
            }
            "bus" -> {
                spaceTypeImg = R.drawable.vehicle_bus
            }
            "truck" -> {
                spaceTypeImg = R.drawable.vehicle_truck
            }
            else -> {
                spaceTypeImg = R.drawable.vehicle_car
            }
        }
        viewHolder.binding.ivType.setImageResource(spaceTypeImg)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = bookings.size
}