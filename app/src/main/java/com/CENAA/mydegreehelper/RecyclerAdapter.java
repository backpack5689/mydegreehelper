package com.CENAA.mydegreehelper;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<Course> courseList;
    List<Course> requirementsList;
    String reqString = "";

    ProgressCallback callback;

    public RecyclerAdapter(List<Course> courseList, ProgressCallback callback) {
        this.courseList = courseList;
        this.callback = callback;
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

        // Apply course data to RecyclerView item
        holder.courseSub.setText(course.getCourseSub());
        holder.courseNum.setText(String.valueOf(course.getCourseNum()));
        holder.courseTitle.setText(course.getCourseName());
        holder.gradeDisplay.setText(String.valueOf(course.getGrade()));

        // Build requirements list display
        requirementsList = course.getRequirements();

        int incompleteCount;
        if (requirementsList.size() == 0) {
            reqString = "None";
            incompleteCount = 0;
        } else {
            incompleteCount = 0;
            for (int i = 0; i < requirementsList.size(); i++) {
                reqString = "• " + requirementsList.get(i).courseName;
                if (requirementsList.get(i).isCompleted()) {
                    reqString = reqString + " (Complete)\n";
                } else {
                    reqString = reqString + " (Incomplete)\n";
                    incompleteCount++;
                }
            }
        }
        holder.requirements.setText(reqString);

        if (incompleteCount == 0) {
            holder.completeButton.setVisibility(View.VISIBLE);
        } else {
            holder.completeButton.setVisibility(View.GONE);
        }

        // Check if course item card is expanded
        boolean isExpanded = courseList.get(position).isExpanded();

        if (isExpanded) {
            holder.dropdownIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_up);
            holder.expandableLayout.setVisibility(View.VISIBLE);
        } else {
            holder.dropdownIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_down);
            holder.expandableLayout.setVisibility(View.GONE);
        }

        // Show/hide requirements or grade depending on if course is complete
        boolean isCompleted = courseList.get(position).isCompleted();

        if (isCompleted) {
            holder.background.setBackgroundColor(Color.parseColor("#E9FDE9"));

            holder.gradeLabel.setVisibility(View.VISIBLE);
            holder.gradeDisplay.setVisibility(View.VISIBLE);
            holder.completeButton.setText(R.string.complete_button_edit);
            holder.requirementsLabel.setVisibility(View.GONE);
            holder.requirements.setVisibility(View.GONE);
        } else {
            holder.background.setBackgroundColor(Color.parseColor("#F8F8F8"));

            holder.requirementsLabel.setVisibility(View.VISIBLE);
            holder.requirements.setVisibility(View.VISIBLE);
            holder.completeButton.setText(R.string.complete_button);
            holder.gradeLabel.setVisibility(View.GONE);
            holder.gradeDisplay.setVisibility(View.GONE);
        }
    }

    public void updateList(List<Course> list) {
        courseList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView background;
        ImageView dropdownIcon;
        TextView courseSub, courseNum, courseTitle, requirementsLabel, requirements, gradeLabel, gradeDisplay;
        ConstraintLayout expandableLayout, courseInfoCard;
        Button completeButton;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            // Attach UI elements to variables
            background = itemView.findViewById(R.id.courseRowCard);
            dropdownIcon = itemView.findViewById(R.id.dropdownIcon);
            courseSub = itemView.findViewById(R.id.achievementName);
            courseNum = itemView.findViewById(R.id.courseNumber);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            requirementsLabel = itemView.findViewById(R.id.requirementsLabel);
            requirements = itemView.findViewById(R.id.requirements);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            courseInfoCard = itemView.findViewById(R.id.courseInfoCard);
            completeButton = itemView.findViewById(R.id.completeButton);
            gradeLabel = itemView.findViewById(R.id.gradeLabel);
            gradeDisplay = itemView.findViewById(R.id.gradeDisplay);

            // Listener for expanding course panel
            courseInfoCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Course course = courseList.get(getAdapterPosition());
                    course.setExpanded(!course.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            // Grade entry button listener
            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    String courseName = courseList.get(getAdapterPosition()).getCourseName();
                    double grade = courseList.get(getAdapterPosition()).getGrade();

                    // Create new instance of grade entry dialog
                    GradeEntryDialog dialog = new GradeEntryDialog(new GradeEntryCallback() {
                        @Override
                        public void onDialogCallback() {
                            notifyDataSetChanged(); // Update RecyclerView
                            callback.onProgressCallback(); // Update ProgressBar
                        }
                    });
                    // Pass coursename and current grade to dialog
                    Bundle bundle = new Bundle();
                    bundle.putString("courseName", courseName);
                    bundle.putDouble("grade", grade);
                    dialog.setArguments(bundle);

                    dialog.show(manager, "Grade Entry");
                }
            });
        }
    }
}

