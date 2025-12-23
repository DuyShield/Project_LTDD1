package com.example.do_anltdd1.UI_WareHouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.do_anltdd1.R;

import java.util.ArrayList;
import java.util.List;
import com.example.do_anltdd1.Database.Warehouse;

public class WarehouseAdapter extends ArrayAdapter<Warehouse> {

    private Context context;
    private List<Warehouse> list;

    public WarehouseAdapter(Context context, ArrayList<Warehouse> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_show_warehouse, parent, false);
        }

        Warehouse wh = list.get(position);

        TextView name = convertView.findViewById(R.id.txtWarehouseName);
        TextView quantity = convertView.findViewById(R.id.txtSoLuong);
        TextView status = convertView.findViewById(R.id.txtTrangThai);

        Button btnEdit = convertView.findViewById(R.id.btnEdit);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        Button btnDetail = convertView.findViewById(R.id.btnDetail);

        name.setText(wh.getName());
        quantity.setText(String.valueOf(wh.getQuantity()));
        status.setText(wh.getStatus());
        // Xử lý các nút
        btnEdit.setOnClickListener(v -> {
            ((Formwarehouse) context).openUpdateForm(wh);
        });

        btnDelete.setOnClickListener(v -> {
            ((Formwarehouse) context).deleteWarehouse(wh);
        });

        btnDetail.setOnClickListener(v -> {
            ((Formwarehouse) context).showDetail(wh);
        });
        return convertView;


    }
}

