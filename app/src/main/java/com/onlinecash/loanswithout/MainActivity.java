package com.onlinecash.loanswithout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.facebook.FacebookSdk;
import com.facebook.applinks.AppLinkData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import static com.onlinecash.loanswithout.Utils.CARDS_FRAGMENT_TAG;
import static com.onlinecash.loanswithout.Utils.CREDITS_FRAGMENT_TAG;
import static com.onlinecash.loanswithout.Utils.FAVOURITES_FRAGMENT_TAG;
import static com.onlinecash.loanswithout.Utils.LOANS_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_TERM = "user_term_html";
    private static final String EXTRA_PAGE = "page";
    private static final String EXTRA_TAB = "page";
    private static final String EXTRA_LOANS = "loans";
    private static final String EXTRA_CARDS = "cards";
    private static final String EXTRA_CREDITS = "credits";

    private int page = 0;
    private int tab = 0;

    private ImageButton informationImageButton;
    private TextView pageLabelTextView;
    private ProgressBar mainProgressBar;
    private BottomNavigationView bottomNavigationView;

    public static Intent newIntent(Context packageContext, String user_term_html, int page, int tab) {
        Intent intent = new Intent(packageContext, MainActivity.class);

        intent.putExtra(EXTRA_TERM, user_term_html);
        intent.putExtra(EXTRA_PAGE, page);
        intent.putExtra(EXTRA_TAB, tab);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        informationImageButton = findViewById(R.id.informationImageButton);
        mainProgressBar = findViewById(R.id.mainProgressBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);

        String user_term_html = getIntent().getStringExtra(EXTRA_TERM);
        if (user_term_html != null) {
            informationImageButton.setVisibility(View.VISIBLE);
            setInformationWindow(user_term_html);
        }

        page = getIntent().getIntExtra(EXTRA_PAGE, 0);
        tab = getIntent().getIntExtra(EXTRA_TAB, 0);

        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String jsonLoans = sharedPreferences.getString("loans", "");
        Loan[] loans = gson.fromJson(jsonLoans, Loan[].class);
        gson = new Gson();
        String jsonCards = sharedPreferences.getString("cards", "");
        Cards[] cards = gson.fromJson(jsonCards, Cards[].class);
        gson = new Gson();
        String jsonCredits = sharedPreferences.getString("credits", "");
        Loan[] credits = gson.fromJson(jsonCredits, Loan[].class);

        pageLabelTextView = findViewById(R.id.pageLabelTextView);

        setFragments(loans, cards, credits);
    }


    @SuppressLint("NonConstantResourceId")
    public void setFragments(Loan[] loans, Cards[] cards, Loan[] credits) {
        mainProgressBar.setVisibility(View.GONE);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        LoansFragment loansFragment = LoansFragment.newInstance(loans);
        fragmentManager.beginTransaction().add(R.id.main_container, loansFragment, LOANS_FRAGMENT_TAG).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.loansPage:
                    pageLabelTextView.setText(getString(R.string.loans_page));
                    fragmentManager.beginTransaction().replace(R.id.main_container,
                            LoansFragment.newInstance(loans), LOANS_FRAGMENT_TAG).commit();
                    return true;
                case R.id.cardsPage:
                    pageLabelTextView.setText(getString(R.string.cards_page));
                    fragmentManager.beginTransaction().replace(R.id.main_container,
                            CardsFragment.newInstance(cards, tab), CARDS_FRAGMENT_TAG).commit();
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

        if (page == 0)
            bottomNavigationView.setSelectedItemId(R.id.loansPage);
        else if (page == 1)
            bottomNavigationView.setSelectedItemId(R.id.cardsPage);
        else if (page == 2)
            bottomNavigationView.setSelectedItemId(R.id.creditsPage);
        else
            bottomNavigationView.setSelectedItemId(R.id.favouritesPage);
    }

    private void setInformationWindow(String user_term_html) {
        informationImageButton.setOnClickListener(view ->
        {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);

            @SuppressLint("InflateParams") PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.window_information,
                    null, false), (int) (size.x * 0.95), (int) (size.y * 0.65), true);

            TextView informationTextView = pw.getContentView().findViewById(R.id.informationTextView);
            informationTextView.setText(Html.fromHtml(user_term_html));
            informationTextView.setMovementMethod(new ScrollingMovementMethod());

            ImageButton okInformationImageButton = pw.getContentView().findViewById(R.id.okInformationImageButton);
            okInformationImageButton.setOnClickListener(view1 -> pw.dismiss());

            pw.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }
}