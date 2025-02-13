package com.CENAA.mydegreehelper;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {

    List<Achievement> achievementsList;

    public AchievementAdapter(List<Achievement> achievementsList) { this.achievementsList = achievementsList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.achievement_row_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Achievement achievement = achievementsList.get(position);
        holder.achievementName.setText(achievement.getAchievementName());
        holder.achievementDesc.setText(achievement.getAchievementDesc());

        if(achievement.isComplete()) {
            holder.achievementCompleteCheck.setImageResource(R.drawable.ic_baseline_check_box_24);
            holder.achievementName.setTextColor(Color.BLACK);
        } else {
            holder.achievementName.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return achievementsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView achievementCompleteCheck;
        TextView achievementName, achievementDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            achievementName = itemView.findViewById(R.id.achievementName);
            achievementDesc = itemView.findViewById(R.id.achievementDesc);
            achievementCompleteCheck = itemView.findViewById(R.id.achievementCompleteCheck);
        }
    }

}
