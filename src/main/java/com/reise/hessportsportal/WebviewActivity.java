package com.reise.hessportsportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.jsoup.internal.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebviewActivity extends AppCompatActivity {

    private WebView mWebView;
    private URL mWebUrl;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        mWebView = findViewById(R.id.idWebView);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("webViewUrl");


        String[] splitUrl = mUrl.split("//");
        mUrl = splitUrl[1];


        mWebView.setWebViewClient(new MyBrowser());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        mWebView.loadUrl(mUrl);


    }

    private class MyBrowser extends WebViewClient {




        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (isValidUrl(url)) {
                return false;
            }
            return true;

        }

        private boolean isValidUrl(String url) {

            List<String> validUrls = new ArrayList<>();

            validUrls.add("https://www\\.google\\.com/*");
            validUrls.add("https://www\\.facebook\\.com/*");

            for (String validUrl : validUrls) {
                Pattern pattern = Pattern.compile(validUrl, Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                    return true;
                }
            }
            return false;
        }


    }
}
