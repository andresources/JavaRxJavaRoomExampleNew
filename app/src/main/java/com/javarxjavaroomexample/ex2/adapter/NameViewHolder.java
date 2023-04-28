package com.javarxjavaroomexample.ex2.adapter;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.javarxjavaroomexample.R;

public class NameViewHolder extends RecyclerView.ViewHolder {

    private TextView tvName;

    public NameViewHolder(@NonNull View view) {
        super(view);
        this.tvName = view.findViewById(R.id.tvName);
    }

    public void setName(@NonNull String name) {
        tvName.setText(name);
    }

    public String getName() {
        return tvName.getText().toString();
    }
}
