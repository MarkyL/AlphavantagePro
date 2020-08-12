package com.mark.alphavantage.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mark.alphavantage.R
import kotlinx.android.synthetic.main.stock_data_layout.view.*

class StockDataView : ConstraintLayout {

    constructor(context: Context) : super(context) { initialize(context) }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context)
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context)
        initAttrs(attrs)
    }

    private fun initialize(context: Context) {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.stock_data_layout, this, true)
    }

    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StockDataView)

        titleTv.text = typedArray.getString(R.styleable.StockDataView_title_text)
        dataTv.text = typedArray.getString(R.styleable.StockDataView_data_text)

        typedArray.recycle()
    }

}