package com.example.inimfonakpabio.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

public class users implements Parcelable {
    public int curDaily;
    public int userid;
    public String email;
    public String name;

    public users() {}

    public users(int curDaily, int userid, String email, String name) {
        this.curDaily = curDaily;
        this.email = email;
        this.name = name;
        this.userid = userid;
    }

    protected users(Parcel in) {
        curDaily = in.readInt();
        userid = in.readInt();
        email = in.readString();
        name = in.readString();
    }

    public static final Creator<users> CREATOR = new Creator<users>() {
        @Override
        public users createFromParcel(Parcel in) {
            return new users(in);
        }

        @Override
        public users[] newArray(int size) {
            return new users[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(userid);
        parcel.writeInt(curDaily);
        parcel.writeString(name);
        parcel.writeString(email);
    }
}
