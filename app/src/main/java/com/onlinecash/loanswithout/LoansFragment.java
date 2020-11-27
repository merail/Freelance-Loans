package com.onlinecash.loanswithout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoansFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoansFragment extends Fragment {
    private OnFavouriteClick onFavouriteClick;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private Boolean hasConnection;
    private Loan[] loans;

    public LoansFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param hasConnection Parameter 1.
     * @param loans Parameter 2.
     * @return A new instance of fragment LoansFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoansFragment newInstance(boolean hasConnection, Loan[] loans, OnFavouriteClick onFavouriteClick) {
        LoansFragment fragment = new LoansFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, hasConnection);
        args.putParcelableArray(ARG_PARAM2, loans);
        args.putParcelable(ARG_PARAM3, onFavouriteClick);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hasConnection = getArguments().getBoolean(ARG_PARAM1);
            loans = (Loan[]) getArguments().getParcelableArray(ARG_PARAM2);
            onFavouriteClick = getArguments().getParcelable(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loans, container, false);

        TextView connectionStatusTextView = v.findViewById(R.id.connectionStatusTextView);
        RecyclerView loansRecyclerView = v.findViewById(R.id.loansRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        loansRecyclerView.setLayoutManager(linearLayoutManager);
        if(hasConnection) {
            ArrayList<Loan> loanArrayList = new ArrayList<>();
            Collections.addAll(loanArrayList, loans);
            LoansAdapter loansAdapter = new LoansAdapter(Objects.requireNonNull(getContext()), loanArrayList, onFavouriteClick);
            loansRecyclerView.setAdapter(loansAdapter);
        }
        else
        {
            connectionStatusTextView.setVisibility(View.VISIBLE);
        }

        return v;
    }
}