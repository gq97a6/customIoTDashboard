package com.netDashboard.recycler_view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.netDashboard.R
import com.netDashboard.click
import com.netDashboard.databinding.PopupConfirmBinding
import com.netDashboard.globals.G.theme
import com.netDashboard.iterate

@Suppress("UNUSED")
abstract class RecyclerViewAdapter<item : RecyclerViewItem>(
    var context: Context,
    var spanCount: Int,
    c: DiffUtil.ItemCallback<item>
) : ListAdapter<item, RecyclerViewAdapter.ViewHolder>(c) {

    var editMode = Modes()

    var list: MutableList<item> = mutableListOf()
    lateinit var currentItem: item
    private lateinit var touchHelper: ItemTouchHelper

    var onItemClick: (item) -> Unit = {}
    var onItemLongClick: (item) -> Unit = {}
    var onItemRemoved: (item) -> Unit = {}
    var onItemMarkedRemove: (Int, Boolean) -> Unit = { _, _ -> }
    var onItemEdit: (item) -> Unit = {}

    override fun submitList(list: MutableList<item>?) {
        super.submitList(list)
        this.list = list ?: mutableListOf()
    }

    override fun getCurrentList(): MutableList<item> {
        return list
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return list[position].id
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) { //0 (order of execution)
        super.onAttachedToRecyclerView(recyclerView)

        touchHelper = ItemTouchHelper(ItemTouchCallback(this))
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun getItemViewType(position: Int): Int { //1 (order of execution)
        currentItem = list[position]
        return list[position].getItemViewType(this)
    }

    override fun onCreateViewHolder( //2 (order of execution)
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return currentItem.onCreateViewHolder(parent, viewType)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) { //4 (order of execution)
        super.onViewAttachedToWindow(holder)
        currentItem.onViewAttachedToWindow(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { //3 (order of execution)
        currentItem.onBindViewHolder(holder, position)

        theme.apply(holder.itemView as ViewGroup, anim = false)

        val callback = { v: View ->
            var isLongPressed = false

            v.isClickable = true
            v.setOnTouchListener { v, e ->
                if (editMode.isNone) list[position].onTouch(v, e)

                if (e.action == ACTION_DOWN) {
                    isLongPressed = false
                    if (editMode.isSwap) {
                        list[position].holder?.let {
                            touchHelper.startDrag(it)
                        }
                    } else {
                        val ripple = holder.itemView.findViewById<View>(R.id.ripple_foreground)
                        ripple?.click()
                    }
                }

                if (e.action == ACTION_UP && !isLongPressed) { // onClick
                    onItemClick(list[position])

                    when {
                        editMode.isNone -> list[position].onClick(v, e)
                        editMode.isRemove -> {
                            markItemRemove(position)
                            //removeMarkedItems()
                        }
                        editMode.isEdit -> onItemEdit(list[position])
                    }
                }

                if (e.eventTime - e.downTime > 300 && !isLongPressed) { // onLongClick
                    isLongPressed = true
                    onItemLongClick(list[position])
                }

                return@setOnTouchListener false
            }
        }

        holder.itemView.iterate(callback)
    }

    fun markItemRemove(position: Int) {
        val recyclerView =
            list[position].holder?.itemView?.parent as RecyclerView

        recyclerView.itemAnimator?.changeDuration = 250

        list[position].flag.let {
            if (it.isRemove) it.setNone() else it.setRemove()
            onItemMarkedRemove(list.count { item: item -> item.flag.isRemove }, it.isRemove)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeMarkedItems() {
        if (list.none { item: item -> item.flag.isRemove } || itemCount == 0) return

        val dialog = Dialog(context)

        dialog.setContentView(R.layout.popup_confirm)
        val binding = PopupConfirmBinding.bind(dialog.findViewById(R.id.pc_root))

        binding.pcText.text = "Confirm removing"

        binding.pcConfirm.setOnClickListener {
            var i = 0
            while (i < list.size) {
                list[i].let {
                    if (it.flag.isRemove) {
                        removeItemAt(i, false)
                        onItemRemoved(it)
                        i--
                    }
                }
                i++
            }
            notifyDataSetChanged()
            dialog.dismiss()
        }

        binding.pcDeny.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        val a = dialog.window?.attributes

        a?.dimAmount = 0.9f
        dialog.window?.attributes = a
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        theme.apply(binding.root)
    }

    fun removeItemAt(pos: Int, notify: Boolean = true) {
        val callback = { v: View ->
            v.setOnTouchListener(null)
            v.setOnClickListener(null)
        }

        (list[pos].holder?.itemView as? ViewGroup)?.let {
            it.iterate(callback)
            list.removeAt(pos)

            if (notify) notifyDataSetChanged()
        }
    }

    open inner class Modes {
        private var mode = -1

        var onSetMode: (Modes) -> Unit = {}

        val isNone
            get() = mode == -1
        val isSwap
            get() = mode == 0
        val isRemove
            get() = mode == 1
        val isEdit
            get() = mode == 2

        fun setNone() = setMode(-1)
        fun setSwap() = setMode(0)
        fun setRemove() = setMode(1)
        fun setEdit() = setMode(2)

        private fun setMode(type: Int) {
            mode = type
            onSetMode(this)
            for (e in list) {
                e.flag.setNone()
                e.onEdit(!isNone)
            }
        }
    }
}