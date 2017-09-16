package com.kumar.prince.foodneturationchecker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.communication.FC_Search;
import com.kumar.prince.foodneturationchecker.data.model.FC_Event;
import com.kumar.prince.foodneturationchecker.data.model.FC_Product;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by prince on 15/9/17.
 */

public class FC_FoodListAdapter extends RecyclerView.Adapter<FC_FoodListAdapter.FC_FoodListViewHolder> implements Serializable{
    FC_Search fc_search;
    List<FC_Product> fc_productList;
    FC_Product fc_product;
    final private FC_FoodOnClickHandler fc_foodOnClickHandler;

    Context context;

    public FC_FoodListAdapter(FC_FoodOnClickHandler fc_foodOnClickHandler){
        this.fc_foodOnClickHandler=fc_foodOnClickHandler;
    }

    @Override
    public FC_FoodListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.product_card_view,parent,false);
       // ButterKnife.bind(view);
        return new FC_FoodListViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(FC_FoodListViewHolder holder, int position) {

        FC_Product fc_product = fc_productList.get(position);
        holder.tv_food_product_name.setText(fc_product.getmGenericName());
        Picasso.with(context).load(fc_product.getmImageFrontUrl()).into(holder.foodImageView);
        holder.fc_productdata=fc_product;
    }

    @Override
    public int getItemCount() {
        if (fc_productList==null)
            return 0;
        return fc_productList.size();
    }


    public void setEventData(List<FC_Product> fc_productList){
        this.fc_productList=fc_productList;
        notifyDataSetChanged();
    }

    public interface  FC_FoodOnClickHandler{
        void onClick( FC_Product fc_product);
    }



    public class FC_FoodListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       // @BindView(R.id.iv_product_image)
                ImageView foodImageView;
       // @BindView(R.id.tv_food_product_name)
                TextView tv_food_product_name;
        FC_Product fc_productdata;

        public FC_FoodListViewHolder(View itemView) {
            super(itemView);
            foodImageView =(ImageView) itemView.findViewById(R.id.iv_product_image);
            tv_food_product_name=(TextView) itemView.findViewById(R.id.tv_food_product_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Timber.d("Click Button"+fc_productdata.getmBarcode());
            fc_foodOnClickHandler.onClick(fc_productdata);

        }
    }

}
