package michael.com.kotlindemo.java.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import michael.com.kotlindemo.R;
import michael.com.kotlindemo.java.model.Task;

/**
 * Created by Mikhail on 5/31/17.
 */

public class TaskFragment extends Fragment implements TaskContract.View {

    TaskPresenter mPresenter;
    TaskAdapter mAdapter;
    List<Task> mTasks;

    Toolbar mToolbar;
    TextView mHeaderText;
    EditText mTextInput;
    ProgressBar mProgress;
    FloatingActionButton mButtonSave;
    RecyclerView mRecyclerView;

    public TaskFragment() {

    }

    public static TaskFragment newInstance() {
        return new TaskFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new TaskAdapter(new ArrayList<Task>());
        mPresenter = new TaskPresenter(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        initView(rootView);
        setToolBar();
        setRecyclerView();
        showNewTask();

        mTasks = mPresenter.tasksListRespond();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTaskClickListener();
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unSubscribe();
    }

    @Override
    public void showTasks(List<Task> tasks) {
        mAdapter.replaceData(tasks);
    }

    @Override
    public void showAddedTasks(Task task) {
        mTasks.add(task);
        mAdapter.replaceData(mTasks);
    }

    @Override
    public void showCompletedTasks(List<Task> tasks) {
        mAdapter.replaceData(tasks);
        progressBarManager(View.INVISIBLE);
    }

    @Override
    public void showProgressBar() {
        progressBarManager(View.VISIBLE);
    }

    @Override
    public void removeProgressBar() {
        progressBarManager(View.INVISIBLE);
    }

    @Override
    public void showError(String errorMessage) {
        showMessage(errorMessage);
    }

    @Override
    public void showSuccess(String successMessage) {
        showMessage(successMessage);
    }

    @Override
    public void showHeader(String title) {
        mHeaderText.setText(title);
    }

    @Override
    public void showUpdatedTask() {

        mAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Task task = mTasks.get(position);
                mPresenter.changeTaskStatus(task);
                mPresenter.updateTask(task.getId(), task);
                mAdapter.replaceData(mTasks);
                Log.d("Click", task.getTaskTitle());

            }

        });
    }

    @Override
    public void clearInputText() {
        mTextInput.setText("");
        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setTaskClickListener() {
        showUpdatedTask();
    }

    private void progressBarManager(int visibility) {
        mProgress.setVisibility(visibility);
    }

    private void showNewTask() {

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTask = mTextInput.getText().toString();
                mPresenter.saveTask(newTask);
                mPresenter.loadTasks(true);
            }
        });
    }

    private void showMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    private void initView(View view) {
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mHeaderText = (TextView) view.findViewById(R.id.header);
        mTextInput = (EditText) view.findViewById(R.id.editText);
        mProgress = (ProgressBar) view.findViewById(R.id.progress_bar);
        mButtonSave = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
    }

    private void setRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setToolBar() {
        mToolbar.inflateMenu(R.menu.menu_main);
        setHasOptionsMenu(true);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.all) {
                    mPresenter.loadTasks(true);
                }
                if (id == R.id.completed) {
                    mPresenter.loadCompletedTasks();
                }
                return true;
            }
        });
    }
}
