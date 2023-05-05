package com.keepqueue.sparepark.ui.owner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keepqueue.sparepark.R
import com.keepqueue.sparepark.data.model.Space
import com.keepqueue.sparepark.databinding.ListItemSearchSpaceBinding

class OwnerAdapter(private val listener: Listener): RecyclerView.Adapter<OwnerAdapter.SpaceViewHolder>() {

    interface Listener {
        fun onSpaceClick(space: Space);
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    var spaces: List<Space> = emptyList()
    class SpaceViewHolder(val binding: ListItemSearchSpaceBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    fun setSpaceList(spaces: List<Space>) {
        this.spaces = spaces
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SpaceViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = ListItemSearchSpaceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        val holder = SpaceViewHolder(binding)
        binding.root.setOnClickListener {
            listener.onSpaceClick(spaces[holder.adapterPosition])
        }
        return holder
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: SpaceViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val space = spaces[position]
        viewHolder.binding.tvSpaceName.text = space.name
        viewHolder.binding.tvSpaceRate.text = "£${space.hourRate}/HR"

        var spaceTypeImg = R.drawable.vehicle_car
        when(space.type) {
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
        viewHolder.binding.ivSpaceType.setImageResource(spaceTypeImg)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = spaces.size
}