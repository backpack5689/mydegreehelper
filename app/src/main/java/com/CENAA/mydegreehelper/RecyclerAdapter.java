package com.CENAA.mydegreehelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<Course> courseList;

    public RecyclerAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.course_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseSub.setText(course.getCourseSub());
        holder.courseNum.setText(String.valueOf(course.getCourseNum()));
        holder.courseTitle.setText(course.getCourseName());
        holder.requirements.setText("tmp");

        boolean isExpanded = courseList.get(position).isExpanded();

        if (isExpanded) {
            holder.dropdownIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_up);
            holder.expandableLayout.setVisibility(View.VISIBLE);
        } else {
            holder.dropdownIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_down);
            holder.expandableLayout.setVisibility(View.GONE);
        }

        boolean isCompleted = courseList.get(position).isCompleted();

        if (isCompleted) {
            holder.completeButton.setVisibility(View.GONE);
        } else {
            holder.completeButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView dropdownIcon;
        TextView courseSub, courseNum, courseTitle, requirements;
        ConstraintLayout expandableLayout, courseInfoCard;
        Button completeButton;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            dropdownIcon = itemView.findViewById(R.id.dropdownIcon);
            courseSub = itemView.findViewById(R.id.achievementName);
            courseNum = itemView.findViewById(R.id.courseNumber);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            requirements = itemView.findViewById(R.id.requirements);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            courseInfoCard = itemView.findViewById(R.id.courseInfoCard);
            completeButton = itemView.findViewById(R.id.completeButton);

            courseInfoCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Course course = courseList.get(getAdapterPosition());
                    course.setExpanded(!course.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
