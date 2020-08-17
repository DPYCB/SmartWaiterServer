package com.dpycb.smartwaiterserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.dpycb.smartwaiterserver.model.Common;
import com.dpycb.smartwaiterserver.viewholder.OrderDetailAdapter;

public class OrderDetailActivity extends AppCompatActivity {
    TextView orderName;
    TextView orderTable;
    TextView orderStatus;
    TextView orderComment;

    String orderID = "";

    RecyclerView recyclerOrderFoods;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderName = findViewById(R.id.orderName);
        orderTable = findViewById(R.id.orderTable);
        orderStatus = findViewById(R.id.orderStatus);
        orderComment = findViewById(R.id.orderComment);

        recyclerOrderFoods = findViewById(R.id.recyclerOrderFoods);
        recyclerOrderFoods.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerOrderFoods.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            orderID = getIntent().getStringExtra("OrderID");
        }

        orderName.setText(orderID);
        orderTable.setText(Common.currentRequest.getTableName());
        orderStatus.setText(Common.currentRequest.getStatus());
        orderComment.setText(Common.currentRequest.getComment());

        OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentRequest.getFoods());
        adapter.notifyDataSetChanged();
        recyclerOrderFoods.setAdapter(adapter);
    }
}
