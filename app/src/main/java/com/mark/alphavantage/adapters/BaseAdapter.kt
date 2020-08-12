package com.mark.alphavantage.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mark.alphavantage.utils.ItemAnimation
import timber.log.Timber

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    var items: ArrayList<T>
    var listener: AdapterListener<T>? = null
    var lastPosition = -1
    private var onAttach = true
    private var animationType = ItemAnimation.FADE_IN

    constructor(listItems: ArrayList<T>) {
        this.items = listItems
    }

    constructor(listener: AdapterListener<T>) {
        items = arrayListOf()
        this.listener = listener
    }

    constructor() {
        items = arrayListOf()
    }

    open fun submitList(listItems: List<T>) {
        this.items.clear()
        this.items.addAll(listItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false), viewType)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Binder<T>).bind(items[position])

        listener?.let { listener ->
            holder.itemView.setOnClickListener { listener.onItemClick(items[position]) } }

        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position, items[position])
    }

    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder(view: View, viewType: Int):RecyclerView.ViewHolder

    internal interface Binder<T> {
        fun bind(data: T)
    }

    interface AdapterListener<T> {
        fun onItemClick(data: T)
    }

    private fun setAnimation(view: View, position: Int) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, if (onAttach) position else -1, animationType)
            lastPosition = position
        }
    }

    fun setAnimationType(type: Int) {
        this.animationType = type
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                onAttach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

}