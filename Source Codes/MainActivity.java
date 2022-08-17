package com.mc2022.template;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    myCustomBroadcastReceiver myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentTwo f2 = new fragmentTwo();
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        Context c = getBaseContext();
        Bundle b= new Bundle();
        ft.replace(R.id.buttonLinearLayout,f2);
        ft.commit();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }
    protected void onResume() {
        super.onResume();
        myReceiver = new myCustomBroadcastReceiver();
        registerReceiver(myBroadcastReceiverFinal, new IntentFilter(newsDownloadService.BROADCAST_ACTION));
        IntentFilter myCustomBroadcastIntentFilter = new IntentFilter();
        myCustomBroadcastIntentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        myCustomBroadcastIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        myCustomBroadcastIntentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        myCustomBroadcastIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(myReceiver,myCustomBroadcastIntentFilter);
    }
    protected void onPause(){
        super.onPause();
        unregisterReceiver(myBroadcastReceiverFinal);
        unregisterReceiver(myReceiver);
    }
    protected void onStop(){
        super.onStop();

    }
    protected void onDestroy() {
        super.onDestroy();
    }

    private BroadcastReceiver myBroadcastReceiverFinal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle receivedServiceBundle = intent.getExtras();
            int newsNumber=receivedServiceBundle.getInt("newsNumber");
            String title = readDataTitle(newsNumber);
            String body = readDataBody(newsNumber);
            String photoUrl = readDataPhotoUrl(newsNumber);
            if(getLifecycle().getCurrentState() ==  Lifecycle.State.RESUMED){
                //Toast.makeText(getApplicationContext(),"Fragment created for news "+newsNumber,Toast.LENGTH_LONG).show();
                AsyncTaskDownloadTwo task = new AsyncTaskDownloadTwo(title,body,photoUrl,newsNumber);
                task.execute();
            }
        }
    };

    public void fragmentGenerator(Fragment fragment, String title, String body, Bitmap b, int newsNumber){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle activityToFragmentBundle = new Bundle();
        activityToFragmentBundle.putString("newsTitle", title);
        activityToFragmentBundle.putString("newsBody",body);
        activityToFragmentBundle.putInt("newsNumber",newsNumber);
        fragment.setArguments(activityToFragmentBundle);
        ft.replace(R.id.newsLinearLayout, fragment);
        ft.commit();
    }

    private String readDataTitle(int newsNumber){
        File titleFile = new File(getApplicationContext().getFilesDir(),"News"+newsNumber+".txt");
        StringBuilder output = new StringBuilder();
        try {
            BufferedReader br =new BufferedReader(new FileReader(titleFile));
            String line;
            while((line = br.readLine()) != null){
                output.append(line);
                output.append("\n");
            }
            br.close();
        }catch (Exception ignored){

        }
        return output.toString();
    }

    private String readDataBody(int newsNumber) {
        File bodyFile = new File(getApplicationContext().getFilesDir(),"Newss"+newsNumber+".txt");
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br =new BufferedReader(new FileReader(bodyFile));
            String line;
            while((line = br.readLine()) != null){
                result.append(line);
                result.append("\n");
            }
            br.close();
        }catch (Exception ignored){

        }
        return result.toString();
    }

    private String readDataPhotoUrl(int newsNumber) {
        String photoUrlFile = "Newsss"+newsNumber+".txt";
        String result ="";
        try {
            FileInputStream fin = getApplicationContext().openFileInput(photoUrlFile);
            int a;
            StringBuilder output = new StringBuilder();
            while ((a = fin.read()) != -1) {
                output.append((char) a);
            }
            result = output.toString();
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }
    private class AsyncTaskDownloadTwo extends AsyncTask<Integer, Void, Bitmap> {
        String title;
        String body;
        String url;
        InputStream is = null;
        Bitmap bmImg = null;
        int newsNumber;

        public AsyncTaskDownloadTwo(String title, String body, String url, int newsNumber) {
            this.title = title;
            this.body = body;
            this.url=url;
            this.newsNumber=newsNumber;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Bitmap doInBackground(Integer...integers) {
            try {
                URL ImageUrl =new URL(url);
                HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bmImg = BitmapFactory.decodeStream(is, null, options);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmImg;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(getLifecycle().getCurrentState() ==  Lifecycle.State.RESUMED){
                saveImageToFileManager(bitmap,newsNumber);
                fragmentGenerator(new newsFragment(), title, body, bitmap, newsNumber);
            }
        }
    }

    public class myCustomBroadcastReceiver extends BroadcastReceiver {
        public myCustomBroadcastReceiver() {
        }
        @Override
        public void onReceive(Context context, Intent intent){
            switch (intent.getAction()) {
                case Intent.ACTION_BATTERY_LOW:
                    Toast.makeText(context, "Battery Low", Toast.LENGTH_SHORT).show();
                    stopService(new Intent(getBaseContext(), newsDownloadService.class));
                    break;
                case Intent.ACTION_BATTERY_OKAY:
                    Toast.makeText(context, "Battery Okay", Toast.LENGTH_SHORT).show();
                    startService(new Intent(getBaseContext(), newsDownloadService.class));
                    break;
                case Intent.ACTION_POWER_CONNECTED:
                    Toast.makeText(context, "Power Charging Connected", Toast.LENGTH_SHORT).show();
                    stopService(new Intent(getBaseContext(), newsDownloadService.class));
                    break;
                default:
                    intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);
                    Toast.makeText(context, "Power Charging Disconnected", Toast.LENGTH_SHORT).show();
                    startService(new Intent(getBaseContext(), newsDownloadService.class));
                    break;
            }
        }
    }

    private void saveImageToFileManager(Bitmap image, int newsNumber) {
        File pictureFile = new File(getApplicationContext().getFilesDir(),"newssss"+newsNumber+".jpg");
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 30, fos);
            fos.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }catch (NullPointerException e){

        }
    }
}
