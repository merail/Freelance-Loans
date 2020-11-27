package com.onlinecash.loanswithout;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class Cards implements Parcelable
{
    public Loan[] cards_credit;
    public Loan[] cards_debit;
    public Loan[] cards_installment;

    protected Cards(Parcel in) {
        cards_credit = in.createTypedArray(Loan.CREATOR);
        cards_debit = in.createTypedArray(Loan.CREATOR);
        cards_installment = in.createTypedArray(Loan.CREATOR);
    }

    public static final Creator<Cards> CREATOR = new Creator<Cards>() {
        @Override
        public Cards createFromParcel(Parcel in) {
            return new Cards(in);
        }

        @Override
        public Cards[] newArray(int size) {
            return new Cards[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeArray(cards_credit);
        parcel.writeArray(cards_debit);
        parcel.writeArray(cards_installment);
    }
}
