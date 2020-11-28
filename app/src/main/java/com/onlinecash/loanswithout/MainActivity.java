package com.onlinecash.loanswithout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.onlinecash.loanswithout.Utils.CARDS_FRAGMENT_TAG;
import static com.onlinecash.loanswithout.Utils.CREDITS_FRAGMENT_TAG;
import static com.onlinecash.loanswithout.Utils.FAVOURITES_FRAGMENT_TAG;
import static com.onlinecash.loanswithout.Utils.LOANS_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_CONNECTION_STATUS = "CONNECTION_STATUS";
    private static final String EXTRA_ACTUAL_BACKEND = "ACTUAL_BACKEND";

    private boolean hasConnection;

    private TextView pageLabelTextView;

    private SharedPreferences sharedPreferences;

    public static Intent newIntent(Context packageContext, Boolean hasConnection, String actualBackend) {
        Intent intent = new Intent(packageContext, MainActivity.class);

        intent.putExtra(EXTRA_CONNECTION_STATUS, hasConnection);
        intent.putExtra(EXTRA_ACTUAL_BACKEND, actualBackend);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasConnection = Objects.requireNonNull(getIntent()).getBooleanExtra(EXTRA_CONNECTION_STATUS, false);
        String actualBackend = Objects.requireNonNull(getIntent().getStringExtra(EXTRA_ACTUAL_BACKEND));

        sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        if (hasConnection)
            sendDateRequest(actualBackend);
        else
            setFragments(null, null, null);

        pageLabelTextView = findViewById(R.id.pageLabelTextView);
    }

    private void sendDateRequest(String actualBackend) {
        DateService dateService = DateBuilder.build(actualBackend);

        dateService.getDate().enqueue(new Callback<DateJson>() {
            @Override
            public void onResponse(@NonNull Call<DateJson> call, @NonNull Response<DateJson> response) {
                DateJson dateJson = response.body();
                if (dateJson != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    if(Objects.equals(sharedPreferences.getString("date", ""), dateJson.date))
                    {
                        Gson gson = new Gson();
                        String jsonLoans = sharedPreferences.getString("loans", "");
                        Loan[] loans = gson.fromJson(jsonLoans, Loan[].class);
                        gson = new Gson();
                        String jsonCards = sharedPreferences.getString("cards", "");
                        Cards[] cards = gson.fromJson(jsonCards, Cards[].class);
                        gson = new Gson();
                        String jsonCredits = sharedPreferences.getString("credits", "");
                        Loan[] credits = gson.fromJson(jsonCredits, Loan[].class);

                        setFragments(loans, cards, credits);
                    }
                    else
                    {
                        sharedPreferences.edit().putString("date", dateJson.date).apply();

                        sendDatabaseRequest(actualBackend);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DateJson> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendDatabaseRequest(String actualBackend) {
        DatabaseService databaseService = DatabaseBuilder.build(actualBackend);

        databaseService.getDatabase().enqueue(new Callback<DatabaseJson>() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onResponse(@NonNull Call<DatabaseJson> call, @NonNull Response<DatabaseJson> response) {
                DatabaseJson databaseJson = response.body();
                if (databaseJson != null) {
                    Loan[] loans = databaseJson.loans;
                    Cards[] cards = databaseJson.cards;
                    Loan[] credits = databaseJson.credits;

                    Gson gson = new Gson();
                    ArrayList<Loan> loansArrayList = new ArrayList<>();
                    Collections.addAll(loansArrayList, loans);
                    String jsonLoans = gson.toJson(loansArrayList);
                    sharedPreferences.edit().putString("loans", jsonLoans).apply();
                    gson = new Gson();
                    ArrayList<Cards> cardsArrayList = new ArrayList<>();
                    Collections.addAll(cardsArrayList, cards);
                    String jsonCards = gson.toJson(cardsArrayList);
                    sharedPreferences.edit().putString("cards", jsonCards).apply();
                    gson = new Gson();
                    ArrayList<Loan> creditsArrayList = new ArrayList<>();
                    Collections.addAll(creditsArrayList, credits);
                    String jsonCredits = gson.toJson(creditsArrayList);
                    sharedPreferences.edit().putString("credits", jsonCredits).apply();

                    setFragments(loans, cards, credits);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DatabaseJson> call, @NonNull Throwable t) {
                t.printStackTrace();

                setFragments(null, null, null);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void setFragments(Loan[] loans, Cards[] cards, Loan[] credits) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        LoansFragment loansFragment = LoansFragment.newInstance(hasConnection, loans);
        fragmentManager.beginTransaction().add(R.id.main_container, loansFragment, LOANS_FRAGMENT_TAG).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.loansPage:
                    pageLabelTextView.setText(getString(R.string.loans_page));
                    fragmentManager.beginTransaction().replace(R.id.main_container,
                            LoansFragment.newInstance(hasConnection, loans), LOANS_FRAGMENT_TAG).commit();
                    return true;
                case R.id.cardsPage:
                    pageLabelTextView.setText(getString(R.string.cards_page));
                    fragmentManager.beginTransaction().replace(R.id.main_container,
                            CardsFragment.newInstance(cards), CARDS_FRAGMENT_TAG).commit();
                    return true;
                case R.id.creditsPage:
                    pageLabelTextView.setText(getString(R.string.credits_page));
                    fragmentManager.beginTransaction().replace(R.id.main_container,
                            CreditsFragment.newInstance(credits), CREDITS_FRAGMENT_TAG).commit();
                    return true;
                case R.id.favouritesPage:
                    pageLabelTextView.setText(getString(R.string.favourites_page));
                    fragmentManager.beginTransaction().replace(R.id.main_container,
                            new FavouritesFragment(), FAVOURITES_FRAGMENT_TAG).commit();
                    return true;
                default:
                    return false;
            }
        });
    }
}