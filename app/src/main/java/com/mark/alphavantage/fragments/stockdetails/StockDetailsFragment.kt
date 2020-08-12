package com.mark.alphavantage.fragments.stockdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mark.alphavantage.R
import com.mark.alphavantage.adapters.BaseAdapter
import com.mark.alphavantage.adapters.StocksDetailsAdapter
import com.mark.alphavantage.fragments.main.*
import com.mark.alphavantage.mvvm.State
import com.mark.alphavantage.network.model.responses.StockDetailsResponse
import com.mark.alphavantage.utils.DateTimeHelper
import com.mark.alphavantage.utils.Event
import com.mark.alphavantage.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class StockDetailsFragment : Fragment(), BaseAdapter.AdapterListener<StockDetailsResponse> {

    private val viewModel by viewModel<StockDetailsViewModel>()

    private val stockDetailsAdapter = StocksDetailsAdapter(this)

    private lateinit var symbol: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stock_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel()

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(GridSpacingItemDecoration(2, 30, true))
//            this.adapter = stocksAdapter
        }

        arguments?.let {
            symbol = it.getString(SYMBOL, "")
            viewModel.dispatchInputEvent(GetStockDetails(symbol))
        }
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

    private fun handleNext(data: Event<StockDetailsDataState>?) {
        data?.let { responseEvent ->
            if (!responseEvent.consumed) {
                responseEvent.consume()?.let { response ->
                    when (response) {
                        is GetStockDetailsSuccess -> handleGetStockDetailsSuccess(response.stockDetailsResponse)
                    }
                }
            }
        }
    }

    private fun handleGetStockDetailsSuccess(stockDetailsResponse: StockDetailsResponse) {
        Timber.i("handleGetStockDetailsSuccess with $stockDetailsResponse")

    }

    private fun handleError(it: Throwable) {
    }

    private fun showProgressView() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressView() {
        progressBar.visibility = View.GONE
    }

    companion object {
        private const val SYMBOL = "symbol"

        fun newInstance(symbol: String): StockDetailsFragment {
            val fragment = StockDetailsFragment()
            val args = Bundle()
            args.putString(SYMBOL, symbol)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(data: StockDetailsResponse) {
        TODO("Not yet implemented")
    }
}