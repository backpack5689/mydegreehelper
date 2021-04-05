package com.CENAA.mydegreehelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<CourseUI> courseList;

    public RecyclerAdapter(List<CourseUI> courseList) {
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
        CourseUI course = courseList.get(position);
        holder.courseSub.setText(course.getCourseSub());
        holder.courseNum.setText(String.valueOf(course.getCourseNum()));
        holder.courseTitle.setText(course.getCourseName());
        holder.requirements.setText(course.getRequirements());

        boolean isExpanded = courseList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView courseSub, courseNum, courseTitle, requirements;
        ConstraintLayout expandableLayout, courseInfoCard;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            courseSub = itemView.findViewById(R.id.achievementName);
            courseNum = itemView.findViewById(R.id.courseNumber);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            requirements = itemView.findViewById(R.id.requirements);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            courseInfoCard = itemView.findViewById(R.id.courseInfoCard);

            courseInfoCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseUI course = courseList.get(getAdapterPosition());
                    course.setExpanded(!course.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
