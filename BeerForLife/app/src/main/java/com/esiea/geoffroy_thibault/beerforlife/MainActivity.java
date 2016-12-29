package com.esiea.geoffroy_thibault.beerforlife;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.NotificationCompat;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    static List<Beer>  listOfBeers;
    MediaPlayer mp;
    DatePickerDialog dpd = null;

    NotificationCompat.Builder notif = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView tv = (TextView) findViewById(R.id.adresse);
        Calendar cal = Calendar.getInstance();

        tv.setText( cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" +  cal.get(Calendar.YEAR));
        mp = MediaPlayer.create(this, R.raw.sound);
        DatePickerDialog.OnDateSetListener odsl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                TextView tv = (TextView) findViewById(R.id.adresse);
                tv.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

            }
        } ;

        Button b = (Button) findViewById(R.id.button);
        b.setText(R.string.startMusic);
        b =  (Button) findViewById(R.id.button4);
        b.setText(R.string.download);
        b =  (Button) findViewById(R.id.button3);
        b.setText(R.string.launchNotif);
        b =  (Button) findViewById(R.id.button2);
        b.setText(R.string.chooseDate);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer != null){

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


        }

        dpd = new DatePickerDialog(this, odsl, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        notif = new NotificationCompat.Builder(this);
        notif.setSmallIcon(R.drawable.dickbutt);
        notif.setContentTitle("Beer is life");
        notif.setContentText("Beer is love");

        MyASyncTask task =  new MyASyncTask();
        task.execute();



    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(MainActivity.this, BeerListActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class MyASyncTask extends AsyncTask<Void,Void,Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            URL url = null;
            try{
                url = new URL("http://binouze.fabrigli.fr/bieres.json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                    copyInputStreamToFile(conn.getInputStream(),
                            new File(getCacheDir(), "bieres.json"));
                    return Boolean.TRUE;
                }
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Boolean.FALSE;
        }

        @Override
        protected void onPostExecute(Boolean result){
            Toast toast = null;
            if(result){
                toast = Toast.makeText(MainActivity.this, "Download réussi", Toast.LENGTH_SHORT);
            }else{
                toast = Toast.makeText(MainActivity.this, "Download raté", Toast.LENGTH_SHORT);
            }
            toast.show();

            JSONArray json = readJson("bieres.json");
            List<Beer> list = new ArrayList<Beer>();
            for(int i = 0; i < json.length(); i++){
                try {
                    list.add(Beer.createFromJson(json.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listOfBeers = list;
            for(Beer b : list){
                System.out.println(b.name);
            }

        }


    }
    public JSONArray readJson(String fileName){
        try{
            InputStream is = new FileInputStream(getCacheDir() + "/" + fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();

        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();

        }
    }

    public void startMusic(View v){
        if(!mp.isPlaying()){
            mp.start();
        }else{
            mp.pause();
        }

    }

    public void launchNotif(View v){
        NotificationManager notifMan = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notifMan.notify(1, notif.build());
    }

    public void chooseDate(View v){
        dpd.show();
    }

    public void startDownload(View v){

        MyASyncTask task =  new MyASyncTask();

        task.execute();

    }
    private class BeersAdapter extends RecyclerView.Adapter<BeersAdapter.BeerHolder>{
        private List<Beer> beers;
        BeersAdapter(List<Beer> l ){
            this.beers = l;
        }
        @Override
        public BeerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater lf = LayoutInflater.from(MainActivity.this);
            return new BeerHolder(lf.inflate(R.layout.beer_view, parent, false));
        }

        @Override
        public void onBindViewHolder(BeerHolder holder, int position) {
            holder.mTextView.setText(beers.get(position).name);
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

    private void copyInputStreamToFile(InputStream in, File file) {
        try{
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf))>0){
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
