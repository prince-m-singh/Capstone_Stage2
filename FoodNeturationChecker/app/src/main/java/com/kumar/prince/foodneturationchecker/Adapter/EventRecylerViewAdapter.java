package com.kumar.prince.foodneturationchecker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kumar.prince.foodneturationchecker.R;
import com.kumar.prince.foodneturationchecker.data.model.FC_Event;

import java.io.Serializable;
import java.util.List;

import timber.log.Timber;

/**
 * Created by prince on 9/9/17.
 */

public class EventRecylerViewAdapter extends RecyclerView.Adapter<EventRecylerViewAdapter.EventViewHolder>implements Serializable{

    List<FC_Event> fc_events;
    final private EventAdapterOnClickHandler eventAdapterOnClickHandler;
    Context context;

    public EventRecylerViewAdapter(EventAdapterOnClickHandler eventAdapterOnClickHandler) {
        this.eventAdapterOnClickHandler = eventAdapterOnClickHandler;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view =LayoutInflater.from(context).inflate(R.layout.card_view,parent,false);

        return new EventViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        FC_Event fc_event = fc_events.get(position);

      //  holder.imageView.setImageDrawable(android.R.drawable.common_google_signin_btn_icon_dark);
        holder.textView.setText(fc_event.getBarcode());
        holder.fc_event = fc_event;

    }

    @Override
    public int getItemCount() {
        if (fc_events==null)
            return 0;
        return fc_events.size();
    }

    public void setEventData(List<FC_Event> fc_events){
        this.fc_events=fc_events;
        notifyDataSetChanged();
    }
    public interface  EventAdapterOnClickHandler{
        void onClick( FC_Event fc_event);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView imageView;
        public final TextView textView;
        public  FC_Event fc_event;

        public EventViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.iconId);
            textView=(TextView) itemView.findViewById(R.id.tvVersionName);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Timber.d("Click Button"+fc_event.getBarcode());
            eventAdapterOnClickHandler.onClick(fc_events.get(getAdapterPosition()));

        }
    }
}
