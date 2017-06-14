package michael.com.kotlindemo.kotlin.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import michael.com.kotlindemo.R
import michael.com.kotlindemo.kotlin.model.Task
import michael.com.kotlindemo.kotlin.util.invisible
import michael.com.kotlindemo.kotlin.util.visible

/**
 * Created by Mikhail on 5/31/17.
 */

class TaskFragment : Fragment(), TaskContract.View {

    private lateinit var mPresenter: TaskPresenter
    private lateinit var mAdapter: TaskAdapter

    private var mTasks: MutableList<Task>? = null

    private lateinit var mToolbar: Toolbar
    private lateinit var mHeaderText: TextView
    private lateinit var mTextInput: EditText
    private lateinit var mProgress: ProgressBar
    private lateinit var mButtonSave: FloatingActionButton
    private lateinit var mRecyclerView: RecyclerView

    companion object {

        fun newInstance(): TaskFragment {
            return TaskFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAdapter = TaskAdapter(ArrayList<Task>())
        mPresenter = TaskPresenter(this)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater!!.inflate(R.layout.fragment_main, container, false)

        initView(rootView)
        setToolBar()
        setRecyclerView()
        setSaveButton()

        mTasks = mPresenter.tasksListRespond()

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTaskClickListener()
    }


    override fun clearInputText() {
        mTextInput.setText("")
        activity.window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun showTasks(tasks: MutableList<Task>) {
        mAdapter.replaceData(tasks)
    }


    override fun showCompletedTasks(tasks: MutableList<Task>) {
        mAdapter.replaceData(tasks)
        mProgress.invisible()
    }


    override fun showAddedTasks(task: Task) {
        mTasks?.add(task)
        mAdapter.replaceData(mTasks!!)

    }

    override fun showProgressBar() {
        mProgress.visible()
    }

    override fun removeProgressBar() {
        mProgress.invisible()
    }

    override fun showError(errorMessage: String) {
        showMessage(errorMessage)
    }

    override fun showSuccess(successMessage: String) {
        showMessage(successMessage)
    }

    override fun showHeader(header: String) {
        mHeaderText.text = header
    }

    override fun onResume() {
        super.onResume()
        mPresenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.unSubscribe()
    }

    override fun showUpdatedTask() {

        mAdapter.setOnItemClickListener(object : TaskAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View, position: Int) {

                val task: Task = mTasks!![position]
                mPresenter.changeTaskStatus(task)
                mPresenter.updateTask(task.id!!, task)
                mAdapter.replaceData(mTasks!!)

                Log.d("Click", task.taskTitle + " $position")
            }
        })
    }

    private fun setTaskClickListener() {
        showUpdatedTask()
    }

    private fun setSaveButton() {

        mButtonSave.setOnClickListener {
            val newTask = mTextInput.text.toString()
            mPresenter.saveTask(newTask)
            mPresenter.loadTasks(true)

        }
    }

    private fun showMessage(message: String) {
        if (view != null) {
            Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun initView(view: View) {
        mToolbar = activity.findViewById(R.id.toolbar) as Toolbar
        mRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        mHeaderText = view.findViewById(R.id.header) as TextView
        mTextInput = view.findViewById(R.id.editText) as EditText
        mProgress = view.findViewById(R.id.progress_bar) as ProgressBar
        mButtonSave = view.findViewById(R.id.floatingActionButton) as FloatingActionButton
    }

    private fun setRecyclerView() {
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setToolBar() {
        mToolbar.inflateMenu(R.menu.menu_main)
        setHasOptionsMenu(true)
        mToolbar.setTitle(R.string.app_name)
        mToolbar.setOnMenuItemClickListener { item ->
            val id = item.itemId

            if (id == R.id.all) {
                mPresenter.loadTasks(true)
            }
            if (id == R.id.completed) {
                mPresenter.loadCompletedTasks()
            }
            true
        }
    }

}
