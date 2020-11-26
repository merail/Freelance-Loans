package com.onlinecash.loanswithout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        sendDateRequest(actualBackend);

        pageLabelTextView = findViewById(R.id.pageLabelTextView);
    }

    private void sendDateRequest(String actualBackend)
    {
        DateService dateService = DateBuilder.build(actualBackend);

        dateService.getDate().enqueue(new Callback<DateJson>() {
            @Override
            public void onResponse(@NonNull Call<DateJson> call, @NonNull Response<DateJson> response) {
                DateJson dateJson = response.body();
                if (dateJson != null) {
//                    SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
//                    if(Objects.equals(sharedPreferences.getString("date", ""), dateJson.date))
//                    {
//
//                    }
//                    else
//                    {
//                        sharedPreferences.edit().putString("date", dateJson.date).apply();

                        sendDatabaseRequest(actualBackend);
                    //}
                }
            }

            @Override
            public void onFailure(@NonNull Call<DateJson> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendDatabaseRequest(String actualBackend)
    {
        DatabaseService databaseService = DatabaseBuilder.build(actualBackend);

        databaseService.getDatabase().enqueue(new Callback<DatabaseJson>() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onResponse(@NonNull Call<DatabaseJson> call, @NonNull Response<DatabaseJson> response) {
                DatabaseJson databaseJson = response.body();
                if (databaseJson != null) {
                    Loan[] loans = databaseJson.loans;
                    Cards[] cards = databaseJson.cards;

                    Log.d("aaaaaaaaaaaa", call.request().url().toString());

                    final FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment loansFragment = LoansFragment.newInstance(hasConnection, loans);
                    final Fragment cardsFragment = CardsFragment.newInstance(hasConnection, "b");
                    final Fragment creditsFragment = CreditsFragment.newInstance(hasConnection, "b");
                    final Fragment favouritesFragment = FavouritesFragment.newInstance(hasConnection, "b");
                    final Fragment[] active = {loansFragment};
                    fragmentManager.beginTransaction().add(R.id.main_container, favouritesFragment, FAVOURITES_FRAGMENT_TAG).hide(favouritesFragment).commit();
                    fragmentManager.beginTransaction().add(R.id.main_container, creditsFragment, CREDITS_FRAGMENT_TAG).hide(creditsFragment).commit();
                    fragmentManager.beginTransaction().add(R.id.main_container, cardsFragment, CARDS_FRAGMENT_TAG).hide(cardsFragment).commit();
                    fragmentManager.beginTransaction().add(R.id.main_container, loansFragment, LOANS_FRAGMENT_TAG).commit();

                    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

                    bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.loansPage:
                                pageLabelTextView.setText(getString(R.string.loans_page));
                                fragmentManager.beginTransaction().hide(active[0]).show(loansFragment).commit();
                                active[0] = loansFragment;

                                return true;
                            case R.id.cardsPage:
                                pageLabelTextView.setText(getString(R.string.cards_page));
                                fragmentManager.beginTransaction().hide(active[0]).show(cardsFragment).commit();
                                active[0] = cardsFragment;
                                return true;
                            case R.id.creditsPage:
                                pageLabelTextView.setText(getString(R.string.credits_page));
                                fragmentManager.beginTransaction().hide(active[0]).show(creditsFragment).commit();
                                active[0] = creditsFragment;
                                return true;
                            case R.id.favouritesPage:
                                pageLabelTextView.setText(getString(R.string.favourites_page));
                                fragmentManager.beginTransaction().hide(active[0]).show(favouritesFragment).commit();
                                active[0] = favouritesFragment;
                                return true;
                            default:
                                return false;
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<DatabaseJson> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}