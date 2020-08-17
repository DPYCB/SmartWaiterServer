package com.dpycb.smartwaiterserver;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dpycb.smartwaiterserver.model.Common;
import com.dpycb.smartwaiterserver.model.Request;
import com.dpycb.smartwaiterserver.viewholder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class OrderStatusActivity extends AppCompatActivity {
    MaterialSpinner statusSpinner;

    RecyclerView recyclerOrder;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Init Firebase
        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");

        recyclerOrder = findViewById(R.id.recyclerOrder);
        recyclerOrder.setHasFixedSize(true);
        recyclerOrder.setItemViewCacheSize(5);
        layoutManager = new LinearLayoutManager(this);
        recyclerOrder.setLayoutManager(layoutManager);

        loadOrders();
    }

    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class, R.layout.order_layout,
                OrderViewHolder.class, requests) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, final Request request, final int i) {
                orderViewHolder.orderName.setText(adapter.getRef(i).getKey());
                orderViewHolder.orderTable.setText(request.getTableName());
                orderViewHolder.orderStatus.setText(Common.convertCodeToStatus(request.getStatus()));

                orderViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openEditDialog(adapter.getRef(i).getKey(), adapter.getItem(i));
                    }
                });

                orderViewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openRemoveDialog(adapter.getRef(i).getKey());
                    }
                });

                orderViewHolder.btnDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(OrderStatusActivity.this, OrderDetailActivity.class);
                        Common.currentRequest = request;
                        intent.putExtra("OrderID", adapter.getRef(i).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerOrder.setAdapter(adapter);
    }

    private void openRemoveDialog(String key) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Удалить заказ?");
        alertDialog.setMessage("Уверены, что хотите удалить данный заказ? Гости могут быть недовольны...");

        final String localKey = key;
        alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requests.child(localKey).removeValue();
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void openEditDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Статус заказа");
        alertDialog.setMessage("Пожалуйста, выберите статус заказа");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.edit_order_item, null);
        statusSpinner = findViewById(R.id.statusSpinner);
        statusSpinner.setItems("Готовится", "Завершен");

        alertDialog.setView(view);
        final String localKey = key;
        alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item.setStatus(String.valueOf(statusSpinner.getSelectedIndex()));
                adapter.notifyDataSetChanged();
                requests.child(localKey).setValue(item);
            }
        }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}
