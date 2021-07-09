package com.reise.hessportsportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    ArrayList<GameModel> Games = new ArrayList<>();
    ArrayList<WebViewURL> WebViewUrl = new ArrayList<>();
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         /*
          setup the ads
         */
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdRequest adRequest = new AdRequest.Builder().build();

                InterstitialAd.load(MainActivity.this,"ca-app-pub-9524002965449333/3539858711", adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                });

            }
        });




        /*
          setup the ads
         */




        Content content = new Content();
        content.execute();



        //handle refreshing
        final SwipeRefreshLayout swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Content content = new Content();
                content.execute();

                swipe.setRefreshing(false);


            }
        });


    }




    public class Content extends AsyncTask<Void,Void,Void>{
         Dialog loading = new Dialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading.setCancelable(false);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            loading.setContentView(R.layout.load);

            loading.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            //recyclerview handler
            mRecyclerView = findViewById(R.id.idRecyclerVIew);
            recyclerAdapter adapter = new recyclerAdapter(Games,WebViewUrl);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(adapter);
            loading.dismiss();

            //use this at the loading moment
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {

            //handling page one
            String Url = "http://www.hesgoal.com/";
            String gameName;
            String gameHtmlLink;
            String startingTime;
            String gameThumbnailLink;




            try {
                Document doc = Jsoup.connect(Url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101").get();

                Elements table = doc.getElementsByClass("file file_index");



                for(Element elem : table) {

                    /*
                     * get sports games only
                     */
                    // String to be scanned to find the pattern.
                    String line = elem.select("p:nth-of-type(2)").text().replace(" ", "");
                    // Create a Pattern object
                    Pattern r = Pattern.compile("\\w+[:]\\w+");
                    Matcher m = r.matcher(line);

                    if (m.matches()) {
                        //first page results
                        gameName = elem.select("a[href]").text();//link name or text
                        gameHtmlLink = elem.select("a[href]").attr("href");//link
                        startingTime = elem.select("p:nth-of-type(2)").text()+" SAST";//starting time
                        gameThumbnailLink = elem.select("img").attr("src");//thumbnail


                        //Create game instances
                        Games.add(new GameModel(gameName,gameHtmlLink,startingTime,gameThumbnailLink));
                    }

                }

            } catch ( IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            for(GameModel game : Games) {

                //handling page one
                String url = game.getGameHtmlLink();

                try {
                    Document doc = Jsoup.connect(url).get();

                    Elements table = doc.getElementsByTag("iframe");



                    for(Element elem : table) {

                        //webview link src
                        String wvLink = elem.attr("src");

                        WebViewUrl.add(new WebViewURL(wvLink));

                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }


           return null;
        }
    }
}
