package test.com.sampleapp.mor.ui

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import test.com.sampleapp.mor.data.cache.relations.PropertyAndTenant
import test.com.sampleapp.mor.databinding.PropertyListItemBinding

private const val TAG = "PropertiesViewHolder"

class PropertiesViewHolder(
    private val binding: PropertyListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        propertyAndTenant: PropertyAndTenant,
        listener: PropertiesAdapter.PropertiesAdapterListener
    ) {
        Log.d(TAG, "bind: $propertyAndTenant")

        binding.property = propertyAndTenant
        binding.tenantInclude.tenant = propertyAndTenant.tenant
    }
}