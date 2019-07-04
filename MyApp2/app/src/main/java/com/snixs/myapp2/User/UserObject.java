package com.snixs.myapp2.User;

import android.os.Parcel;
import android.os.Parcelable;

public class UserObject implements Parcelable {

    private String name, phone, uid, imgUrl;

    public UserObject(String name, String phone, String uid){
        this.name = name;
        this.phone = phone;
        this.uid = uid;

    }

    protected UserObject(Parcel in) {
        name = in.readString();
        phone = in.readString();
        uid = in.readString();
        imgUrl = in.readString();
    }

    public static final Creator<UserObject> CREATOR = new Creator<UserObject>() {
        @Override
        public UserObject createFromParcel(Parcel in) {
            return new UserObject(in);
        }

        @Override
        public UserObject[] newArray(int size) {
            return new UserObject[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(uid);
        dest.writeString(imgUrl);
    }
}
