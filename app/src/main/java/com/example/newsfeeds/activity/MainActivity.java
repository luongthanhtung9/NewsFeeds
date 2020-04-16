package com.example.newsfeeds.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newsfeeds.adapter.AdapterNew;
import com.example.newsfeeds.R;
import com.example.newsfeeds.util.Constant;
import com.example.newsfeeds.util.Utils;
import com.example.newsfeeds.api.ApiClient;
import com.example.newsfeeds.api.ApiInterface;
import com.example.newsfeeds.models.Article;
import com.example.newsfeeds.models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private TextView mTopHeadLine;
    private TextView mErrorTitle, mErrorMessage;

    private ImageView mErrorImage;

    private Button mBtnRetry;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Article> mArticles = new ArrayList<>();

    private AdapterNew mAdapterNew;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RelativeLayout mErrorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mTopHeadLine = findViewById(R.id.topheadlines);
        mRecyclerView = findViewById(R.id.recycleView);

        mErrorLayout = findViewById(R.id.errorLayout);
        mErrorImage = findViewById(R.id.errorImage);
        mErrorTitle = findViewById(R.id.errorTitle);
        mErrorMessage = findViewById(R.id.errorMessage);
        mBtnRetry = findViewById(R.id.btnRetry);

        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);

        mAdapterNew = new AdapterNew(mArticles, MainActivity.this);
        mRecyclerView.setAdapter(mAdapterNew);
        mAdapterNew.notifyDataSetChanged();

        onLoadingSwipeRefresh("");
    }

    public void LoadJson(final String keyWord) {
        mErrorLayout.setVisibility(View.GONE);
        mTopHeadLine.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        String country = Utils.getCountry();
//String language = Utils.getLanguage();
        Call<News> call;

        if (keyWord.length() > 0) {
            call = apiInterface.getNewsSearch(keyWord, Constant.SORT_BY, Constant.API_KEY);
        } else {
            call = apiInterface.getNews(country, Constant.API_KEY);
        }


        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    if (!mArticles.isEmpty()) {
                        mArticles.clear();
                    }


                    mArticles = response.body().getArticles();
                    //Log.d(Constant.TAG, "onResponse: " + response.body().getArticles());
                    mAdapterNew.updateNews(mArticles);

                    initListener();

                    mTopHeadLine.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mTopHeadLine.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);

                    String errorCode;
                    switch (response.code()) {
                        case 404:
                            errorCode = Constant.ERROR_404;
                            break;
                        case 500:
                            errorCode = Constant.ERROR_500;
                            break;
                        default:
                            errorCode = Constant.ERROR_UNKNOW;
                            break;
                    }

                    showErrorMessage(R.drawable.no_result,
                            Constant.ERROR_TITTLE,
                            Constant.ERROR_MESSAGE + "\n" + errorCode);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                mTopHeadLine.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
                showErrorMessage(R.drawable.no_result,
                        Constant.FAILURE_TITTLE,
                        Constant.FAILURE_MESSAGE + "\n" + t.toString());
            }
        });
    }


    private void initListener() {
        Log.d("Test", "initListener: 1");
        mAdapterNew.setOnItemClickListener(new AdapterNew.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ImageView imageView = v.findViewById(R.id.img);
                Intent intent = new Intent(MainActivity.this, NewDetails.class);

                Article article = mArticles.get(position);
                intent.putExtra(Constant.KEY_URL, article.getUrl());
                intent.putExtra(Constant.KEY_TITTlE, article.getTitle());
                intent.putExtra(Constant.KEY_IMG, article.getUrlToImage());
                intent.putExtra(Constant.KEY_SOURCE, article.getSource().getName());
                intent.putExtra(Constant.KEY_AUTHOR, article.getAuthor());
                intent.putExtra(Constant.KEY_DATE, article.getPublishedAt());

                Pair<View, String> pair = Pair.create((View) imageView, ViewCompat.getTransitionName(imageView));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, optionsCompat.toBundle());
                } else {
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem seachMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(Constant.SEARCH_TEXT_HINT);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 2) {
                    onLoadingSwipeRefresh(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        seachMenuItem.getIcon().setVisible(false, false);
        return true;
    }

    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private void onLoadingSwipeRefresh(final String keyWord) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                LoadJson(keyWord);
            }
        });
    }

    private void showErrorMessage(int imageView, String title, String message) {
        if (mErrorLayout.getVisibility() == View.GONE) {
            mErrorLayout.setVisibility(View.VISIBLE);
        }

        mErrorImage.setImageResource(imageView);
        mErrorTitle.setText(title);
        mErrorMessage.setText(message);

        mBtnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoadingSwipeRefresh("");
            }
        });
    }
}
