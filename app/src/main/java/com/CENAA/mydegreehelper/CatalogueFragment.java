package com.CENAA.mydegreehelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CENAA.mydegreehelper.ui.login.LoginActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.Objects;


public class CatalogueFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalogue, container, false);
        Button testButton = view.findViewById(R.id.testButton);
        Button fileTestButton = view.findViewById(R.id.fileTestButton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile(view);
            }
        });

        fileTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return view;
    }

    private static final int PICK_TXT_FILE = 2;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    public void openFile(View view) {
        @SuppressLint("InlinedApi")
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");

        startActivityForResult(intent, PICK_TXT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 2
                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                // Perform operations on the document using its URI.
                String fileContents = "failed to read";
                try {
                    fileContents = readTextFromUri(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Log.d("File", fileContents);
                //TO-DO possibly an Intent to push the BP below to a DB upload function
                Blueprint blueprint = new Blueprint(fileContents);
                saveBPAsLocal(blueprint);
                //Gson gson = new Gson();
                //String bpString = gson.toJson(blueprint);
                //Log.d("GSON", bpString);
                //Blueprint gsonBP = gson.fromJson(bpString, Blueprint.class);
                //gsonBP.displayBP();
                //uploadBP(blueprint);
                //blueprint.displayBP();
                //requestBP(1);
                //getAllBP();
            }
        }
    }

    private String readTextFromUri(Uri uri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream =
                     getActivity().getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
        }
        return stringBuilder.toString();
    }

    //inner class to perform network request extending an AsyncTask
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        PerformNetworkRequest(String url, int requestCode) {
            this.url = url;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getActivity().getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                   //*****TURN REQUESTED BP BACK INTO JAVA OBJECT*****
                    /* Log.d("Obj" , object.getString("degree"));

                    Gson gson = new Gson();
                    Blueprint gsonBP = gson.fromJson(object.getJSONObject("degree").getString("object"), Blueprint.class);
                    gsonBP.displayBP();*/


                    //refreshing the herolist after every operation
                    //so we get an updated list
                    //we will create this method right now it is commented
                    //because we haven't created it yet
                    //refreshHeroList(object.getJSONArray("heroes"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
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

    public void uploadBP(Blueprint bp){
        Gson gson = new Gson();
        String bpString = gson.toJson(bp);
        HashMap<String, String> params = new HashMap<>();
        params.put("object", bpString);
        params.put("degreename", "Software Engineering");
        params.put("totalhours", String.valueOf(bp.totalCredits));
        params.put("location", "Lipscomb University");

        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_CREATE_BP, params, CODE_POST_REQUEST);
        request.execute();
    }

    public void requestBP(int id){
        //Blueprint blueprint;

        HashMap<String, String> params = new HashMap<>();
        params.put("selector", Integer.toString(id));

        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_READ_BP, params, CODE_GET_REQUEST);
        request.execute();

        //return blueprint;
    }

    public void getAllBP(){
        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_GETALL_BP, CODE_GET_REQUEST);
        request.execute();
    }

    public void saveBPAsLocal(Blueprint bp){
        File file = new File(getActivity().getFilesDir(), "local");
        if(!file.exists()){
            file.mkdir();
        }
        try{
            File localFile = new File(file, "localFile.txt");
            FileWriter writer = new FileWriter(localFile);
            Gson gson = new Gson();
            String bpString = gson.toJson(bp);
            writer.append(bpString);
            writer.flush();
            writer.close();
            Toast.makeText(getActivity().getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
