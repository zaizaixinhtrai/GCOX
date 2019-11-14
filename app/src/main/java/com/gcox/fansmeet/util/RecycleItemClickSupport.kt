package com.gcox.fansmeet.util

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gcox.fansmeet.R

import timber.log.Timber


/**
 * Created by sonnguyen on 9/11/16.
 */
class RecycleItemClickSupport private constructor(private val mRecyclerView: RecyclerView) {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private val mOnClickListener = View.OnClickListener { v ->
        if (mOnItemClickListener != null) {
            val holder = mRecyclerView.getChildViewHolder(v)
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                mOnItemClickListener!!.onItemClicked(mRecyclerView, position, v)
            }
        }
    }
    private val mOnLongClickListener = View.OnLongClickListener { v ->
        if (mOnItemLongClickListener != null) {
            val holder = mRecyclerView.getChildViewHolder(v)
            return@OnLongClickListener mOnItemLongClickListener!!.onItemLongClicked(
                mRecyclerView,
                holder.adapterPosition,
                v
            )
        }
        false
    }
    private val mAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {

            if (mOnItemClickListener != null) {
                view.setOnClickListener(mOnClickListener)
            }
            if (mOnItemLongClickListener != null) {
                view.setOnLongClickListener(mOnLongClickListener)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {

        }
    }

    init {
        mRecyclerView.setTag(R.id.item_click_support, this)
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener)
    }

    fun setOnItemClickListener(listener: OnItemClickListener): RecycleItemClickSupport {
        mOnItemClickListener = listener
        return this
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener): RecycleItemClickSupport {
        mOnItemLongClickListener = listener
        return this
    }

    private fun detach(view: RecyclerView) {
        view.removeOnChildAttachStateChangeListener(mAttachListener)
        view.setTag(R.id.item_click_support, null)
    }

    interface OnItemClickListener {

        fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View)
    }

    interface OnItemLongClickListener {

        fun onItemLongClicked(recyclerView: RecyclerView, position: Int, v: View): Boolean
    }

    companion object {

        fun addTo(view: RecyclerView): RecycleItemClickSupport {
            var support: RecycleItemClickSupport? =
                view.getTag(R.id.item_click_support) as RecycleItemClickSupport
            if (support == null) {
                support = RecycleItemClickSupport(view)
            }
            return support
        }

        fun removeFrom(view: RecyclerView): RecycleItemClickSupport? {
            val support = view.getTag(R.id.item_click_support) as RecycleItemClickSupport
            support.detach(view)
            return support
        }
    }
}
