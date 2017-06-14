package michael.com.kotlindemo.kotlin.ui

import android.util.Log
import michael.com.kotlindemo.kotlin.model.ResponseObject
import michael.com.kotlindemo.kotlin.model.Task
import michael.com.kotlindemo.kotlin.network.TaskService
import retrofit2.Response
import rx.Observable
import rx.SingleSubscriber
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

/**
 * Created by Mikhail on 5/31/17.
 */

class TaskPresenter(private val mView: TaskContract.View) : TaskContract.Presenter {


    private var mSubscription: CompositeSubscription

    init {
        mSubscription = CompositeSubscription()
    }

    override fun loadTasks(isLoading: Boolean) {

        if (isLoading) {
            mView.showProgressBar()
            tasksListRespond()
        }

    }

    fun tasksListRespond(): MutableList<Task> {

        val cache = ArrayList<Task>()

        mSubscription.add(TaskService.networkCall().getTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<ResponseObject>>() {
                    override fun onCompleted() {
                        mView.removeProgressBar()
                        mView.showHeader("My tasks:")
                    }

                    override fun onError(e: Throwable) {
                        mView.showError("Unable to load tasks")
                        Log.d("Presenter", e.toString())
                    }

                    override fun onNext(tasks: Response<ResponseObject>) {
                        Log.d("Response code", tasks.code().toString())
                        Log.d("Response", tasks.body()!!.tasks!![0].taskTitle)
                        val results = tasks.body()!!.tasks
                        mView.showTasks(results!!)
                        cache.addAll(results)

                    }

                }))

        return cache

    }


    override fun loadCompletedTasks() {
        mView.showProgressBar()
        completedTasksRequest()
    }

    private fun completedTasksRequest() {
        val subscription = TaskService.networkCall().getTasks()
                .flatMap { responseObject -> Observable.from(responseObject.body()!!.tasks!!) }
                .filter({ t: Task? -> t?.isCompleted })
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ tasks ->
                    mView.showCompletedTasks(tasks)
                    mView.showHeader("Completed tasks:")
                }) { mView.showError("Unable to load completed tasks") }

        mSubscription.add(subscription)
    }

    override fun saveTask(text: String) {

        val newTask = Task(taskTitle = text)

        if (newTask.isEmpty()) {
            mView.showError("Task cannot be empty!")
        } else {
            makeNewTaskRequest(newTask)
            mView.clearInputText()
        }

    }

    fun makeNewTaskRequest(task: Task) {

        mSubscription.add(TaskService.networkCall().saveTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleSubscriber<Response<ResponseObject>>() {
                    override fun onSuccess(value: Response<ResponseObject>) {
                        mView.showSuccess("New task saved")

                        val taskId = value.body()!!.id
                        val taskTitle = value.body()!!.taskTitle
                        val taskStatus = value.body()!!.isCompleted

                        Log.d("NEWTASK", "$taskId $taskTitle $taskStatus")

                        val newTask = Task(taskId!!, taskTitle!!, false)

                        mView.showAddedTasks(newTask)
                    }

                    override fun onError(error: Throwable) {
                        Log.d("NEWTASK", "$error")
                        mView.showError("Unable to save a new task")
                    }
                }))

    }

    override fun updateTask(id: String, task: Task) {

        mSubscription.add(TaskService.networkCall().updateTask(id, task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleSubscriber<Response<ResponseObject>>() {
                    override fun onSuccess(taskId: Response<ResponseObject>) {
                        Log.d("Update task", taskId.body().toString())
                        mView.showUpdatedTask()
                    }

                    override fun onError(error: Throwable) {
                        mView.showError("Unable to update task")
                    }
                })
        )
    }

    override fun subscribe() {
        loadTasks(false)
    }

    override fun unSubscribe() {
        mSubscription.clear()
    }

    fun changeTaskStatus(task: Task) {
        task.isCompleted = task.isCompleted != true
    }

}





