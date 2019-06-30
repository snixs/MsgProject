package com.snixs.myapp2.Chat;

import com.snixs.myapp2.User.UserObject;

import java.util.ArrayList;

public class ChatObject {
    private String uid;

    private ArrayList<UserObject> userObjectArrayList = new ArrayList<>();

    public ChatObject(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public ArrayList<UserObject> getUserObjectArrayList() {
        return userObjectArrayList;
    }




    public void addUserToArrayList(UserObject mUser){
        userObjectArrayList.add(mUser);
    }
}
