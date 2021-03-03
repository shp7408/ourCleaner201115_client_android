package com.example.ourcleaner_201008_java.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ourcleaner_201008_java.R;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

public class UriImageAdapter extends RecyclerView.Adapter<UriImageAdapter.ItemViewHolder> {
    public ArrayList<Uri> albumImgList;
    public Context mContext;

    //생성자 정의
    public UriImageAdapter(ArrayList<Uri> albumImgList,Context mContext){
        this.albumImgList = albumImgList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UriImageAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_uri,parent,false);
        UriImageAdapter.ItemViewHolder viewHolder = new UriImageAdapter.ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UriImageAdapter.ItemViewHolder holder, int position) {
        //앨범에서 가져온 이미지 표시
        holder.imageView.setImageURI(albumImgList.get(position));
    }

    @Override
    public int getItemCount() {
        return albumImgList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
