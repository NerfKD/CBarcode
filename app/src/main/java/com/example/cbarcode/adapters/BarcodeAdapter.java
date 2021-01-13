package com.example.cbarcode.adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cbarcode.R;
import com.example.cbarcode.models.Barcode;
import java.util.List;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.BarcodeViewHolder> {
    private List<Barcode> mBarcodeList;
    private Context context;

    public BarcodeAdapter(List<Barcode> mBarcodeList, Context context) {
        this.mBarcodeList = mBarcodeList;
        this.context = context;
    }

    @Override
    public BarcodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.barcode_item, parent, false);
        return new BarcodeViewHolder(v);
    }


    public class BarcodeViewHolder extends RecyclerView.ViewHolder {

        public TextView Order,Quantity,Barcode;


        public BarcodeViewHolder(View itemView) {
            super(itemView);

            Order = itemView.findViewById(R.id.txtOrder);
            Quantity = itemView.findViewById(R.id.txtQuantity);
            Barcode = itemView.findViewById(R.id.txtBarcode);
        }

    }

    @Override
    public void onBindViewHolder(final BarcodeViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        Barcode barcode = mBarcodeList.get(position);

        String quantitytest = String.valueOf(barcode.getQuantity());
        String barcodetest = String.valueOf(barcode.getBarcode());
        //String ordertest = String.valueOf(barcode.getOrder());
        String ordertest = String.valueOf(position + 1);

        holder.Quantity.setText(quantitytest);
        holder.Barcode.setText(barcodetest);
        holder.Order.setText(ordertest);


    }

    @Override
    public int getItemCount() {
        return mBarcodeList.size();
    }

}
