package com.CENAA.mydegreehelper;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.CENAA.mydegreehelper.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    StateManager stateManager;
    Blueprint state;
    int userID;
    NavigationView navigationView;
    Menu navMenu;
    View headerView;
    TextView headerTitle;

    public static final String EXTRA_MESSAGE = "com.CENAA.mydegreehelper.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Attach navigation elements to variables
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navMenu = navigationView.getMenu();
        headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        // Nav drawer listener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Load state
        stateManager = ((BPstate)getApplicationContext()).getStateManager();
        userID = stateManager.getUserState().id;
        state = stateManager.getState();

        getUser(userID);

        headerTitle = (TextView) headerView.findViewById(R.id.name);
        headerTitle.setText(stateManager.getUserState().username);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                if (userID != -1 && stateManager.getState().requirements.size() > 0) {
                    item.setCheckable(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                } else { // Disable navigation to home if blueprint is not selected
                    item.setCheckable(false);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CatalogueFragment()).commit();
                    Toast.makeText(getApplicationContext(), "Please select a blueprint first", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_catalogue:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CatalogueFragment()).commit();
                break;
            case R.id.nav_achievements:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AchievementsFragment()).commit();
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.log_out_confirm);
        builder.setPositiveButton(R.string.log_out, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stateManager.setUserState(new User());
                stateManager.setState(new Blueprint());
                MainActivity.PerformNetworkRequest request = new MainActivity.PerformNetworkRequest(API.URL_LOGIN, CODE_POST_REQUEST);
                request.execute();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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

    private boolean initBP() throws IOException {
        File file = new File(getApplicationContext().getFilesDir() + "/local/", "user_" + userID +"_localFile.txt");
        if(!file.exists()){return false;}
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fis);
        StringBuilder stringBuilder = new StringBuilder();
        String contents;
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            contents = stringBuilder.toString();
        }
        Log.d("contents", contents);
        if(contents.equals("null\n")){
            return false;
        }else{
            Gson gson = new Gson();
            Blueprint userBP = gson.fromJson(contents, Blueprint.class);
            userBP.referenceRebuild();
            stateManager.setState(userBP);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

   public void getAchievement(String achievementName) {
        String toastText = "Achievment Unlocked: " + achievementName;
        Toast achievementToast = Toast.makeText(getApplicationContext(), achievementName, Toast.LENGTH_SHORT);
        achievementToast.show();
   }

   //****Begin API Request Code****

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
                    //Log.d("UP", object.getString("user_progress"));
                    if(object.getString("user_progress").startsWith("{\"bpID")){
                        Gson gson = new Gson();
                        Blueprint userProgress = gson.fromJson(object.getString("user_progress"), Blueprint.class);
                        Blueprint.saveBPAsLocal(getApplicationContext(), userProgress);
                    }
                    if(initBP()){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                        navigationView.setCheckedItem(R.id.nav_home);
                    }else{
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CatalogueFragment()).commit();
                        navigationView.setCheckedItem(R.id.nav_catalogue);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Failed to Load Progress from DB", Toast.LENGTH_LONG).show();

                }
            } catch (JSONException | IOException e) {
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
    public void getUser(int userID){

        HashMap<String, String> params = new HashMap<>();
        params.put("userid", Integer.toString(userID));

        MainActivity.PerformNetworkRequest request = new MainActivity.PerformNetworkRequest(API.URL_GETUSER, params, CODE_POST_REQUEST);
        request.execute();
    }



}