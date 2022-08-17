package com.mc2022.template;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentTwo extends Fragment {
    Button b1,b2,b3;
    Context context;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmentTwo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentTwo newInstance(String param1, String param2) {
        fragmentTwo fragment = new fragmentTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        context = container.getContext();
        b1=view.findViewById(R.id.buttonOne);
        b2=view.findViewById(R.id.buttonTwo);
        b3=view.findViewById(R.id.secondActivityCallerButton);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(view);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(view);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = readNewsNumberFromFileManager();
                if(temp == null){
                    Toast.makeText(context.getApplicationContext(),"Please start service to download atleast 5 news",Toast.LENGTH_SHORT).show();
                }else{
                    int newsNumber = Integer.parseInt(temp);
                    if(newsNumber > 4){
                        Intent intentOne = new Intent(context.getApplicationContext(),topFiveNewsActivity.class);
                        startActivity(intentOne);
                    }else{
                        Toast.makeText(context.getApplicationContext(),"Please Start Service to Download atleast 5 news",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    public void startService(View view){
        Log.d("Application Status","Staring Service");
        getActivity().getBaseContext().startService(new Intent(getActivity().getBaseContext(),newsDownloadService.class));


    }

    public void stopService(View view){
        Log.d("Application Status","Stopping Service");
        getActivity().getBaseContext().stopService(new Intent(getActivity().getBaseContext(),newsDownloadService.class));
    }

    private String readNewsNumberFromFileManager() {
        String filename = "newsNumber.txt";
        String result = "";
        try {
            FileInputStream fin = context.getApplicationContext().openFileInput(filename);
            int a;
            StringBuilder output = new StringBuilder();
            while ((a = fin.read()) != -1) {
                output.append((char) a);
            }
            result = output.toString();
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return  result;
    }
}