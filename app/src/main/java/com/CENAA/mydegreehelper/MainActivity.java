package com.CENAA.mydegreehelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.CENAA.mydegreehelper.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

    public static final String EXTRA_MESSAGE = "com.CENAA.mydegreehelper.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        stateManager = ((BPstate)getApplicationContext()).getStateManager();
        userID = stateManager.getUserState().id;
        getUser(userID);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_browse:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BrowseFragment()).commit();
                break;
            case R.id.nav_catalogue:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CatalogueFragment()).commit();
                break;
            case R.id.nav_achievements:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AchievementsFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            stateManager.setState(gson.fromJson(contents, Blueprint.class));
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void navToFile(View view){
       Intent intent = new Intent(this, FileTest.class);
       String message = "Navigating";
       intent.putExtra(EXTRA_MESSAGE, message);
       startActivity(intent);
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
                    if(object.getString("user_progress").startsWith("{\"bpComplete")){
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