package com.example.myapplication;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserViewModel extends AndroidViewModel {
    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserViewModel(Application application) {
        super(application);
        AppDatabase database = DatabaseClient.getInstance(application).getAppDatabase();
        userDao = database.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public void insertUser(User user) {
        executorService.execute(() -> userDao.insert(user));
    }

}
