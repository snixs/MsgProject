package com.snixs.myapp2;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.snixs.myapp2.User.UserListAdapter;
import com.snixs.myapp2.User.UserObject;
import com.snixs.myapp2.Utils.CountryToPhonePrefix;

import java.util.ArrayList;
import java.util.HashSet;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserObject> userList, contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        contactList= new ArrayList<>();
        userList = new ArrayList<>();

        initializeRecyclerView();
        getContactList();
    }

    private void getContactList(){

        int i=0;
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone._ID, null, null);
        HashSet<String> exists = new HashSet<>();
        while(phones.moveToNext())
        {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String Test = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

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
                //  userList.add(mContact);
                //  mUserListAdapter.notifyDataSetChanged();
                getUserDetails(mContact);
            }
            System.out.println("NORMALIZED_NUMBER is = " + Test);
            System.out.println("Statment is = " + b);

        }
        System.out.println("There are this much users = " + i);
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
                        mUserListAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    private String getCountryISO(){
        String iso = null;
        TelephonyManager tM = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if(tM.getNetworkCountryIso()!=null)
            if (!tM.getNetworkCountryIso().equals(""))
                iso = tM.getNetworkCountryIso();
        return  CountryToPhonePrefix.getPhone(iso);
    }

    private void initializeRecyclerView() {
        mUserList= findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        mUserList.setLayoutManager(mUserListLayoutManager);
        mUserListAdapter = new UserListAdapter(userList);
        mUserList.setAdapter(mUserListAdapter);
    }
}
