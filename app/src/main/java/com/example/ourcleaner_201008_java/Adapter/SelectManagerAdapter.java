package com.example.ourcleaner_201008_java.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ourcleaner_201008_java.CircleTransform;
import com.example.ourcleaner_201008_java.DTO.ManagerDTO;
import com.example.ourcleaner_201008_java.DTO.ReviewDTO;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.Manager.Manager_ProfileActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class SelectManagerAdapter extends RecyclerView.Adapter<SelectManagerAdapter.MyViewHolder> {

    private static final String TAG = "매니저정보확인어댑터";

    private LayoutInflater inflater;
    private ArrayList<ManagerDTO> managerDTOArrayList;

    public OnListItemSelectedInterface listItemSelectedInterface;
    
    Context context;


    //메인 뉴스피드의 각 객체를 클릭했을 때, 아이템 클릭 인터페이스
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);

    }

    public SelectManagerAdapter(Context context, ArrayList<ManagerDTO> managerDTOArrayList, OnListItemSelectedInterface listItemSelectedInterface) {
        inflater = LayoutInflater.from(context);
        this.managerDTOArrayList = managerDTOArrayList;
        this.listItemSelectedInterface = listItemSelectedInterface;
    }

    @NonNull
    @Override
    public SelectManagerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_select_manager,parent,false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectManagerAdapter.MyViewHolder holder, int position) {
//        Glide.with(context)
//                .load(managerDTOArrayList.get(position).getImagePathStr())
//                .circleCrop()
//                .into(holder.managerImgView);

        Picasso.get()
                .load(managerDTOArrayList.get(position).getImagePathStr())
                .transform(new CircleTransform())
                .into(holder.managerImgView);

//        Picasso.get().load(managerDTOArrayList.get(position).getImagePathStr())
//                .into(holder.managerImgView);


        holder.managerNameTxt.setText(managerDTOArrayList.get(position).getNameStr());

        // TODO: 2020-12-14 리뷰의 별점 가져와야 함
//        ArrayList<ReviewDTO> reviewDTOArrayList = managerDTOArrayList.get(position).getReviewDTOArrayList();
//        ArrayList<Float> arrayList = new ArrayList<>();;
//
//        for(int i=0; i<reviewDTOArrayList.size()-1; i++) {
//            Log.e(TAG, "=== 리뷰 어레이리스트 i ===" +i);
//
//            arrayList.add(reviewDTOArrayList.get(i).getStarScoreLong());
//            Log.e(TAG, "=== 리뷰 어레이리스트 getStarScoreLong ===" + reviewDTOArrayList.get(i).getStarScoreLong());
//            Log.e(TAG, "=== 리뷰 어레이리스트 arrayList ===" + arrayList.toString());
//        }

        holder.ratingBar.setNumStars(3);

        holder.reviewScoreNumTxt.setText("("+"3"+"/5)  "+"2"+"건");

        // TODO: 2020-12-14 디테일 후기에서 상위 4개 태그 넣을 것
    }

    @Override
    public int getItemCount() {
        return managerDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView managerImgView;
        TextView managerNameTxt, reviewTxt1, reviewTxt2, reviewTxt3, reviewTxt4, reviewScoreNumTxt;
        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            managerImgView=itemView.findViewById(R.id.managerImgView);

            managerNameTxt=itemView.findViewById(R.id.managerNameTxt);
            reviewTxt1=itemView.findViewById(R.id.reviewTxt1);
            reviewTxt2=itemView.findViewById(R.id.reviewTxt2);
            reviewTxt3=itemView.findViewById(R.id.reviewTxt3);
            reviewTxt4=itemView.findViewById(R.id.reviewTxt4);

            ratingBar=itemView.findViewById(R.id.ratingBar);

            reviewScoreNumTxt=itemView.findViewById(R.id.reviewScoreNumTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos  = getAdapterPosition();
                    listItemSelectedInterface.onItemSelected(view,getAdapterPosition());

                }
            });

        }
    }
}
