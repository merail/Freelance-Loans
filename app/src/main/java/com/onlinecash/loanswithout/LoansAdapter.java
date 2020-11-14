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
    private Context context;

    public LoansAdapter(Context context)
    {
        this.context = context;
    }


    @NonNull
    @Override
    public LoansHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.loan,parent, false);

        return new LoansHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final LoansHolder holder, int position) {
        holder.detailsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        holder.checkoutImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.favouriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    static class LoansHolder extends RecyclerView.ViewHolder {
        ImageButton detailsImageButton;
        ConstraintLayout detailsConstraintLayout;
        ImageButton checkoutImageButton;
        ImageButton favouriteImageButton;
        public LoansHolder(@NonNull View itemView) {
            super(itemView);
            detailsImageButton = itemView.findViewById(R.id.detailsImageButton);
            detailsConstraintLayout = itemView.findViewById(R.id.detailsConstraintLayout);
            checkoutImageButton = itemView.findViewById(R.id.checkoutImageButton);
            favouriteImageButton = itemView.findViewById(R.id.favouriteImageButton);
        }
    }
}
