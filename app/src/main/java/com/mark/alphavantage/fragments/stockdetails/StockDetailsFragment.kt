package com.mark.alphavantage.fragments.stockdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mark.alphavantage.R
import com.mark.alphavantage.activities.FragmentNavigator
import com.mark.alphavantage.adapters.BaseAdapter
import com.mark.alphavantage.adapters.StocksDetailsAdapter
import com.mark.alphavantage.mvvm.State
import com.mark.alphavantage.network.model.responses.StockData
import com.mark.alphavantage.network.model.responses.StockDetailsResponse
import com.mark.alphavantage.utils.Event
import com.mark.alphavantage.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.progressBar
import kotlinx.android.synthetic.main.fragment_main.recyclerView
import kotlinx.android.synthetic.main.fragment_main.toolbar
import kotlinx.android.synthetic.main.fragment_stock_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StockDetailsFragment : Fragment(), BaseAdapter.AdapterListener<StockData> {

    private val viewModel by viewModel<StockDetailsViewModel>()

    private val stockDetailsAdapter = StocksDetailsAdapter(listener = this)

    private lateinit var symbol: String
    private var selectedTimeInterval = StockDetailsResponse.TimeInterval.ONE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stock_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(GridSpacingItemDecoration(1, 30, true))
            this.adapter = stockDetailsAdapter
        }

        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_arrow_back)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        arguments?.let {
            symbol = it.getString(SYMBOL, "")
            toolbar.title = symbol
            viewModel.dispatchInputEvent(GetStockDetails(symbol, selectedTimeInterval))
        }

        setSpinnerData()
    }

    private fun setSpinnerData() {
        var intervalsList = mutableListOf<String>()
        StockDetailsResponse.TimeInterval.values().forEach {
            timeInterval -> intervalsList.add(timeInterval.typeName)
        }

        var arrayAdapter =
            object : ArrayAdapter<String>(requireContext(), R.layout.spinner_item, intervalsList) {
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    return view
                }
            }

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(timeIntervalSelector) {
            adapter = arrayAdapter

            onItemSelectedListener = object : AdapterView.OnItemClickListener,
                AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) { Timber.i("onNothingSelected") }
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val newSelectedTimeInterval = StockDetailsResponse.TimeInterval.values()[position]
                    Timber.i("onItemSelected - $newSelectedTimeInterval")
                    if (newSelectedTimeInterval != selectedTimeInterval) {
                        selectedTimeInterval = newSelectedTimeInterval
                        viewModel.dispatchInputEvent(GetStockDetails(symbol, selectedTimeInterval))
                    }
                }
            }
        }
    }


    private fun registerViewModel() {
        viewModel.dataStream.observe(
            viewLifecycleOwner,
            Observer { t ->
                when (t.state) {
                    State.INIT -> {
                    }
                    State.LOADING -> {
                        showProgressView()
                    }
                    State.NEXT -> {
                        hideProgressView()
                        handleNext(t.data)
                    }
                    State.ERROR -> {
                        t.throwable?.let { handleError(it) }
                    }
                    State.COMPLETE -> {
                        hideProgressView()
                    }
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
        stockDetailsResponse.stockTimeSeries.stockList.sortedWith(
            compareBy { it.timeStamp }
        )
        stockDetailsAdapter.submitList(stockDetailsResponse.stockTimeSeries.stockList)

        graphBtn.setOnClickListener {
            (activity as FragmentNavigator).navigateToStockGraph(
                stockDetailsResponse.metaData.symbol,
                stockDetailsResponse.stockTimeSeries.stockList
            )
        }
        graphBtn.isEnabled = true
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

    override fun onItemClick(data: StockData) {

    }

}