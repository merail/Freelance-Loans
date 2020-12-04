package com.onlinecash.loanswithout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreditsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditsFragment extends Fragment {

    private static final String ARG_CREDITS = "credits";
    private static final String ARG_ELEMENT = "element";
    private static final String ARG_OPEN_TYPE = "open_type";

    private Loan[] credits;
    private int element;
    private String openType = "offerwall";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param credits credits.
     * @return A new instance of fragment CreditsFragment.
     */
    public static CreditsFragment newInstance(Loan[] credits, int element, String openType) {
        CreditsFragment fragment = new CreditsFragment();

        Bundle args = new Bundle();
        args.putParcelableArray(ARG_CREDITS, credits);
        args.putInt(ARG_ELEMENT, element);
        args.putString(ARG_OPEN_TYPE, openType);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            credits = (Loan[]) getArguments().getParcelableArray(ARG_CREDITS);
            element = getArguments().getInt(ARG_ELEMENT);
            openType = getArguments().getString(ARG_OPEN_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_credits, container, false);

        TextView connectionStatusTextView = v.findViewById(R.id.connectionStatusTextView);

        RecyclerView loansRecyclerView = v.findViewById(R.id.favouritesRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        loansRecyclerView.setLayoutManager(linearLayoutManager);

        if (credits != null) {
            ArrayList<Loan> loanArrayList = new ArrayList<>();
            Collections.addAll(loanArrayList, credits);
            LoansAdapter loansAdapter = new LoansAdapter(Objects.requireNonNull(getContext()), loanArrayList,
                    false, null, openType, element);
            loansRecyclerView.setAdapter(loansAdapter);
            loansRecyclerView.scrollToPosition(element);
        } else {
            connectionStatusTextView.setVisibility(View.VISIBLE);
        }

        return v;
    }
}