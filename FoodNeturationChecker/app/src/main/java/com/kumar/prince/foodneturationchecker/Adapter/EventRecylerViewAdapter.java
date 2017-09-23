package com.kumar.prince.foodneturationchecker.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.data.model.FoodCheckerEvent;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_NOT_IN_OFF_DATABASE;
import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_OK;
import static com.kumar.prince.foodneturationchecker.utils.ErrorMessage.STATUS_SERVER_UNREACHABLE;

/**
 * Created by prince on 9/9/17.
 */

public class EventRecylerViewAdapter extends RecyclerView.Adapter<EventRecylerViewAdapter.EventViewHolder> implements Serializable {

    final private EventAdapterOnClickHandler eventAdapterOnClickHandler;
    List<FoodCheckerEvent> foodChecker_events;
    Context context;

    public EventRecylerViewAdapter(EventAdapterOnClickHandler eventAdapterOnClickHandler) {
        this.eventAdapterOnClickHandler = eventAdapterOnClickHandler;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        FoodCheckerEvent foodChecker_event = foodChecker_events.get(position);
        String value = "";

        //  holder.imageView.setImageDrawable(android.R.drawable.common_google_signin_btn_icon_dark);
        value = "BarCode:-" + foodChecker_event.getBarcode();
        holder.textView.setText(value);
        value = "Status:-" + foodChecker_event.getStatus();
        holder.textView1.setText(value);
        value = "Date:-  " + longToDate(foodChecker_event.getTimestamp());
        holder.textView2.setText(value);
        if (foodChecker_event.getStatus().equals(STATUS_NOT_IN_OFF_DATABASE)) {
            holder.imageView.setImageResource(R.drawable.ic_cloud_off_black_24dp);
            holder.textView1.setTextColor(Color.parseColor("#FF0000"));
        } else if (foodChecker_event.getStatus().equals(STATUS_SERVER_UNREACHABLE)) {
            holder.imageView.setImageResource(R.drawable.ic_network_problem_orange_24dp);
            holder.textView1.setTextColor(Color.parseColor("#FB8C00"));
        } else if (foodChecker_event.getStatus().equals(STATUS_OK)) {
            holder.textView1.setTextColor(Color.parseColor("#4CAF50"));
            holder.imageView.setImageResource(R.drawable.ic_food_found_green_24dp);
        }
        holder.foodChecker_event = foodChecker_event;

    }

    @Override
    public int getItemCount() {
        if (foodChecker_events == null)
            return 0;
        return foodChecker_events.size();
    }

    public void setEventData(List<FoodCheckerEvent> foodChecker_events) {
        this.foodChecker_events = foodChecker_events;
        notifyDataSetChanged();
    }

    private String longToDate(Long data) {
        Date date = new Date(data * 1000);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
        String dateText = df2.format(date);
        Timber.d(dateText);
        return dateText;
    }

    public interface EventAdapterOnClickHandler {
        void onClick(FoodCheckerEvent foodChecker_event);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView imageView;
        public final TextView textView;
        public final TextView textView1;
        public final TextView textView2;
        public FoodCheckerEvent foodChecker_event;

        public EventViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iconId);
            textView = (TextView) itemView.findViewById(R.id.tvVersionName);
            textView1 = (TextView) itemView.findViewById(R.id.tvVersionName2);
            textView2 = (TextView) itemView.findViewById(R.id.tvVersionName3);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Timber.d("Click Button" + foodChecker_event.getBarcode());
            eventAdapterOnClickHandler.onClick(foodChecker_events.get(getAdapterPosition()));

        }
    }
}
