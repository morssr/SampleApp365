package test.com.sampleapp.mor.ui.properties

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_properties, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLoadStateAdapter()
        setupListLayoutManager()
        loadProperties(PropertyStatusFilter.OCCUPIED)
    }

    private fun loadProperties(
        propertyStatusFilter: PropertyStatusFilter = PropertyStatusFilter.ACTIVE,
        tenantStatusFilter: TenantStatusFilter = TenantStatusFilter.ANY
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


    fun loadAllProperties() {
        lifecycleScope.launchWhenResumed {
            propertiesViewModel.getPropertiesByStatusFlow().collect { list ->
                list.forEach {
                    Log.d(TAG, "loadAllProperties: $it")
                }
            }
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

    override fun onPropertyClick(property: PropertyAndTenant, position: Int) {
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
}