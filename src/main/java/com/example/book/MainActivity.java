package com.example.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.view.ContextMenu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.book.DBHelper.DATA_NAME;
import static com.example.book.DBHelper.TABLE_NAME;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ListAdapter listAdapter;
    private ListView listView;
    private Cursor cursor;
    private List<Words> wordsList = new ArrayList<>();
    private List<Words> queryList = new ArrayList<>();
    private EditText et_word, et_meaning, et_sample;
    private Button change, add, query;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        dbHelper = new DBHelper(MainActivity.this, DATA_NAME, null, 1);
        db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM wordlist", null);

        while (cursor.moveToNext()) {

            String ids = cursor.getString(cursor.getColumnIndex("id"));
            String words = cursor.getString(cursor.getColumnIndex("word"));
            String meanings = cursor.getString(cursor.getColumnIndex("meaning"));
            String samples = cursor.getString(cursor.getColumnIndex("sample"));
            Words words2 = new Words(ids, words, meanings, samples);
            wordsList.add(words2);
        }
            final Configuration configuration = this.getResources().getConfiguration();
            if (configuration.orientation == configuration.ORIENTATION_PORTRAIT) {
                et_word = (EditText) findViewById(R.id.etWord);
                et_meaning = (EditText) findViewById(R.id.etMeaning);
                et_sample = (EditText) findViewById(R.id.etSample);
                add = (Button) findViewById(R.id.bt_add);
                change = (Button) findViewById(R.id.bt_change);
                query = (Button) findViewById(R.id.bt_query);
                listView = (ListView) findViewById(R.id.list_view);

                listAdapter = new ListAdapter();
                listView.setAdapter(listAdapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        position = i;
                        et_word.setText(wordsList.get(i).getWord());
                        et_meaning.setText(wordsList.get(i).getMeaning());
                        et_sample.setText(wordsList.get(i).getSample());

                    }
                });    

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et_word.getText().length() > 1 &&
                                et_meaning.getText().length() > 0 &&
                                et_sample.getText().length() > 0) {

                            Words words1 = new Words();
                            words1.setWord(et_word.getText().toString());
                            words1.setMeaning(et_meaning.getText().toString());
                            words1.setSample(et_sample.getText().toString());
                            Log.d("MainActivity", words1.toString());

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("word", words1.getWord());
                            contentValues.put("meaning", words1.getMeaning());
                            contentValues.put("sample", words1.getSample());

                            db.insert("wordlist", null, contentValues);

                            String s = words1.getWord();
                            cursor = db.rawQuery("SELECT id FROM wordlist where word=?", new String[]{s});
                            while (cursor.moveToNext()) {
                                Log.d("MainActivity", cursor.getString(cursor.getColumnIndex("id")));
                                String id1 = cursor.getString(cursor.getColumnIndex("id"));
                                words1.setId(id1);
                            }
                            wordsList.add(words1);
                            listView.setAdapter(new MainActivity.ListAdapter());
                            //重置为空
                            et_word.setText("");
                            et_meaning.setText("");
                            et_sample.setText("");
                        }
                    }


                });

                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wordsList.get(position).setWord(et_word.getText().toString());
                        wordsList.get(position).setMeaning(et_meaning.getText().toString());
                        wordsList.get(position).setSample(et_sample.getText().toString());


                        ContentValues contentValues = new ContentValues();
                        contentValues.put("word",et_word.getText().toString() );
                        contentValues.put("meaning",et_meaning.getText().toString());
                        contentValues.put("sample", et_sample.getText().toString());
                        db.update("wordlist",contentValues,"id=" + wordsList.get(position).getId(),null);
                        listView.setAdapter(new MainActivity.ListAdapter());
                    }
                });

                query.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(et_word.getText().length()>0){
                            queryList.clear();
                            String s = et_word.getText().toString();
                            cursor = db.rawQuery("SELECT * FROM wordlist where word=?",new String[]{s});
                            String ids = cursor.getString(cursor.getColumnIndex("id"));
                            String words = cursor.getString(cursor.getColumnIndex("word"));
                            String meanings = cursor.getString(cursor.getColumnIndex("meaning"));
                            String samples = cursor.getString(cursor.getColumnIndex("sample"));
                            Words words3= new Words(ids,words,meanings,samples);
                            queryList.add(words3);
                            listView.setAdapter(new QueryListAdapter());
                        }
                    }
                });


            }



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.baidu:
                intent.setClass(MainActivity.this, BaiDuTranslate.class);
                startActivity(intent);
            case R.id.help:
                intent.setClass(MainActivity.this, help.class);
                startActivity(intent);



        }
        return super.onOptionsItemSelected(item);
    }

    public List<Words> getlist() {
        return wordsList;
    }

    private class ListAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return wordsList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null){
                view = View.inflate(getBaseContext(),R.layout.items,null);

            }
            else
                view = convertView;
            final Words w = wordsList.get(position);
            TextView word_view = (TextView)view.findViewById(R.id.iword);
            TextView mean_view = (TextView)view.findViewById(R.id.imeaning);
            TextView sample_view = (TextView)view.findViewById(R.id.isample);
            TextView delete = (TextView)view.findViewById(R.id.delete);


            word_view.setText(w.getWord());
            mean_view.setText(w.getMeaning());
            sample_view.setText(w.getSample());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        db.delete("wordlist","id="+ w.getId(),null);
                        wordsList.remove(position);
                        listView.setAdapter(new MainActivity.ListAdapter());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            return view;
        }

        }
    private class QueryListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return queryList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null){
                view = View.inflate(getBaseContext(),R.layout.items,null);

            }
            else
                view = convertView;
            Words w = queryList.get(position);
            TextView word_view = (TextView)view.findViewById(R.id.iword);
            TextView mean_view = (TextView)view.findViewById(R.id.imeaning);
            TextView sample_view = (TextView)view.findViewById(R.id.isample);


            word_view.setText(w.getWord());
            mean_view.setText(w.getMeaning());
            sample_view.setText(w.getSample());
            return view;
        }



    }
}
