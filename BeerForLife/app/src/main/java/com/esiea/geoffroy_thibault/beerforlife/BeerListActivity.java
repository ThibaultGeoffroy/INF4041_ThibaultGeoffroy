package com.esiea.geoffroy_thibault.beerforlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.nfc.NfcAdapter.EXTRA_ID;

/**
 * Created by thibaultgeoffroy on 27/12/2016.
 */

public class BeerListActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer_list_activity);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_bieres);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new BeersAdapter(MainActivity.listOfBeers));
    }


    public void viewBeerInfo(View v){
        Intent intent = new Intent(BeerListActivity.this,  BeerInfoActivity.class);
        intent.putExtra("EXTRA_ID" , Integer.toString((Integer) v.findViewById(R.id.beer_name).getTag()));
        startActivity(intent);
    }

    private class BeersAdapter extends RecyclerView.Adapter<BeersAdapter.BeerHolder>{
        private List<Beer> beers;
        BeersAdapter(List<Beer> l ){
            this.beers = l;
        }
        @Override
        public BeerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater lf = LayoutInflater.from(BeerListActivity.this);
            return new BeerHolder(lf.inflate(R.layout.beer_view, parent, false));
        }

        @Override
        public void onBindViewHolder(BeerHolder holder, int position) {
            holder.mTextView.setText(beers.get(position).name);
            holder.mTextView.setTag(beers.get(position).id);
        }

        @Override
        public int getItemCount() {
            return beers.size();
        }

        public class BeerHolder extends RecyclerView.ViewHolder{
            public TextView mTextView;
            public BeerHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.beer_name);
            }
        }
    }
}
