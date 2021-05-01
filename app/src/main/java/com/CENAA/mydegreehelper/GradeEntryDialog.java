package com.CENAA.mydegreehelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

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

    private final TextWatcher mTextWatcher = new TextWatcher() {
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
        stateManager = ((BPstate) requireActivity().getApplicationContext()).getStateManager();
        state = stateManager.getState();

        View dialogView = inflater.inflate(R.layout.grade_entry_dialog, null);
        builder.setView(dialogView);

        // Retrieve course name and grade
        bundle = getArguments();
        assert bundle != null;
        courseName = bundle.getString("courseName");
        grade = bundle.getDouble("grade");

        // Attach UI elements to variables
        enterButton = dialogView.findViewById(R.id.enter_grade_button);
        cancelButton = dialogView.findViewById(R.id.cancel_button);
        gradeInput = dialogView.findViewById(R.id.gradeEntry);

        gradeInput.setFilters(new InputFilter[]{new GradeInputFilter()});
        gradeInput.addTextChangedListener(mTextWatcher); // Check if input is blank to disable entry button

        // Set text entry value to current grade if editing
        if (grade != 0.0) {
            gradeInput.setText(String.valueOf(grade));
        }

        // Listener for entering
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade = Double.parseDouble(gradeInput.getText().toString());
                if (grade != 0) {
                    state.completeCourse(courseName, grade);
                    stateManager.setState(state);
                    stateManager.getUserState().userUpdateProgress(requireActivity().getApplicationContext());
                    callback.onDialogCallback();
                }
                dismiss();
                Gson gson = new Gson();
                Log.d("BPSTATE", gson.toJson(stateManager.getState()));
                Log.d("Complete", Boolean.toString(stateManager.getState().checkBPComplete()));
                if(stateManager.getState().checkBPComplete()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Congratulations Graduate!!", Toast.LENGTH_SHORT).show();
                }
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
        enterButton.setEnabled(!input.equals(""));
    }
}

