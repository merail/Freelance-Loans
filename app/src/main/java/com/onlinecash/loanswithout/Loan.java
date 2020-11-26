package com.onlinecash.loanswithout;

import android.os.Parcel;
import android.os.Parcelable;

public class Loan implements Parcelable
{
    public int id;
    public String itemId;
    public String name;
    public String description;
    public String order;
    public String orderButtonText;
    public String percent;
    public String percentPostfix;
    public String percentPrefix;
    public double score;
    public String screen;
    public int show_mastercard;
    public int show_mir;
    public int show_visa;
    public int show_qiwi;
    public int show_yandex;
    public int show_cash;
    public String termPostfix;
    public String termMax;
    public String termMid;
    public String termMin;
    public String termPrefix;
    public String summPostfix;
    public String summPrefix;
    public String summMin;
    public String summMid;
    public String summMax;
    public int hide_TermFields;
    public int hide_PercentFields;

    protected Loan(Parcel in) {
        id = in.readInt();
        itemId = in.readString();
        name = in.readString();
        description = in.readString();
        order = in.readString();
        orderButtonText = in.readString();
        percent = in.readString();
        percentPostfix = in.readString();
        percentPrefix = in.readString();
        score = in.readDouble();
        screen = in.readString();
        show_mastercard = in.readInt();
        show_mir = in.readInt();
        show_visa = in.readInt();
        show_qiwi = in.readInt();
        show_yandex = in.readInt();
        show_cash = in.readInt();
        termPostfix = in.readString();
        termMax = in.readString();
        termMid = in.readString();
        termMin = in.readString();
        termPrefix = in.readString();
        summPostfix = in.readString();
        summPrefix = in.readString();
        summMin = in.readString();
        summMid = in.readString();
        summMax = in.readString();
        hide_TermFields = in.readInt();
        hide_PercentFields = in.readInt();
    }

    public static final Creator<Loan> CREATOR = new Creator<Loan>() {
        @Override
        public Loan createFromParcel(Parcel in) {
            return new Loan(in);
        }

        @Override
        public Loan[] newArray(int size) {
            return new Loan[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(itemId);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(order);
        parcel.writeString(orderButtonText);
        parcel.writeString(percent);
        parcel.writeString(percentPostfix);
        parcel.writeString(percentPrefix);
        parcel.writeDouble(score);
        parcel.writeString(screen);
        parcel.writeInt(show_mastercard);
        parcel.writeInt(show_mir);
        parcel.writeInt(show_visa);
        parcel.writeInt(show_qiwi);
        parcel.writeInt(show_yandex);
        parcel.writeInt(show_cash);
        parcel.writeString(termPostfix);
        parcel.writeString(termMax);
        parcel.writeString(termMid);
        parcel.writeString(termMin);
        parcel.writeString(termPrefix);
        parcel.writeString(summPostfix);
        parcel.writeString(summPrefix);
        parcel.writeString(summMin);
        parcel.writeString(summMid);
        parcel.writeString(summMax);
        parcel.writeInt(hide_TermFields);
        parcel.writeInt(hide_PercentFields);
    }
}
