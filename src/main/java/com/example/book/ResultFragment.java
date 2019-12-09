package com.example.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {
    private TextView textView,example;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result,null);
        textView =(TextView)view.findViewById(R.id.meaning);
        example = (TextView)view.findViewById(R.id.sample);
        return view;
    }

    public void setText(String content1,String content2 ){
        textView.setText(content1);
        example.setText(content2);
    }
}
