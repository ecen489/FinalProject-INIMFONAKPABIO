package com.example.inimfonakpabio.finalproject;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {

    private List<Exercise> exerciseList;
    private Context mContext;

    //handle images
    ArrayList<Integer> img_handler = new ArrayList<>();


    public RecAdapter(List<Exercise> listitems, Context context) {
        exerciseList = listitems;
        mContext = context;

        populate_images();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Exercise exercise = exerciseList.get(position);

        holder.textName.setText(exercise.exerciseName);
        holder.textMuscle.setText(exercise.muscleGroups);
        holder.thumbnail.setImageResource(img_handler.get(exercise.imageRes));

        final Intent intent = new Intent(mContext, GuideActivity.class);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        intent.putExtra("VIDEORES", R.raw.vid1);
                        mContext.startActivity(intent);
                    case 1:
                        intent.putExtra("VIDEORES", R.raw.vid2);
                        mContext.startActivity(intent);
                    case 2:
                        intent.putExtra("VIDEORES", R.raw.vid3);
                        mContext.startActivity(intent);
                    default:
                        //do nothing
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textMuscle;
        ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            textName = (TextView) itemView.findViewById(R.id.textName);
            textMuscle = (TextView) itemView.findViewById(R.id.textMuscle);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

    public void populate_images() {
        img_handler.add(-1);
        img_handler.add(R.drawable.img_4);
        img_handler.add(R.drawable.img_6);
        img_handler.add(R.drawable.img_16);
        img_handler.add(R.drawable.img_7);
    }
}
