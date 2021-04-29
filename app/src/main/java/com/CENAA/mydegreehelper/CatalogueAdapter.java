package com.CENAA.mydegreehelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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
        Button applyButton;
        boolean expanded = false;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.blueprintName);
            school = itemView.findViewById(R.id.locationDisplay);
            totalCredits =  itemView.findViewById(R.id.totalCreditsDisplay);
            applyButton = itemView.findViewById(R.id.applyButton);

            // Grade entry button listener
            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set active blueprint function

                }
            });
        }
    }
}

