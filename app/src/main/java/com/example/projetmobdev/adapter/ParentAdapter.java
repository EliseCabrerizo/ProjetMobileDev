package com.example.projetmobdev.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projetmobdev.R;
import com.example.projetmobdev.model.Movie;

import java.util.List;

    public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.MyViewHolder> {
        private Context mContext;
        private List<Movie> movieList;

        RecyclerView recyclerView;
        TextView titleSection;
        public ParentAdapter(Context mContext, List<Movie> movieList)
        {
        this.mContext=mContext;
        this.movieList=movieList;
        }


@Override
public ParentAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
        View view= LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.parent_recycler,viewGroup,false);
        return new MyViewHolder(view);
        }

@Override
public void onBindViewHolder(final ParentAdapter.MyViewHolder viewHolder, int i){
        if(i==0)
            titleSection.setText("POPULAR");
        else
            titleSection.setText("ESSAI");
        viewHolder.bind();
        }

@Override
public int getItemCount(){
        return movieList.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder{

    public MyViewHolder(View view)
    {
        super(view);
        titleSection = view.findViewById(R.id.titleSection);
        recyclerView= view.findViewById(R.id.rv_child);

    }
    public void bind()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new MoviesAdapter(mContext, movieList));

    }



}
}
