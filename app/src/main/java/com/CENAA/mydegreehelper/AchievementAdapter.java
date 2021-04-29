package com.CENAA.mydegreehelper;

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
        }
    }

    @Override
    public int getItemCount() {
        return achievementsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView achievementIcon, achievementCompleteCheck;
        TextView achievementName, achievementDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            achievementIcon = itemView.findViewById(R.id.achievementIcon);
            achievementName = itemView.findViewById(R.id.achievementName);
            achievementDesc = itemView.findViewById(R.id.locationLabel);
            achievementCompleteCheck = itemView.findViewById(R.id.achievementCompleteCheck);
        }
    }

}
