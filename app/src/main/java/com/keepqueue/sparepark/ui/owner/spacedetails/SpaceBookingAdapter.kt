package com.keepqueue.sparepark.ui.owner.spacedetails

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keepqueue.sparepark.R
import com.keepqueue.sparepark.data.model.Booking
import com.keepqueue.sparepark.databinding.ListItemSpaceBookingBinding

class SpaceBookingAdapter(private val listener: Listener): RecyclerView.Adapter<SpaceBookingAdapter.BookingViewHolder>() {

    interface Listener {
        fun onAcceptClicked(booking: Booking)
        fun onRejectClicked(booking: Booking)
        fun onCompleteClicked(booking: Booking)
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    var bookings: List<Booking> = emptyList()

    class BookingViewHolder(val binding: ListItemSpaceBookingBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    fun setBookingList(bookings: List<Booking>) {
        this.bookings = bookings
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BookingViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = ListItemSpaceBookingBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        val holder = BookingViewHolder(binding)
        binding.ibAction.setOnClickListener {
            val booking = bookings[holder.adapterPosition]
            if (booking.status == "requested") {
                listener.onAcceptClicked(booking)
            } else if (booking.status == "accepted") {
                listener.onCompleteClicked(booking)
            }
        }
        binding.ibActionReject.setOnClickListener {
            val booking = bookings[holder.adapterPosition]
            listener.onRejectClicked(booking)
        }
        return holder
    }

    override fun onBindViewHolder(viewHolder: BookingViewHolder, position: Int) {

        val booking = bookings[position]
        viewHolder.binding.tvName.text = booking.userName
        viewHolder.binding.tvTime.text = "${booking.timeFrom}\n${booking.timeTo}"
        viewHolder.binding.tvStatus.text = booking.status.capitalize()

        when (booking.status) {
            "requested" -> {
                viewHolder.binding.tvStatus.setTextColor(Color.BLUE)
                viewHolder.binding.ibAction.setImageResource(R.drawable.checkmark_circle_outline)
                viewHolder.binding.ibActionReject.setImageResource(R.drawable.close_circle_outline)
                viewHolder.binding.ibAction.visibility = View.VISIBLE
                viewHolder.binding.ibActionReject.visibility = View.VISIBLE
            }
            "accepted" -> {
                viewHolder.binding.tvStatus.setTextColor(Color.parseColor("#339933"))
                viewHolder.binding.ibAction.setImageResource(R.drawable.checkmark_done_circle_outline)
                viewHolder.binding.ibAction.visibility = View.VISIBLE
                viewHolder.binding.ibActionReject.visibility = View.INVISIBLE
            }
            "rejected" -> {
                viewHolder.binding.tvStatus.setTextColor(Color.RED)
                viewHolder.binding.ibAction.visibility = View.INVISIBLE
                viewHolder.binding.ibActionReject.visibility = View.INVISIBLE
            }
            "cancelled" -> {
                viewHolder.binding.tvStatus.setTextColor(Color.LTGRAY)
                viewHolder.binding.ibAction.visibility = View.INVISIBLE
                viewHolder.binding.ibActionReject.visibility = View.INVISIBLE
            }
            "completed" -> {
                viewHolder.binding.tvStatus.setTextColor(Color.BLACK)
                viewHolder.binding.ibAction.visibility = View.INVISIBLE
                viewHolder.binding.ibActionReject.visibility = View.INVISIBLE
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = bookings.size
}