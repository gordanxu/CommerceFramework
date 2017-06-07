package com.gordan.framework.net.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gordan on 2015/11/5.
 */
public class StudentBean implements Parcelable
{
    private int s_id;

    private String s_name;

    private String s_gender;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(s_id);

        dest.writeString(s_name);

        dest.writeString(s_gender);
    }

    public static final Parcelable.Creator<StudentBean> Creator=new Parcelable.Creator<StudentBean>()
    {
        @Override
        public StudentBean createFromParcel(Parcel source)
        {
            StudentBean bean=new StudentBean();
            bean.s_id=source.readInt();
            bean.s_name=source.readString();
            bean.s_gender=source.readString();

            return bean;
        }

        @Override
        public StudentBean[] newArray(int size) {
            return new StudentBean[size];
        }
    };
}
