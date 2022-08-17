package com.mc2022.template;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class topFiveNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_five_news);

        String[] titles = new String[5];
        String[] bodies = new String[5];
        Bitmap[] images = new Bitmap[5];
        Bundle[] bundles = {new Bundle(),new Bundle(),new Bundle(),new Bundle(),new Bundle(),};
        int newsNumber = Integer.parseInt(readNewsNumberFromFileManager());
        int newsNumberBackup=newsNumber;
        Toast.makeText(this,"News Stored: "+newsNumber,Toast.LENGTH_LONG).show();
        for(int i=0;i<5;i++){
            titles[i]=readDataTitle(newsNumber);
            bodies[i]=readDataBody(newsNumber);
            images[i] = BitmapFactory.decodeFile(String.valueOf(new File(getApplicationContext().getFilesDir(),"newssss"+newsNumber+".jpg")));
            newsNumber--;
        }

        newsFragment[] fragments = {new newsFragment(),new newsFragment(),new newsFragment(),new newsFragment(),new newsFragment(),};
        for(int i=0;i<5;i++){
            bundles[i].putString("newsTitle", titles[i]);
            bundles[i].putString("newsBody",bodies[i]);
            bundles[i].putInt("newsNumber",newsNumberBackup);
            fragments[i].setArguments(bundles[i]);
            newsNumberBackup--;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.targetOne,fragments[0]);
        ft.replace(R.id.targetTwo,fragments[1]);
        ft.replace(R.id.targetThree,fragments[2]);
        ft.replace(R.id.targetFour,fragments[3]);
        ft.replace(R.id.targetFive,fragments[4]);
        ft.commit();

    }
    private String readNewsNumberFromFileManager() {
        String filename = "newsNumber.txt";
        String result = "";
        try {
            FileInputStream fin = getApplicationContext().openFileInput(filename);
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
}
