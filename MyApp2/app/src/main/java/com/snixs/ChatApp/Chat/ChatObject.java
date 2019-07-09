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



    public String getChatName(String asUserId)
    {
        String name="def";
        for(int i=0; i< userObjectArrayList.size(); i++)
        {
            if(!asUserId.equals(userObjectArrayList.get(i).getUid())) {
             name=userObjectArrayList.get(i).getName();
            }
            else name="def";
        }
        return name;
    }


    public void addUserToChat(UserObject mUser){
        userObjectArrayList.add(mUser);
    }
}
