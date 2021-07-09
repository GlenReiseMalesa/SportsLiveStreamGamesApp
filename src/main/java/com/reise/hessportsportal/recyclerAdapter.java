package com.reise.hessportsportal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private ArrayList<GameModel> mGameModels;
    private ArrayList<WebViewURL> mWebViewUrl;
    private int mPos;
    private MyViewHolder mHol;
    private Bitmap mBitmap;
    private Context mContext;

    public recyclerAdapter(ArrayList<GameModel> mGameModels,ArrayList<WebViewURL> mWebViewUrl){
        this.mGameModels = mGameModels;
        this.mWebViewUrl = mWebViewUrl;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView gameName;
        private TextView startingTime;
        private ImageView imageView;
        private CardView  mCardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            gameName = itemView.findViewById(R.id.idGameName);
            startingTime = itemView.findViewById(R.id.idStartingTime);
            imageView = itemView.findViewById(R.id.idImageView);
            mCardView = itemView.findViewById(R.id.idCardView);

            mContext = itemView.getContext();
        }
    }


    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, final int position) {
        mPos = position;
        mHol = holder;
        String gameName = mGameModels.get(position).getGameName();
        String startingTime = mGameModels.get(position).getStartingTime();
        String url = mGameModels.get(position).getGameThumbnailLink();




        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext,WebviewActivity.class);
                intent.putExtra("webViewUrl", mWebViewUrl.get(position).getWebViewUrl());
                mContext.startActivity(intent);
            }

        });

        new GetImageFromUrl(holder.imageView).execute(url);
        holder.gameName.setText(gameName);
        holder.startingTime.setText(startingTime);


    }


    public class GetImageFromUrl extends AsyncTask<String,Void,Bitmap>{

        ImageView mImageView;

        public GetImageFromUrl(ImageView img){
            this.mImageView = img;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            mBitmap = null;
            InputStream inputStream;

            try {
                inputStream = new URL(stringUrl).openStream();
                mBitmap =BitmapFactory.decodeStream(inputStream);
            }catch (IOException e){
                e.printStackTrace();
            }

            return mBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return mGameModels.size();
    }
}
