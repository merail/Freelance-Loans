package com.example.loans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

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

        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_PHONE_STATE"}, 100);

        System.out.println("SimCountryIso: " + Utils.getSimCountryIso(getApplicationContext()));
        System.out.println("Color: " + Utils.getColor(this));
        System.out.println("RootState: " + Utils.getRootState(getApplicationContext()));
        System.out.println("Locale: " + Utils.getLocale());
        System.out.println("AppMetricaAPIKey: " + Utils.appMetricaAPIKey);
        System.out.println("AndroidId: " + Utils.getAndroidId(getApplicationContext()));
        System.out.println("Token: " + Utils.token[0]);
        System.out.println("GoogleAdvertisingId: " + Utils.googleAdvertisingId[0]);
        System.out.println("InstanceId: " + Utils.getInstanceId(getApplicationContext()));
    }
}