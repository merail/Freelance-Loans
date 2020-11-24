package com.onlinecash.loanswithout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class LoansAdapter extends RecyclerView.Adapter<LoansAdapter.LoansHolder> {
    private final Context context;
    private final String order;

    public LoansAdapter(Context context, String order)
    {
        this.context = context;
        this.order = order;
    }


    @NonNull
    @Override
    public LoansHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.loan,parent, false);

        return new LoansHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final LoansHolder holder, int position) {
        holder.detailsImageButton.setOnClickListener(v -> {
            if(holder.detailsConstraintLayout.getVisibility() == View.GONE)
            {
                holder.detailsImageButton.setBackgroundResource(R.drawable.hide_details);
                holder.detailsConstraintLayout.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.detailsImageButton.setBackgroundResource(R.drawable.details);
                holder.detailsConstraintLayout.setVisibility(View.GONE);
            }
        });

        holder.registrationButton.setOnClickListener(view -> context.startActivity(RegistrationActivity.newIntent(context, order)));

        holder.favouriteImageButton.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    static class LoansHolder extends RecyclerView.ViewHolder {
        ImageButton detailsImageButton;
        ConstraintLayout detailsConstraintLayout;
        ImageButton registrationButton;
        ImageButton favouriteImageButton;
        public LoansHolder(@NonNull View itemView) {
            super(itemView);
            detailsImageButton = itemView.findViewById(R.id.detailsImageButton);
            detailsConstraintLayout = itemView.findViewById(R.id.detailsConstraintLayout);
            registrationButton = itemView.findViewById(R.id.registrationImageButton);
            favouriteImageButton = itemView.findViewById(R.id.favouriteImageButton);
        }
    }
}
