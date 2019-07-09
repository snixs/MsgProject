package com.snixs.myapp2;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.snixs.myapp2.User.UserListAdapter;
import com.snixs.myapp2.User.UserObject;
import java.util.ArrayList;
import java.util.Arrays;

public class FindUserActivity extends AppCompatActivity implements UserListAdapter.OnUserListener {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;
    ArrayList <String> mChatIds;
    ArrayList <String> uChatIds;

    ArrayList<UserObject> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        userList = getIntent().getParcelableArrayListExtra("userList");
        mChatIds= new ArrayList<>();
        uChatIds= new ArrayList<>();

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
        String chatUid =  CheckChatExists(user);
        String key = null;

        if(chatUid != null)
        {
            key = chatUid;
        }
        else {
            key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
            FirebaseDatabase.getInstance().getReference().child("user").child(me).child("chat").child(key).setValue(true);
            FirebaseDatabase.getInstance().getReference().child("user").child(user).child("chat").child(key).setValue(true);
            FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("user").child("1").setValue(me);
            FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("user").child("2").setValue(user);
        }
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("chatID", key);
        bundle.putString("uName", userList.get(position).getName());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }


    public String CheckChatExists(final String userUid)
    {
        String uId=null;
        String me = FirebaseAuth.getInstance().getUid();



        DatabaseReference myChats = FirebaseDatabase.getInstance().getReference().child("user").child(me).child("chat");
        myChats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("this is what we get " + dataSnapshot.getChildrenCount());
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot meDataChildern : dataSnapshot.getChildren()){
                        System.out.println("this is what we get children " + meDataChildern.getKey());
                        mChatIds.add(meDataChildern.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference uChats = FirebaseDatabase.getInstance().getReference().child("user").child(userUid).child("chat");
        uChats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot meDataChildern : dataSnapshot.getChildren()){
                        uChatIds.add(meDataChildern.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        for(int i=0; i<uChatIds.size(); i++) {
            if(mChatIds.contains(uChatIds.get(i)))
            {
                uId = uChatIds.get(i);
            }
        }
        return uId;
    }
}

