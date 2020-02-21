package com.niccher.myxkcd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Button btn_prev, btn_next;
    private FloatingActionButton fab_sav;
    private ImageView imview;

    String imgurl;

    ProgressDialog pds;

    ArrayList<String> urlimg = new ArrayList<String>();
    ArrayList<String> urlname = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_prev = findViewById(R.id.btnprev);
        btn_next = findViewById(R.id.btnnext);
        fab_sav = findViewById(R.id.fabsavimg);
        imview = findViewById(R.id.imgvw);

        pds=new ProgressDialog(this);

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Activity under development", Toast.LENGTH_SHORT).show();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitParsers();
            }
        });

        fab_sav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Saving Image", Toast.LENGTH_SHORT).show();
            }
        });

        InitParsers();
    }

    private void InitParsers(){
        final String url = "https://c.xkcd.com/random/comic/";

        pds.setTitle("Loading new image");
        pds.setMessage("Please wait");
        //pds.create();
        pds.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Document doc = Jsoup.connect(url).get();
                    Log.e("File Title >", String.valueOf(doc.title())+"\n");
                    Elements divs=doc.select("div.box");//"a[src]"

                    String[] urllink,urlinmini;
                    String bulk=divs.text().replace("Image URL (for hotlinking/embedding):","").replace("|< < Prev Random Next > >| |< < Prev Random Next > >| Permanent link to this comic:","");

                    urllink=bulk.split("  ");
                    urlinmini=urllink[2].split(" ",4);

                    //Log.e("/*******/******/", bulk);
                    //Log.e("Url mini >", urllink[1]);
                    //Log.e("Url full >", urlinmini[0]);

                    urlimg.add(urlinmini[0]);
                    imgurl=urlinmini[0];

                } catch (IOException e) {
                    Log.e("-----Error----", e.getMessage()+"\n\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Starting  --> ","Start 00001");
                        pds.dismiss();
                        LoadImage(imgurl);
                    }
                });
            }
        }).start();
    }

    private void LoadImage(String urlPath){
        try {
            Glide.with(MainActivity.this)
                    .load(urlPath)
                    .into(imview);
        }catch (Exception ex){
            Log.e("-----LoadImage----", ex.getMessage()+"\n\n");
        }
    }

}
