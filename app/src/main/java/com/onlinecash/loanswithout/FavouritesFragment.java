package com.onlinecash.loanswithout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ParcelCreator")
public class FavouritesFragment extends Fragment implements OnFavouriteClick{

    private ArrayList<Loan> loans;

    private RecyclerView loansRecyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private boolean hasConnection;
    private String mParam2;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouritesFragment newInstance(boolean param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hasConnection = getArguments().getBoolean(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);

        TextView connectionStatusTextView = v.findViewById(R.id.connectionStatusTextView);
        loansRecyclerView = v.findViewById(R.id.loansRecyclerView);

        loans = new ArrayList<>();

        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        Map<String,?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(entry.getKey().contains("favourite"))
            {
                Gson gson = new Gson();
                Loan loan = gson.fromJson((String) entry.getValue(), Loan.class);
                loans.add(loan);
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        loansRecyclerView.setLayoutManager(linearLayoutManager);
        LoansAdapter loansAdapter = new LoansAdapter(getContext(), loans, null);
        loansRecyclerView.setAdapter(loansAdapter);

        return v;
    }

    @Override
    public void onFavouriteClick(boolean addToFavourite, Loan loan) {
        if (addToFavourite)
        {
            loans.add(loan);
            LoansAdapter loansAdapter = new LoansAdapter(Objects.requireNonNull(getContext()), loans, null);
            loansRecyclerView.setAdapter(loansAdapter);
        }
        else
        {
            for(int i = 0;i< loans.size();i++)
            {
                if(loans.get(i).id == loan.id)
                {
                    loans.remove(i);
                    break;
                }
            }
            LoansAdapter loansAdapter = new LoansAdapter(Objects.requireNonNull(getContext()), loans, null);
            loansRecyclerView.setAdapter(loansAdapter);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}