package com.CENAA.mydegreehelper.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.CENAA.mydegreehelper.API;
import com.CENAA.mydegreehelper.BPstate;
import com.CENAA.mydegreehelper.MainActivity;
import com.CENAA.mydegreehelper.R;
import com.CENAA.mydegreehelper.RequestHandler;
import com.CENAA.mydegreehelper.StateManager;
import com.CENAA.mydegreehelper.User;
import com.CENAA.mydegreehelper.ui.login.LoginViewModel;
import com.CENAA.mydegreehelper.ui.login.LoginViewModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button signUpButton = findViewById(R.id.register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void welcomeUser(String username) {
        String welcome = "Welcome " + username +" !";

        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        String url;

        HashMap<String, String> params;

        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        PerformNetworkRequest(String url, int requestCode) {
            this.url = url;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    StateManager stateManager = ((BPstate)getApplicationContext()).getStateManager();
                    User user = new User();
                    user.username = object.getString("user");
                    user.id = object.getInt("id");
                    stateManager.setUserState(user);
                    welcomeUser(object.getString("user"));
                }else{
                    showLoginFailed(object.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);

            if (requestCode == CODE_GET_REQUEST && params != null)
                return requestHandler.sendGetRequest(url, params);

            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
    public void user_login(String username, String password){

        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        LoginActivity.PerformNetworkRequest request = new LoginActivity.PerformNetworkRequest(API.URL_LOGIN, params, CODE_POST_REQUEST);
        request.execute();
    }
}