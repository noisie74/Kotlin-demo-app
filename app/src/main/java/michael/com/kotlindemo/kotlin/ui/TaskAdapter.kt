package michael.com.kotlindemo.kotlin.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.task_item.view.*
import michael.com.kotlindemo.R
import michael.com.kotlindemo.kotlin.model.Task

/**
 * Created by Mikhail on 5/31/17.
 */

class TaskAdapter(tasks: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    var mTasks: MutableList<Task>? = null
    var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        this.clickListener = clickListener
    }

    init {
        setList(tasks)
    }

    fun replaceData(tasks: MutableList<Task>) {
        setList(tasks)
        notifyDataSetChanged()
    }


    private fun setList(tasks: MutableList<Task>) {
        mTasks = requireNotNull(tasks)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val task: Task = mTasks!![position]
        bindData(holder, task)

        holder?.itemView?.setOnClickListener { v ->
            clickListener?.onItemClick(v, holder.layoutPosition)
        }

    }

    fun bindData(holder: ViewHolder?, task: Task) {
        var taskCompleted: Boolean = task.isCompleted!!

        holder?.itemView?.task_title!!.text = task.taskTitle
        holder.itemView?.complete!!.isChecked = task.isCompleted!!

        if (taskCompleted) holder.itemView.complete!!.isChecked = true
        else holder.itemView.complete.isChecked = false

    }

    override fun getItemCount(): Int {
        return mTasks!!.size
    }

    interface OnItemClickListener {
        fun onItemClick(itemView: View, position: Int)
    }
}
