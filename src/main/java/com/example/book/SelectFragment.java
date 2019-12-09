package com.example.book;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import static com.example.book.R.*;

public class SelectFragment extends Fragment {
    static final String TAG = "SelectFragment";
    private ArrayList<String> wordlist;
    private ListView listView;
    private Cursor cursor;

    public SelectFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
    }
    //将一段 XML 资源文件加载成为 View
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(layout.select,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Words> w = ((MainActivity) getActivity()).getlist();
        final String[] word = new String[w.size()];
        final String[] meaning = new String[w.size()];
        final String[] sample = new String[w.size()];
        for(int i =0;i < w.size();i++) {
            word[i] = w.get(i).getWord();
            meaning[i] = w.get(i).getMeaning();
            sample[i] = w.get(i).getSample();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_activated_1,word);
        listView = (ListView)getView().findViewById(R.id.listView);
        listView.setAdapter(adapter);
        final List<Words> finalNames = w;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ResultFragment fragment = (ResultFragment)fm.findFragmentById(R.id.fragment_result);
                fragment.setText(meaning[i],sample[i]);
            }
        });
    }
}
