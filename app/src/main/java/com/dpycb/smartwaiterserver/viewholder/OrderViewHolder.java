package com.dpycb.smartwaiterserver.viewholder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dpycb.smartwaiterserver.R;

import info.hoang8f.widget.FButton;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    public TextView orderName;
    public TextView orderTable;
    public TextView orderStatus;

    public FButton btnEdit;
    public FButton btnRemove;
    public FButton btnDetails;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        orderName = itemView.findViewById(R.id.orderName);
        orderTable = itemView.findViewById(R.id.orderTable);
        orderStatus = itemView.findViewById(R.id.orderStatus);

        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnRemove = itemView.findViewById(R.id.btnRemove);
        btnDetails = itemView.findViewById(R.id.btnDetails);
    }
}
