package com.onlinecash.loanswithout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private static final String ARG_CARDS = "cards";
    private static final String ARG_TAB = "tab";
    private static final String ARG_ELEMENT = "element";
    private static final String ARG_OPEN_TYPE = "open_type";

    private TextView connectionStatusTextView;
    private RecyclerView cardsRecyclerView;
    private int element;
    private String openType = "offerwall";

    private Cards[] cards;
    private int tab;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cards cards.
     * @return A new instance of fragment CardsFragment.
     */
    public static CardsFragment newInstance(Cards[] cards, int tab, int element, String openType) {
        CardsFragment fragment = new CardsFragment();

        Bundle args = new Bundle();
        args.putParcelableArray(ARG_CARDS, cards);
        args.putInt(ARG_TAB, tab);
        args.putInt(ARG_ELEMENT, element);
        args.putString(ARG_OPEN_TYPE, openType);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cards = (Cards[]) getArguments().getParcelableArray(ARG_CARDS);
            tab = getArguments().getInt(ARG_TAB);
            element = getArguments().getInt(ARG_ELEMENT);
            openType = getArguments().getString(ARG_OPEN_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cards, container, false);

        connectionStatusTextView = v.findViewById(R.id.connectionStatusTextView);

        cardsRecyclerView = v.findViewById(R.id.cardsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        cardsRecyclerView.setLayoutManager(linearLayoutManager);
        setList(0);

        TabLayout cardsTabLayout = v.findViewById(R.id.cardsTabLayout);
        cardsTabLayout.setTabTextColors(getResources().getColor(R.color.mainColor)
                , getResources().getColor(R.color.mainColor));
        cardsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    setList(0);
                } else if (tab.getPosition() == 1) {
                    setList(1);
                } else {
                    setList(2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        cardsTabLayout.selectTab(cardsTabLayout.getTabAt(tab));

        return v;
    }

    private void setList(int listType) {
        if (cards != null) {
            ArrayList<Loan> cardsArrayList = new ArrayList<>();
            if (listType == 0)
                Collections.addAll(cardsArrayList, cards[0].cards_credit);
            else if (listType == 1)
                Collections.addAll(cardsArrayList, cards[1].cards_debit);
            else
                Collections.addAll(cardsArrayList, cards[2].cards_installment);
            LoansAdapter loansAdapter = new LoansAdapter(Objects.requireNonNull(getContext()), cardsArrayList,
                    false, null, openType, element);
            cardsRecyclerView.setAdapter(loansAdapter);
            cardsRecyclerView.scrollToPosition(element);
        } else {
            connectionStatusTextView.setVisibility(View.VISIBLE);
        }

    }
}