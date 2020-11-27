package com.onlinecash.loanswithout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreditsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private boolean hasConnection;
    private Loan[] credits;
    private OnFavouriteClick onFavouriteClick;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param hasConnection Parameter 1.
     * @param credits Parameter 2.
     * @return A new instance of fragment CreditsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreditsFragment newInstance(boolean hasConnection, Loan[] credits, OnFavouriteClick onFavouriteClick) {
        CreditsFragment fragment = new CreditsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, hasConnection);
        args.putParcelableArray(ARG_PARAM2, credits);
        args.putParcelable(ARG_PARAM3, onFavouriteClick);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            hasConnection = getArguments().getBoolean(ARG_PARAM1);
            credits = (Loan[]) getArguments().getParcelableArray(ARG_PARAM2);
            onFavouriteClick = getArguments().getParcelable(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);

        TextView connectionStatusTextView = v.findViewById(R.id.connectionStatusTextView);
        RecyclerView loansRecyclerView = v.findViewById(R.id.loansRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        loansRecyclerView.setLayoutManager(linearLayoutManager);
        if(hasConnection) {
            ArrayList<Loan> loanArrayList = new ArrayList<>();
            Collections.addAll(loanArrayList, credits);
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