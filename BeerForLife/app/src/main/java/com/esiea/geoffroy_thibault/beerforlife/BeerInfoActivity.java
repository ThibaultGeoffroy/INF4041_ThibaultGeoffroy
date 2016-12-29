package com.esiea.geoffroy_thibault.beerforlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thibaultgeoffroy on 28/12/2016.
 */

public class BeerInfoActivity extends Activity {

    private int id;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer_info_activity);

        Intent intent = getIntent();

        if (intent != null) {
            this.id = Integer.parseInt(intent.getStringExtra("EXTRA_ID"));
        }

        List<Beer> list = MainActivity.listOfBeers;

        for(Beer b : list){
            if(b.id == this.id){
                ((TextView) findViewById(R.id.beer_name)).setText(this.getString(R.string.beerName) + ": "+ b.name);
                ((TextView) findViewById(R.id.beer_date)).setText(this.getString(R.string.beerDate) + ": "+ b.created_at);
                if(!b.note.equals("null")){
                    ((TextView) findViewById(R.id.beer_grade)).setText(this.getString(R.string.beerGrade)+ ": "+ b.note);
                }
                ((TextView) findViewById(R.id.beer_description)).setText(this.getString(R.string.beerDescription)+ ": " + b.description);
            }
        }
    }
}
