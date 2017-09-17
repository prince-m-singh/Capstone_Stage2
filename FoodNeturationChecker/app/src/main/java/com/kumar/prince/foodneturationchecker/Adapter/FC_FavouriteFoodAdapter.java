package com.kumar.prince.foodneturationchecker.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kumar.prince.fabfoodlibrary.FabFoodEntity;
import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.data.model.FC_Event;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_NOT_IN_OFF_DATABASE;
import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_OK;
import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_SERVER_UNREACHABLE;

/**
 * Created by prince on 17/9/17.
 */

public class FC_FavouriteFoodAdapter extends RecyclerView.Adapter<FC_FavouriteFoodAdapter.FavouriteFoodViewHolder>implements Serializable {

    List<FabFoodEntity> favouriteFoodData;
    final private FC_FavouriteFoodAdapter.FavouriteFoodOnClickHandler favouriteFoodOnClickHandler;
    Context context;

    public FC_FavouriteFoodAdapter(FC_FavouriteFoodAdapter.FavouriteFoodOnClickHandler favouriteFoodOnClickHandler) {
        this.favouriteFoodOnClickHandler = favouriteFoodOnClickHandler;
    }


    @Override
    public FavouriteFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view =LayoutInflater.from(context).inflate(R.layout.card_view,parent,false);

        return new FC_FavouriteFoodAdapter.FavouriteFoodViewHolder(view) ;

    }

    @Override
    public void onBindViewHolder(FavouriteFoodViewHolder holder, int position) {
        FabFoodEntity fabFoodEntity=favouriteFoodData.get(position);
        if (fabFoodEntity.getMGenericName().length()>30)
            holder.textView.setText("Name:-"+fabFoodEntity.getMGenericName().substring(0,30));
        else
            holder.textView.setText("Name:-"+fabFoodEntity.getMGenericName());
        holder.textView1.setText("BarCode:-"+fabFoodEntity.getMBarcode());
        holder.textView2.setText("Grade:-  "+fabFoodEntity.getMNutritionGrades().toUpperCase());
        Picasso.with(context).load(fabFoodEntity.getMImageFrontSmallUrl()).into(holder.imageView);
        holder.fabFoodEntity=fabFoodEntity;

    }

    @Override
    public int getItemCount() {
        if (favouriteFoodData == null)
            return 0;
        return favouriteFoodData.size();
    }

    public void setFavouriteFoodData(List<FabFoodEntity> favouriteFoodData) {
        this.favouriteFoodData = favouriteFoodData;
        notifyDataSetChanged();
    }

    public interface FavouriteFoodOnClickHandler {
        void onClick(FabFoodEntity fabFoodEntity);
    }

    public class FavouriteFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView imageView;
        public final TextView textView;
        public final TextView textView1;
        public final TextView textView2;
        public FabFoodEntity fabFoodEntity;

        public FavouriteFoodViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iconId);
            textView = (TextView) itemView.findViewById(R.id.tvVersionName);
            textView1 = (TextView) itemView.findViewById(R.id.tvVersionName2);
            textView2 = (TextView) itemView.findViewById(R.id.tvVersionName3);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Timber.d("Click Button" + fabFoodEntity.getMBarcode());
            favouriteFoodOnClickHandler.onClick(favouriteFoodData.get(getAdapterPosition()));

        }
    }

    private String longToDate(Long data) {
        Date date = new Date(data * 1000);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
        String dateText = df2.format(date);
        Timber.d(dateText);
        return dateText;
    }
}