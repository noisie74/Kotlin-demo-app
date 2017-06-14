package michael.com.kotlindemo.java.ui;

import java.util.List;

import michael.com.kotlindemo.java.model.Task;

/**
 * Created by Mikhail on 5/31/17.
 */

public interface TaskContract {

    interface View {

        void showTasks(List<Task> tasks);

        void showAddedTasks(Task task);

        void showCompletedTasks(List<Task> tasks);

        void showUpdatedTask();

        void showProgressBar();

        void removeProgressBar();

        void showError(String errorMessage);

        void showSuccess(String successMessage);

        void showHeader(String header);

        void clearInputText();

    }

    interface Presenter {

        void loadTasks(boolean isLoading);

        void loadCompletedTasks();

        void saveTask(String text);

        void updateTask(String id, Task task);

        void subscribe();

        void unSubscribe();
    }
}
