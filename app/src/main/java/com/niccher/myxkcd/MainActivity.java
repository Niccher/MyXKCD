package com.niccher.myxkcd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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
    private ImageView imview,popimg;
    private TextView popclos;
    String imgurl;

    ProgressDialog pds;
    Dialog myInfo;

    Context cnt;

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
        myInfo = new Dialog(this);

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

        imview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Poping();
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
                    Log.e("File Title >", doc.title() +"\n");
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

    private void LoadImage(final String urlPath){
        try {
            Picasso.get().load(urlPath).networkPolicy(NetworkPolicy.OFFLINE).into(imview, new Callback() {
                @Override
                public void onSuccess() {}

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(urlPath).into(imview);
                }
            });
        }catch (Exception ex){
            Log.e("-----LoadImage----", ex.getMessage()+"\n\n");
            Toast.makeText(this, "Missing Image data\n"+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Poping() {
        try {
            myInfo.setContentView(R.layout.part_poppa);
            popclos= myInfo.findViewById(R.id.popclose);
            popimg= myInfo.findViewById(R.id.popimg);
            popclos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myInfo.dismiss();
                }
            });

            try {
                Picasso.get().load(imgurl).networkPolicy(NetworkPolicy.OFFLINE).into(popimg, new Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(imgurl).into(popimg);
                    }
                });
            }catch (Exception ex){
                Log.e("-----Poping----", ex.getMessage()+"\n\n");
                Toast.makeText(this, "Missing Image data\n"+ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            myInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myInfo.show();
        }catch ( Exception pop){
            Toast.makeText(this, "Missing Image to display", Toast.LENGTH_SHORT).show();
        }
    }

}
