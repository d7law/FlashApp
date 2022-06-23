package com.example.wallpaperunsplash_advancedandroid.adapter.roomDatabase;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PathRepository {
    PathDAO pathDAO;
    List<PathDownload> paths;

    public PathRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        pathDAO = db.pathDAO();
    }

    public int getPathDownloaded() throws ExecutionException, InterruptedException {
        Future<Integer> data = AppDatabase.databaseWriteExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int count = pathDAO.getAll().size();
                return count;
            }
        });
        return data.get();
    }
    public void insert(PathDownload pathDownload){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                pathDAO.insertPath(pathDownload);
            }
        });
    }
    public void deletePath(PathDownload pathDownload){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                pathDAO.deletePath(pathDownload);
            }
        });
    }
    public void deleteAll(){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                pathDAO.delete();
            }
        });
    }
    public List<PathDownload> getAllPaths() throws ExecutionException, InterruptedException{
        Future<List<PathDownload>> data = AppDatabase.databaseWriteExecutor.submit(new Callable<List<PathDownload>>() {
            @Override
            public List<PathDownload> call() throws Exception {
                List<PathDownload> dataPath = pathDAO.getAll();
                return dataPath;
            }
        });
        return data.get();
    }
}
