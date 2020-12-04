package com.onlinecash.loanswithout;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class LoansAdapter extends RecyclerView.Adapter<LoansAdapter.LoansHolder> {
    private final Context context;
    private final ArrayList<Loan> loans;
    private final SharedPreferences sharedPreferences;
    private final boolean isFavouriteFragment;
    private final OnLastFavouriteRemoveListener onLastFavouriteRemoveListener;

    public LoansAdapter(Context context, ArrayList<Loan> loans, boolean isFavouriteFragment
            , OnLastFavouriteRemoveListener onLastFavouriteRemoveListener) {
        this.context = context;
        this.loans = loans;
        this.isFavouriteFragment = isFavouriteFragment;
        this.onLastFavouriteRemoveListener = onLastFavouriteRemoveListener;

        sharedPreferences = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
    }


    @NonNull
    @Override
    public LoansHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.loan, parent, false);

        return new LoansHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final LoansHolder holder, int position) {
        holder.bankNameTextView.setText(loans.get(position).name);

        holder.markTextView.setText(String.valueOf(loans.get(position).score));

        if (loans.get(position).hide_PercentFields == 0) {
            holder.rateLabelTextView.setVisibility(View.VISIBLE);
            String rate = loans.get(position).percentPrefix + " " + loans.get(position).percent + loans.get(position).percentPostfix;
            holder.rateTextView.setText(rate);
        } else {
            holder.rateLabelTextView.setVisibility(View.INVISIBLE);
        }

        String summ = loans.get(position).summPrefix + " " + loans.get(position).summMin + " " + loans.get(position).summMid
                + " " + loans.get(position).summMax + " " + loans.get(position).summPostfix;
        holder.sumTextView.setText(summ);

        if (loans.get(position).hide_TermFields == 0) {
            holder.timeLabelTextView.setVisibility(View.VISIBLE);

            String term = loans.get(position).termPrefix + " " + loans.get(position).termMin + " " + loans.get(position).termMid
                    + " " + loans.get(position).termMax + " " + loans.get(position).termPostfix;
            holder.termTextView.setText(term);
        } else {
            holder.timeLabelTextView.setVisibility(View.INVISIBLE);
        }

        if (loans.get(position).show_visa == 0)
            holder.visaImageView.setVisibility(View.GONE);
        if (loans.get(position).show_mastercard == 0)
            holder.mastercardImageView.setVisibility(View.GONE);
        if (loans.get(position).show_mir == 0)
            holder.mirImageView.setVisibility(View.GONE);
        if (loans.get(position).show_yandex == 0)
            holder.yandexImageView.setVisibility(View.GONE);
        if (loans.get(position).show_qiwi == 0)
            holder.qiwiImageView.setVisibility(View.GONE);

        Glide.with(context)
                .load(loans.get(position).screen)
                .into(holder.bankLogoImageView);
        holder.bankLogoImageView.setOnClickListener(v ->
        {
            if (holder.detailsConstraintLayout.getVisibility() == View.GONE) {
                holder.detailsImageButton.setBackgroundResource(R.drawable.hide_details);
                holder.detailsConstraintLayout.setVisibility(View.VISIBLE);
            } else {
                holder.detailsImageButton.setBackgroundResource(R.drawable.details);
                holder.detailsConstraintLayout.setVisibility(View.GONE);
            }
        });

        holder.detailsImageButton.setOnClickListener(v -> {
            if (holder.detailsConstraintLayout.getVisibility() == View.GONE) {
                holder.detailsImageButton.setBackgroundResource(R.drawable.hide_details);
                holder.detailsConstraintLayout.setVisibility(View.VISIBLE);
            } else {
                holder.detailsImageButton.setBackgroundResource(R.drawable.details);
                holder.detailsConstraintLayout.setVisibility(View.GONE);
            }
        });

        holder.documentsTextView.setText(Html.fromHtml(loans.get(position).description));

        holder.registrationImageButton.setOnClickListener(view -> context.startActivity(RegistrationActivity
                .newIntent(context, loans.get(position).order, loans.get(position).itemId, "offerwall")));

        if (sharedPreferences.contains("favourite:" + loans.get(position).name)) {
            holder.favouriteImageButton.setBackgroundResource(R.drawable.favourite_selected);
        }
        holder.favouriteImageButton.setOnClickListener(view -> {
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

            if (sharedPreferences.contains("favourite:" + loans.get(position).name)) {
                prefsEditor.remove("favourite:" + loans.get(position).name).apply();

                if (isFavouriteFragment) {
                    loans.remove(position);
                    if(loans.isEmpty())
                        onLastFavouriteRemoveListener.onLastFavouriteRemove();
                    notifyDataSetChanged();
                } else
                    holder.favouriteImageButton.setBackgroundResource(R.drawable.favourite);
            } else {
                holder.favouriteImageButton.setBackgroundResource(R.drawable.favourite_selected);
                Gson gson = new Gson();
                String json = gson.toJson(loans.get(position));
                prefsEditor.putString("favourite:" + loans.get(position).name, json);
                prefsEditor.apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return loans.size();
    }

    static class LoansHolder extends RecyclerView.ViewHolder {
        TextView bankNameTextView;
        TextView markTextView;
        TextView rateLabelTextView;
        TextView rateTextView;
        TextView sumTextView;
        TextView timeLabelTextView;
        TextView termTextView;
        ImageView visaImageView;
        ImageView mastercardImageView;
        ImageView mirImageView;
        ImageView yandexImageView;
        ImageView qiwiImageView;
        ImageView bankLogoImageView;
        ConstraintLayout detailsConstraintLayout;
        ImageButton detailsImageButton;
        TextView documentsTextView;
        ImageButton registrationImageButton;
        ImageButton favouriteImageButton;

        public LoansHolder(@NonNull View itemView) {
            super(itemView);

            bankNameTextView = itemView.findViewById(R.id.bankNameTextView);
            markTextView = itemView.findViewById(R.id.markTextView);
            rateLabelTextView = itemView.findViewById(R.id.rateLabelTextView);
            rateTextView = itemView.findViewById(R.id.rateTextView);
            sumTextView = itemView.findViewById(R.id.sumTextView);
            timeLabelTextView = itemView.findViewById(R.id.timeLabelTextView);
            termTextView = itemView.findViewById(R.id.termTextView);
            visaImageView = itemView.findViewById(R.id.visaImageView);
            mastercardImageView = itemView.findViewById(R.id.mastercardImageView);
            mirImageView = itemView.findViewById(R.id.mirImageView);
            yandexImageView = itemView.findViewById(R.id.yandexImageView);
            qiwiImageView = itemView.findViewById(R.id.qiwiImageView);
            bankLogoImageView = itemView.findViewById(R.id.bankLogoImageView);
            detailsConstraintLayout = itemView.findViewById(R.id.detailsConstraintLayout);
            detailsImageButton = itemView.findViewById(R.id.detailsImageButton);
            documentsTextView = itemView.findViewById(R.id.documentsTextView);
            registrationImageButton = itemView.findViewById(R.id.registrationImageButton);
            favouriteImageButton = itemView.findViewById(R.id.favouriteImageButton);
        }
    }
}
