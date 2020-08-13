package com.mark.alphavantage.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mark.alphavantage.R
import com.mark.alphavantage.fragments.main.MainFragment
import com.mark.alphavantage.fragments.stockdetails.StockDetailsFragment
import com.mark.alphavantage.fragments.stockgraph.StockGraphFragment
import com.mark.alphavantage.network.model.responses.StockData


class MainActivity : AppCompatActivity(), FragmentNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startMainFragment()
    }

    private fun startMainFragment() {
        val fragment: Fragment = MainFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, fragment::class.java.simpleName)
            .commit()
    }

    override fun navigateToStockDetails(symbol: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, StockDetailsFragment.newInstance(symbol))
            .addToBackStack(null)
            .commit()
    }

    override fun navigateToStockGraph(
        symbol: String,
        stockList: ArrayList<StockData>
    ) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, StockGraphFragment.newInstance(symbol, stockList))
            .addToBackStack(null)
            .commit()
    }

}

interface FragmentNavigator {
    fun navigateToStockDetails(symbol: String)
    fun navigateToStockGraph(
        symbol: String,
        stockList: ArrayList<StockData>
    )
}