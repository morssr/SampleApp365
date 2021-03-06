package test.com.sampleapp.mor.ui.properties

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import test.com.sampleapp.mor.R
import test.com.sampleapp.mor.data.cache.relations.PropertyAndTenant
import test.com.sampleapp.mor.databinding.FragmentPropertiesBinding
import test.com.sampleapp.mor.ui.utils.paging.LoadStateAdapter

private const val TAG = "PropertiesFragment"

@AndroidEntryPoint
class PropertiesFragment : Fragment(), PropertiesAdapter.PropertiesAdapterListener {

    private val propertiesViewModel: PropertiesViewModel by viewModels()
    private lateinit var binding: FragmentPropertiesBinding

    private val adapter = PropertiesAdapter(this)

    private var loadJob: Job? = null
    private var eventsDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.fragment_properties, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLoadStateAdapter()
        setupListLayoutManager()
        loadPropertiesByFilters(PropertyStatusFilter.ACTIVE)

        setupPropertyFilterDropdown()
        setupTenantFilterDropdown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissDialog(eventsDialog)
    }

    private fun loadPropertiesByFilters(
        propertyStatusFilter: PropertyStatusFilter = propertiesViewModel.currentPropertyStatusFilter,
        tenantStatusFilter: TenantStatusFilter = propertiesViewModel.currentTenantStatusFilter
    ) {
        Log.i(
            TAG,
            "loadProperties: start remote load properties with filters: $propertyStatusFilter | $tenantStatusFilter"
        )

        cancelLoadJob()
        loadJob = viewLifecycleOwner.lifecycleScope.launch {
            propertiesViewModel.getPropertiesAndTenantsByStatusPaging(
                propertyStatusFilter,
                tenantStatusFilter
            )
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    adapter.submitData(it)
                }
        }
    }

    override fun onPropertyClick(property: PropertyAndTenant) {
        property.property.run {
            eventsDialog = showPropertyEventsDialog(address, occupationStatus.toString())
        }
    }

    override fun onOwnerClick(property: PropertyAndTenant) {
        property.property.run {
            eventsDialog = showPropertyEventsDialog(owner, ownerStatus.toString())
        }
    }

    override fun onTenantClick(property: PropertyAndTenant) {
        property.tenant?.run {
            eventsDialog = showPropertyEventsDialog("$firstName $lastName", tenantStatus.toString())
        }
    }

    private fun cancelLoadJob() {
        loadJob?.cancel()
    }

    private fun setupLoadStateAdapter() {
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter { adapter.retry() },
            footer = LoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            // show empty lis
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            // Only show the list if refresh succeeds.
            binding.list.isVisible = loadState.mediator?.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.mediator?.refresh is LoadState.Error
            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupListLayoutManager() {
        val manager = LinearLayoutManager(requireContext())

        binding.list.run {
            layoutManager = manager
            itemAnimator = null
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            binding.emptyList.visibility = View.VISIBLE
            binding.list.visibility = View.GONE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.list.visibility = View.VISIBLE
        }
    }

    private fun setupPropertyFilterDropdown() {
        val filters = resources.getStringArray(R.array.properties_filters)
        val adapter =
            ArrayAdapter(requireContext(), R.layout.filter_dropdown_list_item, filters)
        (binding.toolbarFilters.propertiesDropdownEt as? AutoCompleteTextView)?.let {
            it.setAdapter(adapter)
            it.setOnItemClickListener { _, _, i, _ ->
                val filter =
                    PropertyStatusFilter.valueOf(resources.getStringArray(R.array.properties_filters)[i].uppercase())

                it.clearFocus()

                loadPropertiesByFilters(propertyStatusFilter = filter)
            }
        }
    }

    private fun setupTenantFilterDropdown() {
        val filters = resources.getStringArray(R.array.tenant_filters)
        val adapter = ArrayAdapter(requireContext(), R.layout.filter_dropdown_list_item, filters)
        (binding.toolbarFilters.tenantsDropdownEt as? AutoCompleteTextView)?.let {
            it.setAdapter(adapter)
            it.setOnItemClickListener { _, _, i, _ ->
                val filter =
                    TenantStatusFilter.valueOf(resources.getStringArray(R.array.tenant_filters)[i].uppercase())

                it.clearFocus()

                loadPropertiesByFilters(tenantStatusFilter = filter)
            }
        }
    }

    private fun showPropertyEventsDialog(title: String, content: String) : AlertDialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton(resources.getString(android.R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun dismissDialog(dialog: AlertDialog?) {
        dialog?.let {
            if (dialog.isShowing)
                dialog.dismiss()
        }
    }
}