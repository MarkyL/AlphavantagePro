package com.mark.alphavantage.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mark.alphavantage.R
import com.mark.alphavantage.activities.FragmentNavigator
import com.mark.alphavantage.adapters.BaseAdapter
import com.mark.alphavantage.adapters.StocksAdapter
import com.mark.alphavantage.fragments.stockdetails.StockDetailsFragment
import com.mark.alphavantage.model.StockModel
import com.mark.alphavantage.mvvm.State
import com.mark.alphavantage.utils.Event
import com.mark.alphavantage.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainFragment : Fragment(), BaseAdapter.AdapterListener<StockModel> {

    private val viewModel by viewModel<MainViewModel>()

    private val stocksAdapter = StocksAdapter(listener = this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "Alphavantage - Mark"

        registerViewModel()

        recyclerView.apply {
            addItemDecoration(GridSpacingItemDecoration(2, 30, true))
            this.adapter = stocksAdapter
        }

        viewModel.dispatchInputEvent(GetStocks)
    }

    private fun registerViewModel() {
        viewModel.dataStream.observe(
            viewLifecycleOwner,
            Observer { t ->
                when (t.state) {
                    State.INIT -> { }
                    State.LOADING -> { showProgressView() }
                    State.NEXT -> {
                        hideProgressView()
                        handleNext(t.data)
                    }
                    State.ERROR -> { t.throwable?.let { handleError(it) } }
                    State.COMPLETE -> { hideProgressView() }
                }
            })
    }

    private fun handleNext(data: Event<MainDataState>?) {
        data?.let { responseEvent ->
            if (!responseEvent.consumed) {
                responseEvent.consume()?.let { response ->
                    when (response) {
                        is GetStocksSuccess -> handleGetStocksSuccess(response.stocksList)
                        is GetStocksFailureState -> TODO()
                    }
                }
            }
        }
    }

    private fun handleError(it: Throwable) {
    }

    private fun handleGetStocksSuccess(stocksList: List<StockModel>) {
        Timber.i("handleGetStocksSuccess stocksList - $stocksList")
        stocksAdapter.submitList(stocksList)
    }

    private fun showProgressView() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressView() {
        progressBar.visibility = View.GONE
    }

    override fun onItemClick(data: StockModel) {
        Timber.i("onItemClick - $data")
        (activity as FragmentNavigator).navigateToStockDetails(data.stk)
    }


}