package com.hyphenate.chatuidemo.section.friends.viewmodels;

import android.app.Application;

import com.hyphenate.chat.EMGroup;
import com.hyphenate.chatuidemo.common.livedatas.SingleSourceLiveData;
import com.hyphenate.chatuidemo.common.net.Resource;
import com.hyphenate.chatuidemo.common.repositories.EMGroupManagerRepository;
import com.hyphenate.chatuidemo.common.repositories.EMPushManagerRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PublicGroupViewModel extends AndroidViewModel {
    private EMGroupManagerRepository repository;
    private SingleSourceLiveData<Resource<EMGroup>> groupObservable;
    private SingleSourceLiveData<Resource<Boolean>> joinObservable;

    public PublicGroupViewModel(@NonNull Application application) {
        super(application);
        repository = new EMGroupManagerRepository();
        groupObservable = new SingleSourceLiveData<>();
        joinObservable = new SingleSourceLiveData<>();
    }

    public LiveData<Resource<EMGroup>> getGroupObservable() {
        return groupObservable;
    }

    public void getGroup(String groupId) {
        groupObservable.setSource(repository.getGroupFromServer(groupId));
    }

    public LiveData<Resource<Boolean>> getJoinObservable() {
        return joinObservable;
    }

    public void joinGroup(EMGroup group, String reason) {
        joinObservable.setSource(repository.joinGroup(group, reason));
    }

}
