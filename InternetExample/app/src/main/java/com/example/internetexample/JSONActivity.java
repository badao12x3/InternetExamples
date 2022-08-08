package com.example.internetexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONActivity extends AppCompatActivity {

    RecyclerView recyclerView;
//    List<ItemModel> itemModels;
//    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonactivity);
//        String jsonArray = "[{\"name\":\"Peter\",\"age\":20,\"city\":\"New York\"},{\"name\":\"Peter\",\"age\":20,\"city\":\"New York\"},{\"name\":\"Peter\",\"age\":20,\"city\":\"New York\"}]";
//        String jsonString = "{\"name\":\"Peter\",\"age\":20,\"city\":\"New York\"}";
//        try{
//            //get jsonArray
//            JSONArray jsArr= new JSONArray(jsonArray);
//            for (int i = 0; i < jsArr.length(); i++){
//                //get jsonObject
//                JSONObject jsonObject = jsArr.getJSONObject(i);
//                String name = jsonObject.getString("name");
//
//                int age = jsonObject.getInt("age");
//                String city = null;
//                if (jsonObject.has("city"))
//                    city = jsonObject.getString("city");
//
//                Log.v("TAG",name + "-" +age +"-"+city);
//            }
//            //get jsonObject
//            JSONObject jsonObject = new JSONObject(jsonString);
//            String name = jsonObject.getString("name");
//
//            int age = jsonObject.getInt("age");
//            String city = null;
//            if (jsonObject.has("city"))
//            city = jsonObject.getString("city");
//
//            Log.v("TAG",name + "-" +age +"-"+city);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        itemModels = new ArrayList<>();
//        adapter = new ItemAdapter(itemModels);

        recyclerView = findViewById(R.id.rycycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);

        new DownloadTask(this).equals("https://jsonplaceholder.typicode.com/users");

    }

    private class DownloadTask extends AsyncTask<String, Void,JSONArray> {

        ProgressDialog progressDialog;
        Context context;

        public DownloadTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading data... ");
            progressDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                Log.v("TAG","Response Code" + conn.getResponseCode());

                InputStream in = conn.getInputStream();
                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while ((line = reader.readLine()) != null ){
                    builder.append(line);
                }
                reader.close();

                String json = builder.toString();

                //Log.v("TAG",json);
                //List<ItemModel> itemModels = new ArrayList<>();
//                JSONArray jsonArray = new JSONArray(json);
//                for (int i = 0; i < jsonArray.length(); i++){
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    int id = jsonObject.getInt("id");
//                    String username = jsonObject.getString("username");
//                    String name = jsonObject.getString("name");
//                    String email = jsonObject.getString("email");
//
//                    ItemModel itemModel = new ItemModel(id,username,name,email);
//                    itemModels.add(itemModel);
//                }
//
//                return itemModels;

                return new JSONArray(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            progressDialog.dismiss();
//            if (itemModels != null)     {
//                adapter.notifyDataSetChanged();
//            }
            if(jsonArray != null){
                ItemJSONAdapter adapter = new ItemJSONAdapter(jsonArray);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}