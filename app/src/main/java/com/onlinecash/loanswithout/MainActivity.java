package com.onlinecash.loanswithout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_CONNECTION_STATUS = "CONNECTION_STATUS";
    private final String TAG = "onComplete";

    private final String LOANS_FRAGMENT_TAG = "onComplete";
    private final String CARDS_FRAGMENT_TAG = "onComplete";
    private final String CREDITS_FRAGMENT_TAG = "onComplete";
    private final String FAVOURITES_FRAGMENT_TAG = "onComplete";

    private BottomNavigationView bottomNavigationView;

    private boolean hasConnection;

    public static Intent newIntent(Context packageContext, Boolean hasConnection) {
        Intent intent = new Intent(packageContext, MainActivity.class);

        intent.putExtra(EXTRA_CONNECTION_STATUS, hasConnection);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasConnection = Objects.requireNonNull(getIntent()).getBooleanExtra(EXTRA_CONNECTION_STATUS, false);

        final TextView pageLabelTextView = findViewById(R.id.pageLabelTextView);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment loansFragment = LoansFragment.newInstance(hasConnection, "b");
        final Fragment cardsFragment = CardsFragment.newInstance(hasConnection, "b");
        final Fragment creditsFragment = CreditsFragment.newInstance(hasConnection, "b");
        final Fragment favouritesFragment = FavouritesFragment.newInstance(hasConnection, "b");
        final Fragment[] active = {loansFragment};
        fragmentManager.beginTransaction().add(R.id.main_container, favouritesFragment, FAVOURITES_FRAGMENT_TAG).hide(favouritesFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, creditsFragment, CREDITS_FRAGMENT_TAG).hide(creditsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, cardsFragment, CARDS_FRAGMENT_TAG).hide(cardsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, loansFragment, LOANS_FRAGMENT_TAG).commit();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });

        String simCountryIso = Utils.getSimCountryIso(getApplicationContext());
        String color = Utils.getColor(this);
        String rootState = Utils.getRootState(getApplicationContext());
        String locale = Utils.getLocale();
        String appMetricaAPIKey = Utils.appMetricaAPIKey;
        String androidId = Utils.getAndroidId(getApplicationContext());
        String token = Utils.token[0];
        String googleAdvertisingId = Utils.googleAdvertisingId[0];
        String instanceId = Utils.getInstanceId(getApplicationContext());

        Service service = ServiceBuilder.build();

        service.getJson(simCountryIso, color, rootState, locale, appMetricaAPIKey,
                androidId, token, googleAdvertisingId, instanceId).enqueue(new Callback<Json>() {
            @Override
            public void onResponse(@NonNull Call<Json> call, @NonNull Response<Json> response) {
                Json json = response.body();
                if (json != null) {
                    Log.d("aaaaaaaaaa", json.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Json> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}