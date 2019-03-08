package com.example.projetmobdev.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projetmobdev.R;
import com.example.projetmobdev.model.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    private Context mContext;
    private List<Movie> movieList;

    public MoviesAdapter(Context mContext, List<Movie> movieList)
    {
        this.mContext=mContext;
        this.movieList=movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, int i){
        viewHolder.bind(movieList.get(i));
    }

    @Override
    public int getItemCount(){
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title,rating,genres,releaseDate;
        public ImageView thumbnail;
        public MyViewHolder(View view)
        {
            super(view);
            title= (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            rating = itemView.findViewById(R.id.item_movie_rating);
            genres = itemView.findViewById(R.id.item_movie_genre);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    /*int pos=getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                    {
                        Movie clickDataItem = movieList.get(pos);
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("original_title",movieList.get(pos).getOriginalTitle());
                        intent.putExtra("poster_path",movieList.get(pos).getPosterPath());
                        intent.putExtra("overview",movieList.get(pos).getOverview());
                        intent.putExtra("vote_average",Double.toString(movieList.get(pos).getVoteAverage()));
                        intent.putExtra("release_date",movieList.get(pos).getReleaseDate());
                        intent.putExtra("release_date",movieList.get(pos).getReleaseDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Toast.makeText(v.getContext(),"You clicked"+clickDataItem.getOriginalTitle(),Toast.LENGTH_SHORT);
                    }*/
                }
            });
        }
        public void bind(Movie movie)
        {
            releaseDate.setText(movie.getReleaseDate().split("-")[0]);
            rating.setText(String.valueOf(movie.getVoteAverage()));
            genres.setText("");
            title.setText(movie.getOriginalTitle());
            //userrating.setText(String.valueOf(movie.getVoteAverage()));

            Glide.with(mContext)
                    .load("http://image.tmdb.org/t/p/original"+movie.getPosterPath())
                    .into(thumbnail);
        }

    }
}
