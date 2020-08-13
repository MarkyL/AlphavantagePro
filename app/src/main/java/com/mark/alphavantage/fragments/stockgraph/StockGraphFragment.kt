package com.mark.alphavantage.fragments.stockgraph

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.mark.alphavantage.R
import com.mark.alphavantage.network.model.responses.StockData
import kotlinx.android.synthetic.main.fragment_stock_graph.*
import timber.log.Timber

class StockGraphFragment : Fragment() {

    private lateinit var symbol: String
    private lateinit var stockList: ArrayList<StockData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stock_graph, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_arrow_back)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        arguments?.let {
            symbol = it.getString(SYMBOL, "")
            toolbar.title = symbol

            stockList = it.getSerializable(STOCK_LIST) as ArrayList<StockData>
            Timber.i("stockList size = ${stockList.size}")
            initCandleChart()
            applyDataToChart(40)

            // Do this for Parcelization reasons of the library that uses the graph.
            it.remove(STOCK_LIST)
        }

        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress < MIN_PROGRESS)
                    applyDataToChart(MIN_PROGRESS)
                else
                    applyDataToChart(progress)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun initCandleChart() {
        candleStickChart.setMaxVisibleValueCount(60)
        // scaling can now only be done on x- and y-axis separately
        candleStickChart.setPinchZoom(false)
        candleStickChart.setDrawGridBackground(false)
    }

    private fun applyDataToChart(progress: Int) {
        seekBarProgressTv.text = progress.toString()
        val candleEntries = ArrayList<CandleEntry>()

        for (index in 0 until progress) {
            val stockData = stockList[index]
            candleEntries.add(CandleEntry(
                index.toFloat(),
                stockData.high.toFloat(), stockData.low.toFloat(),
                stockData.open.toFloat(), stockData.close.toFloat()))
        }

        val candleDataSet = CandleDataSet(candleEntries, "Data set")
        
        ////
        applyAttrsToCandleDataSet(candleDataSet)
        ////
        
        val candleData = CandleData(candleDataSet)
        candleStickChart.data = candleData
        candleStickChart.invalidate()
        candleData.dataSets
    }

    private fun applyAttrsToCandleDataSet(candleDataSet: CandleDataSet) {
        candleDataSet.setDrawIcons(false)
        candleDataSet.axisDependency = AxisDependency.LEFT
        candleDataSet.shadowColor = Color.DKGRAY
        candleDataSet.shadowWidth = 0.7f
        candleDataSet.decreasingColor = Color.RED
        candleDataSet.decreasingPaintStyle = Paint.Style.FILL
        candleDataSet.increasingColor = Color.rgb(122, 242, 84)
        candleDataSet.increasingPaintStyle = Paint.Style.STROKE
        candleDataSet.neutralColor = Color.BLUE
    }

    companion object {
        private const val MIN_PROGRESS = 30
        private const val SYMBOL = "symbol"
        private const val STOCK_LIST = "stock_list"

        fun newInstance(symbol: String, stockList: ArrayList<StockData>): StockGraphFragment {
            val fragment = StockGraphFragment()
            val args = Bundle()
            args.putString(SYMBOL, symbol)
            args.putSerializable(STOCK_LIST, stockList)
            fragment.arguments = args
            return fragment
        }
    }
}