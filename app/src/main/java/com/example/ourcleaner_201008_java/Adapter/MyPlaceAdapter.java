package com.example.ourcleaner_201008_java.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourcleaner_201008_java.DTO.MyplaceDTO;
import com.example.ourcleaner_201008_java.DTO.PlaceDTO;
import com.example.ourcleaner_201008_java.R;

import java.util.ArrayList;

public class MyPlaceAdapter extends RecyclerView.Adapter<MyPlaceAdapter.MyViewHolder> {

    private static final String TAG = "장소 넣는 어댑터";

    ArrayList<MyplaceDTO> myplaceDTOArrayList; //리사이클러 뷰에 넣을 데이터 객체 리스트

    Context mContext;
    //메인에서 뷰 객체를 클릭했을 때
    private OnListItemSelectedInterface mListener;

    //메인 뉴스피드의 각 객체를 클릭했을 때, 아이템 클릭 인터페이스
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);

    }

    //클릭 리스너 구현 시 필요한 어댑터 생성자
    public MyPlaceAdapter(Context context, ArrayList<MyplaceDTO> arrayList, OnListItemSelectedInterface listener){
        this.mContext = context;
        this.mListener = listener;
        this.myplaceDTOArrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_place, parent, false); //row xml 파일 연결하는 부분

        Log.d(TAG, "onCreateViewHolder start  :");

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // 뷰에 데이터 넣기
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "=== 뷰에 데이터 넣기 onBindViewHolder ===" );
        MyplaceDTO myplaceDTO = myplaceDTOArrayList.get(position);

        holder.addressDetailTxt.setText(myplaceDTO.getAddress()+" "+ myplaceDTO.getDetailAddress());
        holder.placeNameSizeTxt.setText(myplaceDTO.getPlaceNameStr()+"("+myplaceDTO.getSizeStr()+")");



    }






    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView placeNameSizeTxt; //장소 이름과 평수 스트링 값이 들어가야 함
        public TextView addressDetailTxt; //주소와 상세주소가 들어가야 함

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //itemView.을 붙이는 이유 : 25번 줄의 v가 부모 v는 아래 뷰홀더의 레이아웃 View 임.(super)
            placeNameSizeTxt = itemView.findViewById(R.id.placeNameSizeTxt);
            addressDetailTxt = itemView.findViewById(R.id.addressDetailTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    mListener.onItemSelected(view,getAdapterPosition());
                    Log.d(TAG, "position  :"+ getAdapterPosition());
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return myplaceDTOArrayList.size(); // 리사이클러 뷰의 전체 크기를 설정하는 부분
    }

}
