package com.onlinecash.loanswithout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardsFragment extends Fragment {
    private TextView connectionStatusTextView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private Boolean hasConnection;
    private Cards[] cards;
    private OnFavouriteClick onFavouriteClick;

    public CardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param hasConnection Parameter 1.
     * @param onFavouriteClick Parameter 2.
     * @return A new instance of fragment CardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardsFragment newInstance(boolean hasConnection, Cards[] cards, OnFavouriteClick onFavouriteClick) {
        CardsFragment fragment = new CardsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, hasConnection);
        args.putParcelableArray(ARG_PARAM2, cards);
        args.putParcelable(ARG_PARAM3, onFavouriteClick);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hasConnection = getArguments().getBoolean(ARG_PARAM1);
            cards = (Cards[]) getArguments().getParcelableArray(ARG_PARAM2);
            onFavouriteClick = getArguments().getParcelable(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cards, container, false);

        connectionStatusTextView = v.findViewById(R.id.connectionStatusTextView);
        RecyclerView cardsRecyclerView = v.findViewById(R.id.cardsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        cardsRecyclerView.setLayoutManager(linearLayoutManager);

        if(hasConnection) {
            ArrayList<Loan> cards_credit = new ArrayList<>();
            Collections.addAll(cards_credit, cards[0].cards_credit);
            LoansAdapter loansAdapter = new LoansAdapter(Objects.requireNonNull(getContext()), cards_credit, onFavouriteClick);
            cardsRecyclerView.setAdapter(loansAdapter);
        }
        else
        {
            connectionStatusTextView.setVisibility(View.VISIBLE);
        }

        TabLayout cardsTabLayout = v.findViewById(R.id.cardsTabLayout);
        cardsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                {
                    if(hasConnection) {
                        ArrayList<Loan> cards_credit = new ArrayList<>();
                        Collections.addAll(cards_credit, cards[0].cards_credit);
                        LoansAdapter loansAdapter = new LoansAdapter(Objects.requireNonNull(getContext()), cards_credit, onFavouriteClick);
                        cardsRecyclerView.setAdapter(loansAdapter);
                    }
                    else
                    {
                        connectionStatusTextView.setVisibility(View.VISIBLE);
                    }
                }
                else if(tab.getPosition() == 1)
                {
                    if(hasConnection) {
                    ArrayList<Loan> cards_debit = new ArrayList<>();
                    Collections.addAll(cards_debit, cards[1].cards_debit);
                    LoansAdapter loansAdapter = new LoansAdapter(Objects.requireNonNull(getContext()), cards_debit, onFavouriteClick);
                    cardsRecyclerView.setAdapter(loansAdapter);
                    }
                    else
                    {
                        connectionStatusTextView.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    if(hasConnection) {
                        ArrayList<Loan> cards_installment = new ArrayList<>();
                        Collections.addAll(cards_installment, cards[2].cards_installment);
                        LoansAdapter loansAdapter = new LoansAdapter(Objects.requireNonNull(getContext()), cards_installment, onFavouriteClick);
                        cardsRecyclerView.setAdapter(loansAdapter);
                    }
                    else
                    {
                        connectionStatusTextView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return v;
    }
}