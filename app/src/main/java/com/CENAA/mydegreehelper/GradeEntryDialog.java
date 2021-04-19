package com.CENAA.mydegreehelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class GradeEntryDialog extends DialogFragment {

    Button enterButton, cancelButton;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.grade_entry_dialog, null);
        builder.setView(dialogView);

        enterButton = dialogView.findViewById(R.id.enter_grade_button);
        cancelButton = dialogView.findViewById(R.id.cancel_button);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setMessage(R.string.grade_entry_title);
        return builder.create();
    }
}

