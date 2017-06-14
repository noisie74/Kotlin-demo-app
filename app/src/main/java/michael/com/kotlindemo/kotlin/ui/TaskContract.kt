package michael.com.kotlindemo.kotlin.ui

import michael.com.kotlindemo.kotlin.model.Task

/**
 * Created by Mikhail on 5/31/17.
 */

interface TaskContract {

    interface View {

        fun showTasks(tasks: MutableList<Task>)

        fun showAddedTasks(task: Task)

        fun showCompletedTasks(tasks: MutableList<Task>)

        fun showUpdatedTask()

        fun showProgressBar()

        fun removeProgressBar()

        fun showError(errorMessage: String)

        fun showSuccess(successMessage: String)

        fun showHeader(header: String)

        fun clearInputText()

    }

    interface Presenter {

        fun loadTasks(isLoading: Boolean)

        fun loadCompletedTasks()

        fun saveTask(text: String)

        fun updateTask(id: String, task: Task)

        fun subscribe()

        fun unSubscribe()
    }
}

