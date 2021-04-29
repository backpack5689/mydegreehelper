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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CatalogueAdapter extends RecyclerView.Adapter<CatalogueAdapter.ViewHolder> {


    List<JSONObject> blueprintList;

    public CatalogueAdapter(List <JSONObject> bplist) {

        this.blueprintList = bplist;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.catalogue_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Blueprint blueprint = blueprintList.get(position);

        JSONObject object = new JSONObject();

        try {
            object = blueprintList.get(position);

            holder.name.setText(object.getString("name"));
            holder.school.setText(object.getString("location"));
            holder.totalCredits.setText(object.getString("coursehours"));

        } catch (JSONException e) {
            e.printStackTrace();
        }








        // Apply blueprint data to RecyclerView item
        // TODO add methods like these to retrieve bp attributes to Blueprint object





//        // Check if course item card is expanded
//        // TODO add expanded attribute and isExpanded() & setExpanded() methods to Blueprint object
//        //boolean isExpanded = blueprintList.get(position).isExpanded();
//        boolean isExpanded = blueprintList
//
          if (true) {
            holder.dropdownIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_up);
            holder.expandableLayout.setVisibility(View.VISIBLE);
        }
 //         else {
//            holder.dropdownIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_down);
//            holder.expandableLayout.setVisibility(View.GONE);
//        }

       }

    public void updateList(List <JSONObject> list2update) {

        blueprintList = list2update;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return blueprintList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView background;
        ImageView dropdownIcon;
        TextView name, school, totalCredits;
        ConstraintLayout expandableLayout, blueprintInfoCard;
        Button completeButton;
        // boolean isExpanded = false;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            // Attach UI elements to variables
            // TODO make these correspond to elements in catalogue_row_item.xml


            dropdownIcon = itemView.findViewById(R.id.dropdownIcon);
            name = itemView.findViewById(R.id.courseTitle);
            school = itemView.findViewById(R.id.achievementDesc);
            totalCredits =  itemView.findViewById(R.id.requirements);
            blueprintInfoCard = itemView.findViewById(R.id.courseInfoCard);
            background = itemView.findViewById(R.id.courseRowCard);
            completeButton = itemView.findViewById(R.id.completeButton);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);



            // Listener for expanding course panel
            blueprintInfoCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Blueprint blueprint = blueprintList.get(getAdapterPosition());
//                    blueprint.setExpanded(!blueprint.isExpanded());
//                    notifyItemChanged(getAdapterPosition());



                }
            });

            // Grade entry button listener
            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set active blueprint function

                }
            });
        }
    }
}

