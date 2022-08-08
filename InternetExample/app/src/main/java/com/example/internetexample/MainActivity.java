package com.example.internetexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView textView, textView2;
    Task1 task1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);


        findViewById(R.id.button_quick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Quick work done");
            }
        });
        findViewById(R.id.button_slow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new SlowWork().execute(6);
//                new Task1().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                new Task2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                task1 =new Task1();
                task1.execute();
            }
        });
        findViewById(R.id.button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task1.cancel(false);
            }
        });

        findViewById(R.id.button_get_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetTask().execute();
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            Log.v("TAG","Connected");
        else
            Log.v("TAG", "Not Connected");

        //check wifi or mobile
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo.isConnected())
            Log.v("TAG","wifi");

        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(networkInfo.isConnected())
            Log.v("TAG","mobile");
    }

    private class Task1 extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = 0; i<50; i ++){
                    Thread.sleep(200);
                    publishProgress(i);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            textView.setText(values[0]);
        }
    }

    private class Task2 extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = 0; i<50; i ++){
                    Thread.sleep(300);
                    publishProgress(i);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            textView2.setText(values[0]);
        }
    }

    private class SlowWork extends AsyncTask<Integer, Integer, Boolean>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMessage("Processing...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                int n = params[0];
                for (int i = 0; i<n; i ++){
                    Thread.sleep(1000);
                    publishProgress(i + 1);
                }

                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            textView.append("\n"+values[0]);
        }

        @Override
        protected void onPostExecute(Boolean resul) {
            dialog.dismiss();
        }
    }

    private class GetTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL("https://httpbin.org/get?param1=value1&param2=value2");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                Log.v("TAG","Response Code" + conn.getResponseCode());

                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                StringBuffer buffer = new StringBuffer();

                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                    buffer.append("\n");
                }


                reader.close();
                conn.disconnect();

                Log.v("TAG",buffer.toString());

                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private class DownloadTask extends AsyncTask<Void, Integer, Boolean>{
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            Log.v("TAG", "Download started");
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMessage("Downloading...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL("https://lebavui.github.io/video/ecard.mp4");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                Log.v("TAG","Response Code" + conn.getResponseCode());

                int total = conn.getContentLength();
                InputStream in = conn.getInputStream();
                FileOutputStream outputStream =  openFileOutput("test.mp4",MODE_PRIVATE);

                byte[] buffer =new byte[1024];
                int len;
                int size = 0;

                while ((len = in.read(buffer)) > 0 ){
                    outputStream.write(buffer,0,len);
                    size += len;
                    publishProgress(size,total);
                }
                outputStream.close();
                in.close();

                Log.v("TAG",buffer.toString());

                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int size =values[0];
            int total = values[1];
            dialog.setMax(total);
            dialog.setProgress(size);

        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            Log.v("TAG", "Download done");
        }
    }

    private class PostTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL("https://httpbin.org/post");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                Log.v("TAG","Response Code" + conn.getResponseCode());

                OutputStream outputStream = conn.getOutputStream();
                DataOutputStream writer = new DataOutputStream(outputStream);
                writer.writeBytes("user=admin&password=123456");
                writer.close();


                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                StringBuffer buffer = new StringBuffer();

                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                    buffer.append("\n");
                }


                reader.close();
                conn.disconnect();

                Log.v("TAG",buffer.toString());

                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

}