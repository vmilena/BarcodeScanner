package com.example.barcodedemo.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodedemo.R;
import com.example.barcodedemo.api.models.BarcodeModel;
import com.example.barcodedemo.utils.Constants;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.productNameTextView)
        TextView productNameTextView;
        @BindView(R.id.productImageView)
        ImageView productImageView;
        @BindView(R.id.priceTextView)
        TextView priceTextView;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final BarcodeModel product, int position) {
            Picasso.get().load(product.getImage()).into(productImageView);
            productNameTextView.setText(product.getName());
            DecimalFormat df = new DecimalFormat("#.##");
            priceTextView.setText(product.getPrice() != 0 ? "$" + df.format(product.getPrice()) : Constants.UNAVAILABLE);
        }
    }
}
