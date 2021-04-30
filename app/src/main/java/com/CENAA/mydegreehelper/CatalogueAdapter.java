package com.CENAA.mydegreehelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.CENAA.mydegreehelper.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CatalogueAdapter extends RecyclerView.Adapter<CatalogueAdapter.ViewHolder> {


    List<JSONObject> blueprintList;

    public CatalogueAdapter(List<JSONObject> bplist) {
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

    public void updateList(List<JSONObject> list2update) {
        blueprintList = list2update;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return blueprintList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, school, totalCredits;
        ConstraintLayout expandableLayout, blueprintInfoCard;
        Button applyButton;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.blueprintName);
            school = itemView.findViewById(R.id.locationDisplay);
            totalCredits = itemView.findViewById(R.id.totalCreditsDisplay);
            applyButton = itemView.findViewById(R.id.applyButton);

            // Grade entry button listener
            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Are you sure?").setMessage("Setting this as the active blueprint will change your home page");
                    builder.setPositiveButton(R.string.apply_bp, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO set active blueprint function
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }
}

