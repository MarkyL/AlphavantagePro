package com.mark.alphavantage.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mark.alphavantage.R
import com.mark.alphavantage.model.StockModel
import com.mark.alphavantage.network.model.responses.StockDetailsResponse
import com.mark.alphavantage.utils.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.stock_item.view.*

class StocksDetailsAdapter(listener: AdapterListener<StockDetailsResponse>) : BaseAdapter<StockDetailsResponse>(listener) {

    override fun getLayoutId(position: Int, obj: StockDetailsResponse): Int {
        return R.layout.stock_details_item
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return StockDetailsViewHolder(view)
    }
}

class StockDetailsViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer, BaseAdapter.Binder<StockDetailsResponse> {

    override fun bind(data: StockDetailsResponse) {
//        containerView.stockNameTv.text = data.name

    }
}