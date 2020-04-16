package com.example.newsfeeds.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.newsfeeds.R;
import com.example.newsfeeds.util.Utils;
import com.example.newsfeeds.models.Article;

import java.util.List;

public class AdapterNew extends RecyclerView.Adapter<AdapterNew.MyViewHolder> {

    private List<Article> mArticles;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public AdapterNew(List<Article> articles, Context mContext) {
        this.mArticles = articles;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, mOnItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final MyViewHolder viewHolder = holder;
        Article model = mArticles.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(mContext)
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                viewHolder.mProgressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                viewHolder.mProgressBar.setVisibility(View.GONE);
                return false;
            }
        })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.mImageView);

        viewHolder.mTitle.setText(model.getTitle());
        viewHolder.mDesc.setText(model.getDescription());
        viewHolder.mAuthor.setText(model.getAuthor());
        viewHolder.mPublishedAt.setText(Utils.DateFormat(model.getPublishedAt()));
        viewHolder.mSource.setText(model.getSource().getName());
        viewHolder.mTime.setText(" \u2022 " + Utils.DateToTimeFormat(model.getPublishedAt()));

    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public void updateNews(List<Article> articles) {
        this.mArticles = articles;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitle, mDesc, mAuthor, mPublishedAt, mSource, mTime;

        ImageView mImageView;

        ProgressBar mProgressBar;

        OnItemClickListener mOnItemClickListener;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            mTitle = itemView.findViewById(R.id.item_title);
            mDesc = itemView.findViewById(R.id.item_desc);
            mAuthor = itemView.findViewById(R.id.item_author);
            mSource = itemView.findViewById(R.id.item_source);
            mTime = itemView.findViewById(R.id.time);
            mImageView = itemView.findViewById(R.id.item_img);
            mPublishedAt = itemView.findViewById(R.id.item_publishedAt);
            mProgressBar = itemView.findViewById(R.id.item_progress_load_photo);

            this.mOnItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
