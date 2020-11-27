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
    private OnFavouriteClick onFavouriteClick;

    public LoansAdapter(Context context, ArrayList<Loan> loans, OnFavouriteClick onFavouriteClick) {
        this.context = context;
        this.loans = loans;
        this.onFavouriteClick = onFavouriteClick;

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
            holder.prefixRateTextView.setText(loans.get(position).percentPrefix);
            holder.rateTextView.setText(loans.get(position).percent);
            holder.postfixRateTextView.setText(loans.get(position).percentPostfix);
        } else {
            holder.rateLabelTextView.setVisibility(View.INVISIBLE);
        }

        holder.prefixSumTextView.setText(loans.get(position).summPrefix);
        holder.postfixSumTextView.setText(loans.get(position).summPostfix);
        holder.minSumTextView.setText(loans.get(position).summMin);
        holder.midSumTextView.setText(loans.get(position).summMid);
        holder.maxSumTextView.setText(loans.get(position).summMax);

        if (loans.get(position).hide_TermFields == 0) {
            holder.timeLabelTextView.setVisibility(View.VISIBLE);
            holder.prefixTermTextView.setText(loans.get(position).termPrefix);
            holder.postfixTermTextView.setText(loans.get(position).termPostfix);
            holder.minTermTextView.setText(loans.get(position).termMin);
            holder.midTermTextView.setText(loans.get(position).termMid);
            holder.maxTermTextView.setText(loans.get(position).termMax);
        } else {
            holder.timeLabelTextView.setVisibility(View.INVISIBLE);
        }

        if(loans.get(position).show_visa == 0)
            holder.visaImageView.setVisibility(View.GONE);
        if(loans.get(position).show_mastercard == 0)
            holder.mastercardImageView.setVisibility(View.GONE);
        if(loans.get(position).show_mir == 0)
            holder.mirImageView.setVisibility(View.GONE);
        if(loans.get(position).show_yandex == 0)
            holder.yandexImageView.setVisibility(View.GONE);
        if(loans.get(position).show_qiwi == 0)
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
                .newIntent(context, loans.get(position).order)));

        if(sharedPreferences.contains("favourite:" + loans.get(position).name))
        {
            holder.favouriteImageButton.setBackgroundResource(R.drawable.favourite_selected);
        }
        holder.favouriteImageButton.setOnClickListener(view -> {
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

            if(sharedPreferences.contains("favourite:" + loans.get(position).name))
            {
                prefsEditor.remove("favourite:" + loans.get(position).name).apply();

                if(onFavouriteClick == null)
                {
                    loans.remove(position);
                    notifyDataSetChanged();
                }
                else
                {
                    onFavouriteClick.onFavouriteClick(false, loans.get(position));
                    holder.favouriteImageButton.setBackgroundResource(R.drawable.favourite);
                }
            }
            else {
                onFavouriteClick.onFavouriteClick(true, loans.get(position));
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
        TextView prefixRateTextView;
        TextView rateTextView;
        TextView postfixRateTextView;
        TextView prefixSumTextView;
        TextView minSumTextView;
        TextView midSumTextView;
        TextView maxSumTextView;
        TextView timeLabelTextView;
        TextView postfixSumTextView;
        TextView prefixTermTextView;
        TextView minTermTextView;
        TextView midTermTextView;
        TextView maxTermTextView;
        TextView postfixTermTextView;
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
            prefixRateTextView = itemView.findViewById(R.id.prefixRateTextView);
            rateTextView = itemView.findViewById(R.id.rateTextView);
            postfixRateTextView = itemView.findViewById(R.id.postfixRateTextView);
            prefixSumTextView = itemView.findViewById(R.id.prefixSumTextView);
            minSumTextView = itemView.findViewById(R.id.minSumTextView);
            midSumTextView = itemView.findViewById(R.id.midSumTextView);
            maxSumTextView = itemView.findViewById(R.id.maxSumTextView);
            timeLabelTextView = itemView.findViewById(R.id.timeLabelTextView);
            postfixSumTextView = itemView.findViewById(R.id.postfixSumTextView);
            prefixTermTextView = itemView.findViewById(R.id.prefixTermTextView);
            minTermTextView = itemView.findViewById(R.id.minTermTextView);
            midTermTextView = itemView.findViewById(R.id.midTermTextView);
            maxTermTextView = itemView.findViewById(R.id.maxTermTextView);
            postfixTermTextView = itemView.findViewById(R.id.postfixTermTextView);
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
