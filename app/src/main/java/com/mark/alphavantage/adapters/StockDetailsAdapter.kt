package com.mark.alphavantage.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mark.alphavantage.R
import com.mark.alphavantage.model.StockModel
import com.mark.alphavantage.network.model.responses.StockData
import com.mark.alphavantage.network.model.responses.StockDetailsResponse
import com.mark.alphavantage.utils.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.stock_data_layout.view.*
import kotlinx.android.synthetic.main.stock_details_item.view.*
import kotlinx.android.synthetic.main.stock_item.view.*

class StocksDetailsAdapter(listener: AdapterListener<StockData>) : BaseAdapter<StockData>(listener) {

    override fun getLayoutId(position: Int, obj: StockData): Int {
        return R.layout.stock_details_item
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return StockDetailsViewHolder(view)
    }
}

class StockDetailsViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer, BaseAdapter.Binder<StockData> {

    override fun bind(data: StockData) {
        with(containerView) {
            timeTv.dataTv.text = data.time
            openTv.dataTv.text = data.open
            closeTv.dataTv.text = data.close
            highTv.dataTv.text = data.high
            lowTv.dataTv.text = data.low
            volumeTv.dataTv.text = data.volume
        }

    }
}