package com.hyphenate.chatuidemo.section.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.hyphenate.chatuidemo.R;
import com.hyphenate.chatuidemo.common.DemoConstant;
import com.hyphenate.chatuidemo.common.interfaceOrImplement.OnResourceParseCallback;
import com.hyphenate.chatuidemo.common.livedatas.LiveDataBus;
import com.hyphenate.chatuidemo.section.friends.viewmodels.ContactBlackViewModel;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.easeui.widget.EaseRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchBlackActivity extends SearchFriendsActivity {
    private ContactBlackViewModel viewModel;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchBlackActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        registerForContextMenu(rvList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.demo_black_list_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = ((EaseRecyclerView.RecyclerViewContextMenuInfo)item.getMenuInfo()).position;
        EaseUser user = (EaseUser) adapter.getItem(position);
        switch (item.getItemId()) {
            case R.id.action_friend_unblock :
                unBlock(user);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void initData() {
        super.initData();
        result = new ArrayList<>();

        viewModel = new ViewModelProvider(this).get(ContactBlackViewModel.class);
        viewModel.blackObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<List<EaseUser>>() {
                @Override
                public void onSuccess(List<EaseUser> data) {
                    mData = data;
                    String search = query.getText().toString().trim();
                    searchMessages(search);
                }
            });
        });
        viewModel.resultObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    loadData();
                    LiveDataBus.get().with(DemoConstant.CONTACT_CHANGE).postValue(EaseEvent.create(DemoConstant.CONTACT_CHANGE, EaseEvent.TYPE.CONTACT));
                }
            });
        });
        loadData();
    }

    @Override
    protected void onChildItemClick(View view, int position) {
        showToast("请长按条目移除黑名单");
    }

    private void loadData() {
        viewModel.getBlackList();
    }

    private void unBlock(EaseUser user) {
        viewModel.removeUserFromBlackList(user.getUsername());
    }
}
