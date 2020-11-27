package com.onlinecash.loanswithout;

import android.os.Parcel;
import android.os.Parcelable;

public interface OnFavouriteClick extends Parcelable {
    void onFavouriteClick(boolean addToFavourite, Loan loan);

    @Override
    int describeContents();

    @Override
    void writeToParcel(Parcel parcel, int i);
}
