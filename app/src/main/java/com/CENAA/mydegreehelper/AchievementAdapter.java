package com.CENAA.mydegreehelper;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.achievement_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView achievementIcon;
        TextView achievementName, achievementDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            achievementIcon = itemView.findViewById(R.id.achievementIcon);
            achievementName = itemView.findViewById(R.id.achievementName);
            achievementDesc = itemView.findViewById(R.id.achievementDesc);
        }
    }

}
