package edu.ucsb.cs.cs185.jazariethach.cameraroll;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;

import android.view.ViewGroup;
import android.view.View;
import android.widget.*;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jazarie on 5/10/2015.
 */
public class MyAdapter
        extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<File> picList  = new ArrayList<File>();
    Context context;

    public MyAdapter(Context con, ArrayList<File> pics) {
        context = con;
        picList = pics;
    }

    public void updateList(ArrayList<File> pics) {
        picList = pics;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        public CardView cv;
        public View view;
        public File currentItem;


        public ViewHolder(View v) {
            super(v);
            iv = (ImageView) v.findViewById(R.id.image_view);
            cv = (CardView) v.findViewById(R.id.card_view);
            view = v;
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(context, MultiTouch.class);
                    Uri imageUri = Uri.fromFile(currentItem);
                    intent.putExtra("imageUri", imageUri.toString());
                    context.startActivity(intent);
                }
            });
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.adapter, parent, false); //or maybe main?

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uri uri = Uri.fromFile(picList.get(position));
        holder.iv.setImageURI(uri);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, context.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);

        int top = 0;
        int bottom = picList.size()- 1;

        if(position == top){
            layoutParams.setMargins(6, 6, 6, 3);
        }else if(position == bottom) {
            layoutParams.setMargins(6, 3, 6, 6);
        }else {
            layoutParams.setMargins(6, 3, 6, 3);
        }

        holder.cv.setLayoutParams(layoutParams);
        Context context = holder.iv.getContext();
        Picasso.with(context).load(picList.get(position)).resize(800, 400).centerCrop().into(holder.iv);
        holder.currentItem = picList.get(position);
    }

    @Override
    public int getItemCount() {
        return picList.size();
    }
}