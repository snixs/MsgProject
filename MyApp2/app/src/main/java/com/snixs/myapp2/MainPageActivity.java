package com.snixs.myapp2;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.snixs.myapp2.Chat.ChatListAdapter;
import com.snixs.myapp2.Chat.ChatObject;
import com.snixs.myapp2.User.UserObject;
import com.snixs.myapp2.Utils.CountryToPhonePrefix;

import java.util.ArrayList;
import java.util.HashSet;

public class MainPageActivity extends AppCompatActivity {

    private RecyclerView mChatList;
    private RecyclerView.Adapter mChatListAdapter;
    private RecyclerView.LayoutManager mChatListLayoutManager;

    ArrayList<UserObject> userList, contactList;
    ArrayList<ChatObject> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactList = new ArrayList<>();
        userList = new ArrayList<>();
        chatList = new ArrayList<>();

        Button mLogout = findViewById(R.id.logout);
        Button mFindUser = findViewById(R.id.findUser);

        mFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindUserActivity.class);
                intent.putExtra("userList", userList);
                startActivity(intent);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            }
        });

        getPermissions();
        getContactList();
        initRecyclerView();
        getUserChatList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }


    private void getContactList(){

        int i=0;
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone._ID, null, null);
        HashSet<String> exists = new HashSet<>();
        while(phones.moveToNext())
        {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String Test = name;

            boolean b = exists.add(Test);

            if(b)
            {
                phone = phone.replace(" ", "");
                phone = phone.replace("-", "");
                phone = phone.replace("(", "");
                phone = phone.replace(")", "");

                if(!String.valueOf(phone.charAt(0)).equals("+")) {
                    String ISOPrefix = getCountryISO();
                    if (String.valueOf(phone.charAt(0)).equals("0"))
                        phone = ISOPrefix + phone.substring(1) ;
                    else
                        phone = ISOPrefix + phone;
                }
                System.out.println("the phone is = " + phone + " " + name);
                i=i+1;
                UserObject mContact = new UserObject(name, phone, "");
                contactList.add(mContact);
                getUserDetails(mContact);
            }

        }
        phones.close();
    }
    private void getUserDetails(final UserObject mContact)
    {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhone());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String  phone = "",
                            name = "";
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        if(childSnapshot.child("phone").getValue()!=null)
                            phone = childSnapshot.child("phone").getValue().toString();
                        if(childSnapshot.child("name").getValue()!=null)
                            name = mContact.getName();


                        UserObject mUser = new UserObject(name, phone, childSnapshot.getKey());
                        // if (name.equals(phone))
                        //  for(UserObject mContactIterator : contactList){
                        //     if(mContactIterator.getPhone().equals(mUser.getPhone())){
                        //       mUser.setName(mContactIterator.getName());
                        // }
                        //}
                        userList.add(mUser);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void getUserChatList(){
        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");

        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                        ChatObject mChat = new ChatObject(childSnapshot.getKey());
                        boolean ex = false;
                        for (ChatObject mChatIterator : chatList) {
                            if(mChatIterator.getUid().equals(mChat.getUid()))
                                ex = true;
                        }
                        if(ex) continue;
                        String key;
                        UserObject user;
                      //  System.out.println(childSnapshot.getKey() + "this is the chat");
                        DatabaseReference usersChat = FirebaseDatabase.getInstance().getReference().child("chat").child(childSnapshot.getKey());
                        usersChat.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                System.out.println(dataSnapshot.getKey());
                                System.out.println(dataSnapshot.getValue());
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        FirebaseDatabase.getInstance().getReference().child("chat").child(childSnapshot.getKey()).child("user1");
                        DatabaseReference mUserChat2 = FirebaseDatabase.getInstance().getReference().child("chat").child(childSnapshot.getKey()).child("user2");
                     //   System.out.println(mUserChat2.getKey());
                /*        if(FirebaseAuth.getInstance().getUid().equals(mUserChat1.getKey()))
                        {
                           key = mUserChat2.getKey();
                        }
                        else key = mUserChat1.getKey();

                        for (UserObject mUserIterator : userList){
                            if(mUserIterator.getUid().equals(key))
                            {
                                user = new UserObject(mUserIterator.getName(),mUserIterator.getPhone(),mUserIterator.getUid());
                                mChat.addUserToChat(user);
                            }
                        }
                       // UserObject user = new UserObject();*/
                //        mChat.addUserToChat();

                            chatList.add(mChat);
                            mChatListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setmChat()
    {
        for(int i=0; i<userList.size(); i++)
        {

        }
    }

    private void getPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
        }
    }
    private String getCountryISO(){
        String iso = null;
        TelephonyManager tM = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if(tM.getNetworkCountryIso()!=null)
            if (!tM.getNetworkCountryIso().equals(""))
                iso = tM.getNetworkCountryIso();
        return  CountryToPhonePrefix.getPhone(iso);
    }


    private void initRecyclerView() {
        mChatList = findViewById(R.id.chatList);
        mChatList.setNestedScrollingEnabled(false);
        mChatList.setHasFixedSize(false);
        mChatListLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        mChatList.setLayoutManager(mChatListLayoutManager);
        mChatListAdapter = new ChatListAdapter(chatList);
        mChatList.setAdapter(mChatListAdapter);
    }
}