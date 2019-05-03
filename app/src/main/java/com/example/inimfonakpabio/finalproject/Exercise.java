package com.example.inimfonakpabio.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable{
    public String exerciseName;
    public String muscleGroups;
    public String equipment;
    public String instructions;
    public int imageRes;
    public int exid;

    public Exercise() {}

    public Exercise(String exNmae, int img, String musGrps, String equip, String instr, int exercise_id) {
        this.exerciseName = exNmae;
        this.imageRes = img;
        this.muscleGroups = musGrps;
        this.equipment = equip;
        this.instructions = instr;
        this.exid = exercise_id;
    }


    protected Exercise(Parcel in) {
        exerciseName = in.readString();
        muscleGroups = in.readString();
        equipment = in.readString();
        instructions = in.readString();
        imageRes = in.readInt();
        exid = in.readInt();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(exerciseName);
        parcel.writeString(muscleGroups);
        parcel.writeString(equipment);
        parcel.writeString(instructions);
        parcel.writeInt(imageRes);
        parcel.writeInt(exid);
    }
}
