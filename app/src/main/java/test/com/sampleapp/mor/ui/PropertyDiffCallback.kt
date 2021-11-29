package test.com.sampleapp.mor.ui

import androidx.recyclerview.widget.DiffUtil
import test.com.sampleapp.mor.data.cache.relations.PropertyAndTenant

object PropertyDiffCallback : DiffUtil.ItemCallback<PropertyAndTenant>() {
    override fun areItemsTheSame(oldItem: PropertyAndTenant, newItem: PropertyAndTenant): Boolean =
        oldItem.property.id == newItem.property.id

    override fun areContentsTheSame(
        oldItem: PropertyAndTenant,
        newItem: PropertyAndTenant
    ): Boolean =
        oldItem == newItem
}