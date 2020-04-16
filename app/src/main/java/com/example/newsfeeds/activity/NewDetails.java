package com.example.newsfeeds.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsfeeds.R;
import com.example.newsfeeds.util.Constant;
import com.example.newsfeeds.util.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class NewDetails extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private ImageView mImageView;

    private TextView mAppbarTittle, mAppbarSubtittle, mDate, mTime, mTittle;

    private FrameLayout mDateBehavior;

    private LinearLayout mTittleAppbar;

    private AppBarLayout mAppBarLayout;

    private Toolbar mToolbar;

    private String sUrl, sImg, mTitle, sDate, sSource, sAuthor;

    private boolean isHideToolbarView = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        mAppBarLayout = findViewById(R.id.appbar);
        mAppBarLayout.addOnOffsetChangedListener(this);
        mDateBehavior = findViewById(R.id.date_behavior);

        mTittleAppbar = findViewById(R.id.title_appbar);
        mImageView = findViewById(R.id.backdrop);
        mAppbarTittle = findViewById(R.id.title_on_appbar);
        mAppbarSubtittle = findViewById(R.id.subtitle_on_appbar);
        mDate = findViewById(R.id.date);
        mTime = findViewById(R.id.time);
        mTittle = findViewById(R.id.title);

        Intent intent = getIntent();
        sUrl = intent.getStringExtra(Constant.KEY_URL);
        sImg = intent.getStringExtra(Constant.KEY_IMG);
        mTitle = intent.getStringExtra(Constant.KEY_TITTlE);
        sDate = intent.getStringExtra(Constant.KEY_DATE);
        sSource = intent.getStringExtra(Constant.KEY_SOURCE);
        sAuthor = intent.getStringExtra(Constant.KEY_AUTHOR);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());
        Glide.with(this)
                .load(sImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mImageView);

        mAppbarTittle.setText(sSource);
        mAppbarSubtittle.setText(sUrl);
        mDate.setText(Utils.DateFormat(sDate));
        mTittle.setText(mTitle);
        String author = "";
        if (sAuthor != null || sAuthor == "") {
            sAuthor = " \u2022 " + sAuthor;
        } else {
            author = "";
        }

        mTime.setText(sSource + author + " \u2022 " + Utils.DateToTimeFormat(sDate));
        initWebView(sUrl);
    }

    private void initWebView(String url) {
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScoll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScoll;

        if (percentage == 1.0 && isHideToolbarView) {
            mDateBehavior.setVisibility(View.GONE);
            mTittleAppbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
            //Log.d("tung", "onOffsetChanged: date_behavior GONE" + percentage);

        } else if (percentage < 1f && !isHideToolbarView) {
            mDateBehavior.setVisibility(View.VISIBLE);
            mTittleAppbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
            //Log.d("tung", "onOffsetChanged: date_behavior VISIBLE" + percentage );
        }
        //Log.d("tung", "onOffsetChanged:" + percentage );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.view_web) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(sUrl));
            startActivity(i);
            return true;
        } else if (id == R.id.share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plan");
                i.putExtra(Intent.EXTRA_SUBJECT, sSource);
                String body = mTitle + "n" + sUrl + "\n" + "Share form the News App" + "\n";
                i.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(i, "Share with :"));

            } catch (Exception e) {
                Log.d("Tung", "onOptionsItemSelected: cannot share");
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
