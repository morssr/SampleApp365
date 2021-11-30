package test.com.sampleapp.mor.ui.utils.paging

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import test.com.sampleapp.mor.R
import test.com.sampleapp.mor.data.cache.entities.status.OccupationStatus
import test.com.sampleapp.mor.data.cache.entities.status.OwnerStatus
import test.com.sampleapp.mor.data.cache.entities.status.TenantStatus

@BindingAdapter("app:textColorByOccupationStatus")
fun TextView.bindStatusColor(
    status: OccupationStatus
) {
    val textColor = when (status) {
        OccupationStatus.OCCUPIED -> ContextCompat.getColor(context, R.color.active_occupied)
        OccupationStatus.VACANT -> ContextCompat.getColor(context, R.color.vacant)
        OccupationStatus.INACTIVE -> ContextCompat.getColor(context, R.color.inactive)
        else -> ContextCompat.getColor(context, R.color.black_900_alpha_087)
    }

    setTextColor(textColor)
}

@BindingAdapter("app:textColorByOwnerStatus")
fun TextView.bindOwnerStatusColor(
    status: OwnerStatus
) {
    val textColor = when (status) {
        OwnerStatus.ACTIVE -> ContextCompat.getColor(context, R.color.active_occupied)
        OwnerStatus.INACTIVE -> ContextCompat.getColor(context, R.color.inactive)
        else -> ContextCompat.getColor(context, R.color.black_900_alpha_087)
    }

    setTextColor(textColor)
}

@BindingAdapter("app:textColorByTenantStatus")
fun TextView.bindTenantStatusColor(
    status: TenantStatus
) {
    val textColor = when (status) {
        TenantStatus.ACTIVE -> ContextCompat.getColor(context, R.color.active_occupied)
        TenantStatus.INACTIVE -> ContextCompat.getColor(context, R.color.inactive)
        else -> ContextCompat.getColor(context, R.color.black_900_alpha_087)
    }

    setTextColor(textColor)
}