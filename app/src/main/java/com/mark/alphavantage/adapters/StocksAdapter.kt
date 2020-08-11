package com.mark.alphavantage.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mark.alphavantage.R
import com.mark.alphavantage.model.StockModel
import com.mark.alphavantage.utils.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.stock_item.view.*

class StocksAdapter : BaseAdapter<StockModel>() {

    override fun getLayoutId(position: Int, obj: StockModel): Int {
        return R.layout.stock_item
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return StockViewHolder(view)
    }
}

class StockViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer, BaseAdapter.Binder<StockModel> {

    override fun bind(data: StockModel) {
        containerView.stockNameTv.text = data.name
        containerView.stockTickerTv.text = data.stk

        GlideApp.with(containerView)
            .load(data.img)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.ic_launcher_background)
            .into(containerView.stockIV)
    }
}