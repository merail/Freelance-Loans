package com.onlinecash.loanswithout;

import android.content.Context;
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

public class LoansAdapter extends RecyclerView.Adapter<LoansAdapter.LoansHolder> {
    private final Context context;
    private final Loan[] loans;

    public LoansAdapter(Context context, Loan[] loans) {
        this.context = context;
        this.loans = loans;
    }


    @NonNull
    @Override
    public LoansHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.loan, parent, false);

        return new LoansHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final LoansHolder holder, int position) {
        holder.bankNameTextView.setText(loans[position].name);

        holder.markTextView.setText(String.valueOf(loans[position].score));

        if (loans[position].hide_PercentFields == 0) {
            holder.rateLabelTextView.setVisibility(View.VISIBLE);
            holder.prefixRateTextView.setText(loans[position].percentPrefix);
            holder.rateTextView.setText(loans[position].percent);
            holder.postfixRateTextView.setText(loans[position].percentPostfix);
        } else {
            holder.rateLabelTextView.setVisibility(View.INVISIBLE);
        }

        holder.prefixSumTextView.setText(loans[position].summPrefix);
        holder.postfixSumTextView.setText(loans[position].summPostfix);
        holder.minSumTextView.setText(loans[position].summMin);
        holder.midSumTextView.setText(loans[position].summMid);
        holder.maxSumTextView.setText(loans[position].summMax);

        if (loans[position].hide_TermFields == 0) {
            holder.timeLabelTextView.setVisibility(View.VISIBLE);
            holder.prefixTermTextView.setText(loans[position].termPrefix);
            holder.postfixTermTextView.setText(loans[position].termPostfix);
            holder.minTermTextView.setText(loans[position].termMin);
            holder.midTermTextView.setText(loans[position].termMid);
            holder.maxTermTextView.setText(loans[position].termMax);
        } else {
            holder.timeLabelTextView.setVisibility(View.INVISIBLE);
        }

        if(loans[position].show_visa == 0)
            holder.visaImageView.setVisibility(View.GONE);
        if(loans[position].show_mastercard == 0)
            holder.mastercardImageView.setVisibility(View.GONE);
        if(loans[position].show_mir == 0)
            holder.mirImageView.setVisibility(View.GONE);
        if(loans[position].show_yandex == 0)
            holder.yandexImageView.setVisibility(View.GONE);
        if(loans[position].show_qiwi == 0)
            holder.qiwiImageView.setVisibility(View.GONE);

        Glide.with(context)
                .load(loans[position].screen)
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

        holder.documentsTextView.setText(Html.fromHtml(loans[position].description));

        holder.registrationImageButton.setOnClickListener(view -> context.startActivity(RegistrationActivity
                .newIntent(context, loans[position].order)));

        holder.favouriteImageButton.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return loans.length;
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
