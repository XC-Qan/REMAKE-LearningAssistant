package com.bakamcu.remake.learningassistant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Problem类相关的viewmodel
 */

public class ProblemsListViewModel extends AndroidViewModel {
    ProblemRepo problemRepo;

    public ProblemsListViewModel(@NonNull Application application) {
        super(application);
        problemRepo = new ProblemRepo(application);
    }

    LiveData<List<Problem>> getAllProblemsLive() {
        return problemRepo.getAllProbLive();
    }

    LiveData<List<Problem>> findProblemWithPattern(String pattern) {
        return problemRepo.searchedProblems(pattern);
    }

    void insertProbs(Problem... problems) {
        problemRepo.insertProbs(problems);
    }

    void updateProbs(Problem... problems) {
        problemRepo.updateProbs(problems);
    }

    void deleteProbs(Problem... problems) {
        problemRepo.deleteProbs(problems);
    }

    void deleteAllProbs() {
        problemRepo.deleteAllProbs();
    }

    MutableLiveData<List<Problem>> getProblemsWithQuery(String queryName, String query) {
        MutableLiveData<List<Problem>> liveTempList = new MutableLiveData<>();
        LCQuery<LCObject> lcquery = new LCQuery<>("Problems");
        //lcquery.whereContains(queryName, query);
        lcquery.whereEqualTo("user", LCUser.getCurrentUser());
        lcquery.findInBackground().subscribe(new Observer<List<LCObject>>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(List<LCObject> LCProblems) {
                List<Problem> tempList = new ArrayList<>();
                for (LCObject object : LCProblems) {
                    Problem temp = new Problem(
                            (String) object.get("subject"),
                            (String) object.get("problemSource"),
                            (String) object.get("problem"),
                            (String) object.get("wrongAnswer"),
                            (String) object.get("correctAnswer"),
                            (String) object.get("problemImagePath"),
                            (String) object.get("wrongAnswerImagePath"),
                            (String) object.get("correctAnswerImagePath"),
                            (String) object.get("reason"),
                            0,
                            false,
                            0.3f
                    );
                    tempList.add(temp);
                }
                Collections.reverse(tempList);
                liveTempList.setValue(tempList);
            }

            public void onError(Throwable throwable) {
            }

            public void onComplete() {
            }
        });
        return liveTempList;
    }
}