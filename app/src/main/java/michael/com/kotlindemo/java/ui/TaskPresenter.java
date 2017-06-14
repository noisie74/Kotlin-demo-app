package michael.com.kotlindemo.java.ui;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import michael.com.kotlindemo.java.model.ResponseObject;
import michael.com.kotlindemo.java.model.Task;
import michael.com.kotlindemo.java.network.TaskService;
import retrofit2.Response;
import rx.Observable;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mikhail on 5/31/17.
 */

public class TaskPresenter implements TaskContract.Presenter {

    private CompositeSubscription mSubscription;
    private TaskContract.View mView;

    public TaskPresenter(TaskContract.View view) {
        mView = view;
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void loadTasks(boolean isLoading) {

        if (isLoading) {
            mView.showProgressBar();
            tasksListRespond();
        }

    }

    public List<Task> tasksListRespond() {

        final ArrayList<Task> cache = new ArrayList<>();

        mSubscription.add(TaskService.networkCall().getTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<ResponseObject>>() {
                    @Override
                    public void onCompleted() {
                        mView.removeProgressBar();
                        mView.showHeader("My tasks:");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError("Unable to load tasks");
                        Log.d("Presenter", e.toString());
                    }

                    @Override
                    public void onNext(Response<ResponseObject> tasks) {
                        Log.d("Response code", String.valueOf(tasks.code()));
                        Log.d("Response", tasks.body().getTasksResponse().get(0).getTaskTitle());
                        List<Task> results = tasks.body().getTasksResponse();
                        mView.showTasks(results);
                        cache.addAll(results);

                    }

                }));

        return cache;

    }


    @Override
    public void loadCompletedTasks() {
        mView.showProgressBar();
        completedTasksRequest();
    }


    private void completedTasksRequest() {
        Subscription subscription = TaskService.networkCall().getTasks()
                .flatMap(new Func1<Response<ResponseObject>, Observable<Task>>() {
                    @Override
                    public Observable<Task> call(Response<ResponseObject> responseObject) {
                        return Observable.from(responseObject.body().getTasksResponse());
                    }
                })
                .filter(new Func1<Task, Boolean>() {
                    @Override
                    public Boolean call(Task tasks) {
                        return tasks.isCompleted();
                    }
                })
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Task>>() {
                    @Override
                    public void call(List<Task> tasks) {
                        mView.showCompletedTasks(tasks);
                        mView.showHeader("Completed tasks:");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showError("Unable to load completed tasks");
                    }
                });

        mSubscription.add(subscription);
    }

    @Override
    public void saveTask(String text) {

        Task newTask = new Task(text);

        if (newTask.isEmpty()) {
            mView.showError("Task cannot be empty!");
        } else {
            makeNewTaskRequest(newTask);
            mView.clearInputText();
        }

    }

    public void makeNewTaskRequest(Task task) {

        mSubscription.add(TaskService.networkCall().saveTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Response<ResponseObject>>() {
                    @Override
                    public void onSuccess(Response<ResponseObject> value) {
                        mView.showSuccess("New task saved");

                        if (value.body() != null) {

                            Log.d("NEWTASK", value.body().getNewTaskId() +
                                    " " + value.body().getNewTaskTitle() +
                                    " " + value.body().getNewTaskStatus());
                        }

                        String taskId = value.body().getNewTaskId();
                        String taskTitle = value.body().getNewTaskTitle();
                        boolean taskStatus = value.body().getNewTaskStatus();

                        Task task = new Task(taskId, taskTitle, taskStatus);

                        mView.showAddedTasks(task);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.d("NEWTASK", error.toString());
                        mView.showError("Unable to save a new task");
                    }
                }));

    }

    @Override
    public void updateTask(String id, Task task) {

        mSubscription.add(TaskService.networkCall().updateTask(id, task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Response<ResponseObject>>() {
                    @Override
                    public void onSuccess(Response<ResponseObject> taskId) {
                        mView.showUpdatedTask();
                    }

                    @Override
                    public void onError(Throwable error) {
                        mView.showError("Unable to update task");
                    }
                })
        );
    }

    @Override
    public void subscribe() {
        loadTasks(false);
    }

    @Override
    public void unSubscribe() {
        mSubscription.clear();
    }

    protected void changeTaskStatus(Task task) {
        if (task != null && task.isCompleted()) {
            task.setIsCompleted(false);
        } else {
            task.setIsCompleted(true);
        }
    }
}
