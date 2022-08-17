package com.mc2022.template;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class newsDownloadService extends Service {
    private Handler myCustomHandler = new Handler();
    int newsNumber;
    public static final String BROADCAST_ACTION = "adarsh";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(internetAndWifiChecker()){
            File newsNumberFile = new File(getApplicationContext().getFilesDir(),"newsNumber.txt");
            if(newsNumberFile.exists()){
                //Toast.makeText(getApplicationContext(),"News Number Recieved",Toast.LENGTH_LONG).show();
                newsNumber=Integer.parseInt(readNewsNumberFromFileManager())+1;
            }
            else{
                newsNumber=0;
            }
            runnableEventStarter.run();
        }else{
            Toast.makeText(getApplicationContext(),"No Network Available",Toast.LENGTH_LONG).show();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopDownloading();
        super.onDestroy();
    }

    public void onStop(){
        stopDownloading();
        super.onDestroy();
    }

    class AsyncTaskDownloader extends AsyncTask<Integer, String, String> {
        int index;
        Intent intent;
        Context context = getApplicationContext();
        AsyncTaskDownloader(int index) {
            this.index = index;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            intent = new Intent(newsDownloadService.BROADCAST_ACTION);
            //Log.d("Application Status","In onPreExecute for News: "+index);
        }

        @Override
        protected String doInBackground(Integer... strings) {
            JSONObject[] j1 = new JSONObject[1];
            URL url = null;
            String result=null;
            try {
                url = new URL("https://petwear.in/mc2022/news/news_" + index + ".json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                result = convertInputStreamToString(in);
                JSONObject jsonObject = new JSONObject(result);
                String title=jsonObject.getString("title");
                String body=jsonObject.getString("body");
                String photoUrl=jsonObject.getString("image-url");
                saveTitleToFileManager(title,index);
                saveBodyToFileManager(body,index);
                savePhotoUrlToFileManager(photoUrl,index);
                saveNewsNumberToFileManager(index);
                intent.putExtra("newsNumber",index);
                context.sendBroadcast(intent);
                }catch(JSONException | MalformedURLException e){
                //Log.d("Application Status", "Downloading is Stopped because all news are readed.");
                stopDownloading();
                e.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //Log.d("Application Status", "In doInBackground for News: "+index);
            return result;
        }

        @Override
        protected void onPostExecute(String j1) {
            super.onPostExecute(j1);
            //Log.d("Application Status","In onPostExecute for News: "+index);
        }
    }
    private void saveTitleToFileManager(String temp, int index) {
        try{
            FileOutputStream myCustomfos = openFileOutput("News"+index+".txt", Context.MODE_PRIVATE);
            String data = temp;
            myCustomfos.write(data.getBytes());
            myCustomfos.flush();
            myCustomfos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveBodyToFileManager(String temp, int index) {
        try {
            FileOutputStream myCustomfos = openFileOutput("Newss"+index+".txt", Context.MODE_PRIVATE);
            String data = temp;
            myCustomfos.write(data.getBytes());
            myCustomfos.flush();
            myCustomfos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void savePhotoUrlToFileManager(String temp, int index) {
        try {
            FileOutputStream fos = openFileOutput("Newsss"+index+".txt", Context.MODE_PRIVATE);
            String data = temp;
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveNewsNumberToFileManager(int newsNumber) {
        try {
            FileOutputStream fos = openFileOutput("newsNumber.txt", Context.MODE_PRIVATE);
            String data = ""+newsNumber;
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readNewsNumberFromFileManager() {
        String filename = "newsNumber.txt";
        String result = "";
        try {
            FileInputStream myCustomReader = getApplicationContext().openFileInput(filename);
            int a;
            StringBuilder output = new StringBuilder();
            while ((a = myCustomReader.read()) != -1) {
                output.append((char) a);
            }
            result = output.toString();
            myCustomReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }

    private String convertInputStreamToString(InputStream is) {
        String rLine = "";
        InputStreamReader inputReaderObject = new InputStreamReader(is);
        StringBuilder output = new StringBuilder();
        BufferedReader rd = new BufferedReader(inputReaderObject);
        try {
            while ((rLine = rd.readLine()) != null) {
                output.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    private Runnable runnableEventStarter = new Runnable() {
        @Override
        public void run() {
            while(internetAndWifiChecker()){
                AsyncTaskDownloader atd = new AsyncTaskDownloader(newsNumber);
                atd.execute();
                newsNumber++;
                break;
            }
            if(!internetAndWifiChecker()){
                myCustomHandler.postDelayed(this,1000);
            }else{
                myCustomHandler.postDelayed(this,10000);
            }
        }
    };

    private void stopDownloading(){
        myCustomHandler.removeCallbacks(runnableEventStarter);
        stopService(new Intent(getBaseContext(),newsDownloadService.class));
    }

    private boolean internetAndWifiChecker() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nI = cm.getActiveNetworkInfo();
        return nI != null && nI.isConnected();
    }
}
