package com.example.serhii.githubio;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Serhii on 11/10/2017.
 */

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder> {

    private List<RepositoryItemInfo> items;
    private Context context;

    public RepositoryAdapter(List<RepositoryItemInfo> items, Context context)
    {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repository_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RepositoryItemInfo item = items.get(position);

        holder.textViewName.setText(item.getName());
        holder.textViewDescription.setText(item.getDescription());
        holder.textViewLanguage.setText(item.getLanguage());
        holder.textViewStars.setText(Integer.toString(item.getStarsCount()));
        holder.textViewForks.setText(Integer.toString(item.getForksCount()));
        String updateDate = item.getUpdatedDate().substring(0, 10);
        holder.textViewUpdatedDate.setText(updateDate);

        //hide textfields with null values
        //And I am doing GONE/Visible for every case, because cells are reusable and if
        //some first item will be hidden, because it was NULL, all other items in this cell remain the same
        if(item.getName() == null) {
            holder.textViewName.setVisibility(View.GONE);
        } else {
            holder.textViewName.setVisibility(View.VISIBLE);
        }

        if(item.getDescription() == null) {
            holder.textViewDescription.setVisibility(View.GONE);
        } else {
            holder.textViewDescription.setVisibility(View.VISIBLE);
        }

        if(item.getLanguage() == null) {
            holder.textViewLanguage.setVisibility(View.GONE);
        } else {
            holder.textViewLanguage.setVisibility(View.VISIBLE);
        }

        holder.itemTouchHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RepositoryDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Constants.PARSEL_DATA, items.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout itemTouchHandler;
        public TextView textViewName;
        public TextView textViewDescription;
        public TextView textViewLanguage;
        public TextView textViewStars;
        public TextView textViewForks;
        public TextView textViewUpdatedDate;

        public ViewHolder(View itemView) {
            super(itemView);

            itemTouchHandler = (LinearLayout) itemView.findViewById(R.id.itemTouchHandler);
            textViewName = (TextView) itemView.findViewById(R.id.name);
            textViewDescription = (TextView) itemView.findViewById(R.id.description);
            textViewLanguage = (TextView) itemView.findViewById(R.id.language);
            textViewStars = (TextView) itemView.findViewById(R.id.stars);
            textViewForks = (TextView) itemView.findViewById(R.id.forks);
            textViewUpdatedDate = (TextView) itemView.findViewById(R.id.updatedDate);
        }
    }
}