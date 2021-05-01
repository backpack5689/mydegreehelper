package com.CENAA.mydegreehelper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class User {
    public String username;
    public int id;

    public User() {
        username = "unset";
        id = -1;
    }

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        String url;
        HashMap<String, String> params;
        int requestCode;
        Context context;


        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode, Context c) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
            this.context = c;
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
                    if(object.has("degree")){
                        HashMap<String, String> params = new HashMap<>();
                        JSONObject bp = object.getJSONObject("degree");
                        params.put("userid", Integer.toString(id));
                        params.put("bpid", bp.getString("id"));
                        Log.d("OBJ", bp.getString("object"));
                        params.put("progress", bp.getString("object"));
                        //Set state for Home
                        Gson gson = new Gson();
                        StateManager stateManager = ((BPstate) context.getApplicationContext()).getStateManager();
                        stateManager.setState(gson.fromJson(bp.getString("object"), Blueprint.class));
                        stateManager.getState().referenceRebuild();

                        User.PerformNetworkRequest request = new User.PerformNetworkRequest(API.URL_UPDATE_USER, params, CODE_POST_REQUEST);
                        request.execute();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        Log.d("DB", "Progress Updated");
                    }
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

    public void userSetBP(Context c, String bpID) {
        HashMap<String, String> params = new HashMap<>();
        params.put("selector", bpID);

        User.PerformNetworkRequest request = new User.PerformNetworkRequest(API.URL_READ_BP, params, CODE_GET_REQUEST, c);
        request.execute();
    }

    public void userUpdateProgress(Context c) {
        StateManager stateManager = ((BPstate) c.getApplicationContext()).getStateManager();
        User user = stateManager.getUserState();
        Gson gson = new Gson();

        HashMap<String, String> params = new HashMap<>();
        params.put("userid", Integer.toString(user.id));
        params.put("jsonstring", gson.toJson(stateManager.getState()));

        User.PerformNetworkRequest request = new User.PerformNetworkRequest(API.URL_UPDATE_USERP, params, CODE_POST_REQUEST);
        request.execute();
    }
}
