package com.kumar.prince.foodneturationchecker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.communication.FoodCheckerSearch;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerProduct;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import timber.log.Timber;

/**
 * Created by prince on 15/9/17.
 */

public class FoodCheckerFoodListAdapter extends RecyclerView.Adapter<FoodCheckerFoodListAdapter.FC_FoodListViewHolder> implements Serializable {
    final private FC_FoodOnClickHandler fc_foodOnClickHandler;
    FoodCheckerSearch foodChecker_search;
    List<FoodCheckerProduct> foodChecker_productList;
    FoodCheckerProduct foodChecker_product;
    Context context;

    public FoodCheckerFoodListAdapter(FC_FoodOnClickHandler fc_foodOnClickHandler) {
        this.fc_foodOnClickHandler = fc_foodOnClickHandler;
    }

    @Override
    public FC_FoodListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.product_card_view, parent, false);
        // ButterKnife.bind(view);
        return new FC_FoodListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FC_FoodListViewHolder holder, int position) {

        FoodCheckerProduct foodChecker_product = foodChecker_productList.get(position);
        holder.tv_food_product_name.setText(foodChecker_product.getmGenericName());
        Picasso.with(context).load(foodChecker_product.getmImageFrontUrl()).into(holder.foodImageView);
        holder.foodChecker_productdata = foodChecker_product;
    }

    @Override
    public int getItemCount() {
        if (foodChecker_productList == null)
            return 0;
        return foodChecker_productList.size();
    }


    public void setEventData(List<FoodCheckerProduct> foodChecker_productList) {
        this.foodChecker_productList = foodChecker_productList;
        notifyDataSetChanged();
    }

    public interface FC_FoodOnClickHandler {
        void onClick(FoodCheckerProduct foodChecker_product);
    }


    public class FC_FoodListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // @BindView(R.id.iv_product_image)
        ImageView foodImageView;
        // @BindView(R.id.tv_food_product_name)
        TextView tv_food_product_name;
        FoodCheckerProduct foodChecker_productdata;

        public FC_FoodListViewHolder(View itemView) {
            super(itemView);
            foodImageView = (ImageView) itemView.findViewById(R.id.iv_product_image);
            tv_food_product_name = (TextView) itemView.findViewById(R.id.tv_food_product_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Timber.d("Click Button" + foodChecker_productdata.getmBarcode());
            fc_foodOnClickHandler.onClick(foodChecker_productdata);

        }
    }

}
