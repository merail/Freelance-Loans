package com.onlinecash.loanswithout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#FavouritesFragment()} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ParcelCreator")
public class FavouritesFragment extends Fragment implements OnLastFavouriteRemoveListener{

    private TextView noFavouritesTextView;
    private RecyclerView favouritesRecyclerView;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);

        noFavouritesTextView = v.findViewById(R.id.favouritesStatusTextView);

        favouritesRecyclerView = v.findViewById(R.id.favouritesRecyclerView);
        ArrayList<Loan> favourites = new ArrayList<>();
        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().contains("favourite")) {
                Gson gson = new Gson();
                Loan loan = gson.fromJson((String) entry.getValue(), Loan.class);
                favourites.add(loan);
            }
        }

        if (favourites.isEmpty())
            noFavouritesTextView.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        favouritesRecyclerView.setLayoutManager(linearLayoutManager);
        LoansAdapter loansAdapter = new LoansAdapter(getContext(), favourites, true, FavouritesFragment.this);
        favouritesRecyclerView.setAdapter(loansAdapter);

        return v;
    }

    @Override
    public void onLastFavouriteRemove() {
        noFavouritesTextView.setVisibility(View.VISIBLE);
        favouritesRecyclerView.setVisibility(View.GONE);
    }
}