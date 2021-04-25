package com.CENAA.mydegreehelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class GradeEntryDialog extends DialogFragment {

    StateManager stateManager;
    Blueprint state;
    String courseName;
    Bundle bundle;
    EditText gradeInput;
    Button enterButton, cancelButton;
    double grade;
    GradeEntryCallback callback;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkFieldsForEmptyValues();
        }
    };

    public GradeEntryDialog(GradeEntryCallback callback) {
        this.callback = callback;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Retrieve current blueprint state
        stateManager = ((BPstate)getActivity().getApplicationContext()).getStateManager();
        state = stateManager.getState();

        View dialogView = inflater.inflate(R.layout.grade_entry_dialog, null);
        builder.setView(dialogView);

        // Retrieve course name
        bundle = getArguments();
        courseName = bundle.getString("courseName");

        grade = 0.0; // Initialize grade variable

        // Attach UI elements to variables
        enterButton = dialogView.findViewById(R.id.enter_grade_button);
        cancelButton = dialogView.findViewById(R.id.cancel_button);
        gradeInput = dialogView.findViewById(R.id.gradeEntry);

        gradeInput.addTextChangedListener(mTextWatcher);

        // Listener for entering
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade = Double.parseDouble(gradeInput.getText().toString());
                if (grade != 0) {
                    state.completeCourse(courseName, grade);
                    stateManager.setState(state);
                    callback.onDialogCallback();
                }
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    private void checkFieldsForEmptyValues() {
        String input = gradeInput.getText().toString();
        Log.i("input", input);
        enterButton.setEnabled(!input.equals(""));
    }
}

