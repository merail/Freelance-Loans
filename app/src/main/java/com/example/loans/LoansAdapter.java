package com.example.loans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
    public void onBindViewHolder(@NonNull LoansHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    static class LoansHolder extends RecyclerView.ViewHolder {
        public LoansHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
