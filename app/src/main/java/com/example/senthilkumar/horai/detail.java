package com.example.senthilkumar.horai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class detail extends AppCompatActivity {

    int pos = 0;

    GetHoraiList getHoraiList = new GetHoraiList();
    IndepthCalc indepthCalc = new IndepthCalc();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        pos = i.getExtras().getInt("Position");

        ArrayList<HashMap> list = getHoraiList.get(this);
        HashMap item = list.get(pos);
        loadGrid(item);

    }

    private void loadGrid(HashMap item) {
        ListView listView = findViewById(R.id._horaiDetailTimes);
        ArrayList<HashMap> list = indepthCalc.getList(item);
        listviewAdapter adapter = new listviewAdapter(this, list);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

        return  true;
    }
}