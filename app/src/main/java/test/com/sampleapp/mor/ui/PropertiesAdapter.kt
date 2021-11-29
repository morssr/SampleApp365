package test.com.sampleapp.mor.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import test.com.sampleapp.mor.data.cache.relations.PropertyAndTenant
import test.com.sampleapp.mor.databinding.PropertyListItemBinding

private const val TAG = "PropertiesAdapter"

class PropertiesAdapter(private val listener: PropertiesAdapterListener) :
    PagingDataAdapter<PropertyAndTenant, PropertiesViewHolder>(PropertyDiffCallback) {

    interface PropertiesAdapterListener {
        fun onPropertyClick(property: PropertyAndTenant, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertiesViewHolder {
        return PropertiesViewHolder(
            PropertyListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PropertiesViewHolder, position: Int) {
        holder.bind(getItem(position) as PropertyAndTenant, listener)
    }

    override fun getItemViewType(position: Int): Int = position
}