package com.dpycb.smartwaiterserver.viewholder;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dpycb.smartwaiterserver.R;
import com.dpycb.smartwaiterserver.model.Order;

import java.util.List;

class OrderDetailViewHolder extends RecyclerView.ViewHolder {
    public TextView foodName;
    public TextView foodQuantity;
    public TextView foodPrice;

    public OrderDetailViewHolder(@NonNull View itemView) {
        super(itemView);

        foodName = itemView.findViewById(R.id.foodName);
        foodQuantity = itemView.findViewById(R.id.foodQuantity);
        foodPrice = itemView.findViewById(R.id.foodPrice);
    }
}

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailViewHolder> {

    List<Order> orders;

    public OrderDetailAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_layout, parent, false);
        return new OrderDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.foodName.setText(String.format("Блюдо: %s", order.getFoodName()));
        holder.foodQuantity.setText(String.format("Количество: %s", order.getQuantity()));
        holder.foodPrice.setText(String.format("Стоимость: %s", order.getPrice()));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
