package com.example.ourcleaner_201008_java.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourcleaner_201008_java.CircleTransform;
import com.example.ourcleaner_201008_java.DTO.ManagerDTO;
import com.example.ourcleaner_201008_java.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SelectManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "매니저정보확인어댑터";

    private LayoutInflater inflater;
    private ArrayList<ManagerDTO> managerDTOArrayList = null;

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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /* 매니저 가이드 */
        if(viewType == Code.ViewType.MANAGER_SELECT_GUIDE){
            view = inflater.inflate(R.layout.row_select_manager_guide, parent, false);
            return new ManagerGuideViewHolder(view);
        }

        /* 매니저 정보 */
        else {
            view = inflater.inflate(R.layout.row_select_manager, parent, false);
            return new ManagerViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        /* 매니저 가이드 */
        if(holder instanceof ManagerGuideViewHolder){
            ((ManagerGuideViewHolder) holder).managerNameTxt.setText(managerDTOArrayList.get(position).getNameStr());
        }

        /* 매니저 정보 */
        else if(holder instanceof ManagerViewHolder){
            ((ManagerViewHolder) holder).managerNameTxt.setText(managerDTOArrayList.get(position).getNameStr());
//            ((ManagerViewHolder) holder).managerImgView.setImageResource(R.drawable.ic_baseline_person_24);

            /* cannot be null string 체크 시, */
            if(TextUtils.isEmpty(managerDTOArrayList.get(position).getImagePathStr())){
                Picasso.get()
                        .load(R.drawable.ic_baseline_person_24)
                        .transform(new CircleTransform())
                        .error(R.drawable.ic_baseline_person_24)
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .into(((ManagerViewHolder) holder).managerImgView);
            }else{
                Log.e(TAG, "=== managerImgView ===" +((ManagerViewHolder) holder).managerImgView.toString());
                Picasso.get()
                    .load(managerDTOArrayList.get(position).getImagePathStr())
                    .transform(new CircleTransform())
                    .into(((ManagerViewHolder) holder).managerImgView);
            }

        }

    }



//    @Override
//    public void onBindViewHolder(SelectManagerAdapter.MyViewHolder holder, int position) {
//        if(holder instanceof ManagerGuideViewHolder){
//            ((ManagerViewHolder) holder).managerNameTxt.setText(managerDTOArrayList.get(position).getNameStr());
//        }
//        else{
//            ((ManagerGuideViewHolder) holder).managerNameTxt.setText(managerDTOArrayList.get(position).getNameStr());
//        }
//
////        Picasso.get()
////                .load(managerDTOArrayList.get(position).getImagePathStr())
////                .transform(new CircleTransform())
////                .into(holder.managerImgView);
////
////
////        holder.managerNameTxt.setText(managerDTOArrayList.get(position).getNameStr());
////        holder.managerAddressTxt.setText(managerDTOArrayList.get(position).getAddressStr().substring(8,14));
////
////        holder.reviewScoreNumTxt.setText("("+"3"+"/5)  "+"2"+"건");
//
//    }

    @Override
    public int getItemCount() {
        return managerDTOArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return managerDTOArrayList.get(position).getViewType();
    }



    /* 매니저 가이드 */
    public class ManagerGuideViewHolder extends RecyclerView.ViewHolder{
        TextView managerNameTxt;

        public ManagerGuideViewHolder(@NonNull View itemView){
            super(itemView);
            managerNameTxt=itemView.findViewById(R.id.managerNameTxt);

        }
    }


    /* 매니저 정보 */
    public class ManagerViewHolder extends RecyclerView.ViewHolder{

        ImageView managerImgView;
        TextView managerNameTxt;

        public ManagerViewHolder(@NonNull View itemView){
            super(itemView);
            managerNameTxt=itemView.findViewById(R.id.managerNameTxt);
            managerImgView=itemView.findViewById(R.id.managerImgView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos  = getAdapterPosition();
                    listItemSelectedInterface.onItemSelected(view,getAdapterPosition());

                }
            });
        }
    }




//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView managerImgView;
//        TextView managerNameTxt,reviewScoreNumTxt, managerAddressTxt;
//        RatingBar ratingBar;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            managerImgView=itemView.findViewById(R.id.managerImgView);
//            managerNameTxt=itemView.findViewById(R.id.managerNameTxt);
//            ratingBar=itemView.findViewById(R.id.ratingBar);
//            reviewScoreNumTxt=itemView.findViewById(R.id.reviewScoreNumTxt);
//            managerAddressTxt=itemView.findViewById(R.id.managerAddressTxt);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos  = getAdapterPosition();
//                    listItemSelectedInterface.onItemSelected(view,getAdapterPosition());
//
//                }
//            });
//
//        }
//    }



}
