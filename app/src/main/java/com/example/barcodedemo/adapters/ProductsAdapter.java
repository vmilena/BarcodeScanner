package com.example.barcodedemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodedemo.OnItemClickListener;
import com.example.barcodedemo.R;
import com.example.barcodedemo.api.models.BarcodeModel;
import com.example.barcodedemo.utils.Constants;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    private List<BarcodeModel> productsList;
    private Context context;
    private final OnItemClickListener<BarcodeModel> itemClicked;

    public ProductsAdapter(List<BarcodeModel> productsList, Context context, OnItemClickListener<BarcodeModel> itemClicked) {
        this.productsList = productsList;
        this.context = context;
        this.itemClicked = itemClicked;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductsViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.product_recycler_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        holder.bind(productsList.get(position), itemClicked);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
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

        public void bind(final BarcodeModel product, OnItemClickListener<BarcodeModel> itemClicked) {
            if (product.getImage() != null) {
                Picasso.get().load(product.getImage()).into(productImageView);
            }
            productNameTextView.setText(product.getName() != null ? product.getName() : Constants.NAME_UNAVAILABLE);
            DecimalFormat df = new DecimalFormat("#.##");
            priceTextView.setText(product.getPrice() != 0 ? "$ " + df.format(product.getPrice()) : Constants.UNAVAILABLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    itemClicked.itemClicked(product);
                }
            });
        }
    }
}
