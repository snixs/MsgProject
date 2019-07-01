package com.snixs.myapp2.User;

public class UserObject {

    private String name, phone, uid, imgUrl;

    public UserObject(String name, String phone, String uid){
        this.name = name;
        this.phone = phone;
        this.uid = uid;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUid() {
        return uid;
    }
}
