package com.mc2022.template;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newsFragment extends Fragment {
    TextView t1,t2;
    ImageView i1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public newsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static newsFragment newInstance(String param1, String param2) {
        newsFragment fragment = new newsFragment();
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
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        String newsBody, newsTitle;
        Bitmap b;
        int newsNumber = getArguments().getInt("newsNumber");
        Bitmap bMap = BitmapFactory.decodeFile(String.valueOf(new File(getContext().getFilesDir(),"newssss"+newsNumber+".jpg")));

        if (savedInstanceState != null) {
            newsTitle = savedInstanceState.getString("newsTitle");
            newsBody = savedInstanceState.getString("newsBody");
        }else {
            newsTitle = getArguments().getString("newsTitle");
            newsBody = getArguments().getString("newsBody");
        }

        t1=view.findViewById(R.id.newsTitleTextView);
        t2=view.findViewById(R.id.newsDescriptionTextView);
        i1=view.findViewById(R.id.imageView2);

        t1.setText(newsTitle);
        i1.setImageBitmap(bMap);
        t2.setText(newsBody);

        return view;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String s = getArguments().getString("newsTitle");
        String s1 = getArguments().getString("newsBody");
        outState.putString("newsTitle",s);
        outState.putString("newsBody",s1);
    }
}