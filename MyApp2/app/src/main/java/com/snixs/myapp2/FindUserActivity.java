package com.snixs.myapp2;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;
import com.snixs.myapp2.User.UserListAdapter;
import com.snixs.myapp2.User.UserObject;
import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity implements UserListAdapter.OnUserListener {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserObject> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        userList = getIntent().getParcelableArrayListExtra("userList");

        initRecyclerView();
    }





    private void initRecyclerView() {
        mUserList= findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        mUserList.setLayoutManager(mUserListLayoutManager);
        mUserListAdapter = new UserListAdapter(userList, this);
        mUserList.setAdapter(mUserListAdapter);
    }

    @Override
    public void OnUserClick(int position) {

        String me = FirebaseAuth.getInstance().getUid();
        String user = userList.get(position).getUid();
        int i = 1;

        String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
        FirebaseDatabase.getInstance().getReference().child("user").child(me).child("chat").child(key).setValue(true);
        FirebaseDatabase.getInstance().getReference().child("user").child(user).child("chat").child(key).setValue(true);
        FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("user1").setValue(me);
        FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("user2").setValue(user);
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("chatID", key);
        bundle.putString("uName", userList.get(position).getName());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }
}
