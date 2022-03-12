package com.michaelgunawan.cryptolist.arch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

interface AdapterItem<VDB : ViewDataBinding> {
    val layoutId: Int
    val id: Long
    fun bindItem(binding: VDB, holder: ViewBindingHolder<VDB>)
    fun onViewHolderCreated(holder: ViewBindingHolder<VDB>) {
        // Do nothing by default
    }
}

class ViewBindingHolder<VDB : ViewDataBinding>(val binding: VDB, val parent: ViewGroup) : RecyclerView.ViewHolder(binding.root)

class ViewBindingRecyclerAdapter : RecyclerView.Adapter<ViewBindingHolder<ViewDataBinding>>() {

    private var items: List<AdapterItem<ViewDataBinding>> = emptyList()
    var _itemActionHandler: MutableMap<KClass<*>, (ViewDataBinding, AdapterItem<ViewDataBinding>) -> Unit> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingHolder<ViewDataBinding> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)
        return ViewBindingHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: ViewBindingHolder<ViewDataBinding>, position: Int) {
        val item = items[position]

        // Bind the item
        item.bindItem(holder.binding, holder)

        // Callback item action handler
        _itemActionHandler[item::class]?.invoke(holder.binding, item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].layoutId
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    fun getItemAt(index: Int): AdapterItem<ViewDataBinding> {
        return items[index]
    }

    fun getItemPosition(item: AdapterItem<*>): Int {
        return items.indexOf(item)
    }

    @Suppress("UNCHECKED_CAST")
    fun updateItems(newItems: List<AdapterItem<*>>) {
        items = newItems as List<AdapterItem<ViewDataBinding>>
        notifyDataSetChanged()
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : AdapterItem<VDB>, VDB : ViewDataBinding> addItemActionHandler(noinline block: (VDB, T) -> Unit) {
        _itemActionHandler[T::class] = block as (ViewDataBinding, AdapterItem<ViewDataBinding>) -> Unit
    }
}