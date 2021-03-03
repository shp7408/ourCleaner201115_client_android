package com.example.ourcleaner_201008_java.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourcleaner_201008_java.CircleTransform;
import com.example.ourcleaner_201008_java.DTO.MymanagerDTO;
import com.example.ourcleaner_201008_java.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/* 안씀.. */
public class MyManagerAdapter extends RecyclerView.Adapter<MyManagerAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<MymanagerDTO> mymanagerDTOArrayList;
    private LayoutInflater inflater;


    // 리스너 객체 참조를 저장하는 변수
    private AdapterView.OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public MyManagerAdapter(Context context, ArrayList<MymanagerDTO> mymanagerDTOArrayList) {
        this.context = context;
        this.mymanagerDTOArrayList = mymanagerDTOArrayList;
        inflater = LayoutInflater.from(context);
    }

//    public MyManagerAdapter(Context context, ArrayList<MymanagerDTO> mymanagerDTOArrayList, MyManagerAdapter.OnListItemSelectedInterface2 listItemSelectedInterface2) {
//        this.mymanagerDTOArrayList = mymanagerDTOArrayList;
//        inflater = LayoutInflater.from(context);
//        this.listItemSelectedInterface2 = (SelectManagerAdapter.OnListItemSelectedInterface) listItemSelectedInterface2;
//    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_mymanager, parent, false); //row xml 파일 연결하는 부분


        MyManagerAdapter.CustomViewHolder vh = new MyManagerAdapter.CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        MymanagerDTO mymanagerDTO = mymanagerDTOArrayList.get(position);
        holder.myManagerNameTxt.setText(mymanagerDTO.getNameStr());
        Picasso.get()
                .load(mymanagerDTO.getImagePathStr())
                .transform(new CircleTransform())
                .into(holder.myManagerImgView);
    }

//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//                MymanagerDTO mymanagerDTO = mymanagerDTOArrayList.get(position);
//        holder.myManagerNameTxt.setText(mymanagerDTO.getNameStr());
//        Picasso.get()
//                .load(mymanagerDTO.getImagePathStr())
//                .transform(new CircleTransform())
//                .into(holder.myManagerImgView);
//    }

//    @Override
//    public void onBindViewHolder(@NonNull MyManagerAdapter.CustomViewHolder holder, int position) {
//        MymanagerDTO mymanagerDTO = mymanagerDTOArrayList.get(position);
//        holder.myManagerNameTxt.setText(mymanagerDTO.getNameStr());
//        Picasso.get()
//                .load(mymanagerDTO.getImagePathStr())
//                .transform(new CircleTransform())
//                .into(holder.myManagerImgView);
//    }



    @Override
    public int getItemCount() {
        return mymanagerDTOArrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public ImageView myManagerImgView;
        public TextView myManagerNameTxt;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            myManagerImgView = itemView.findViewById(R.id.myManagerImgView);
            myManagerNameTxt = itemView.findViewById(R.id.myManagerNameTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos  = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
//                        // 리스너 객체의 메서드 호출.
//                        if (mListener != null) {
//                            mListener.onItemClick(view, pos) ;
//                        }
                    }
                }
            });
        }
    }
}
