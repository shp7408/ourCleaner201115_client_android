package com.example.ourcleaner_201008_java.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourcleaner_201008_java.DTO.AddressDTO;
import com.example.ourcleaner_201008_java.DTO.BaddressDTO;
import com.example.ourcleaner_201008_java.Model.ModelRecycler;
import com.example.ourcleaner_201008_java.R;

import java.util.ArrayList;

/* 레트로핏을 이용하여 서버에서 법정동 주소 받아오기 위한 어댑터 */
public class ReseachBAddressAdapter extends RecyclerView.Adapter<ReseachBAddressAdapter.MyViewHolder> {

    private static final String TAG = "법정동검색어댑터";


    private LayoutInflater inflater;
    private ArrayList<BaddressDTO> baddressDTOArrayList;


    public OnListItemSelectedInterface listItemSelectedInterface;

    //메인 뉴스피드의 각 객체를 클릭했을 때, 아이템 클릭 인터페이스
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);

    }


    public ReseachBAddressAdapter(Context ctx, ArrayList<BaddressDTO> baddressDTOArrayList){
        inflater = LayoutInflater.from(ctx);
        this.baddressDTOArrayList = baddressDTOArrayList;
    }

    public ReseachBAddressAdapter(Context ctx, ArrayList<BaddressDTO> baddressDTOArrayList, OnListItemSelectedInterface listItemSelectedInterface){
        inflater = LayoutInflater.from(ctx);
        this.baddressDTOArrayList = baddressDTOArrayList;
        this.listItemSelectedInterface=listItemSelectedInterface;
    }

    @NonNull
    @Override
    public ReseachBAddressAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_activeaddress, parent, false);
        ReseachBAddressAdapter.MyViewHolder holder = new ReseachBAddressAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReseachBAddressAdapter.MyViewHolder holder, int position) {

        holder.addressTxt.setText(baddressDTOArrayList.get(position).getSidoName()+" "
                +baddressDTOArrayList.get(position).getSigunguName()+" "
                +baddressDTOArrayList.get(position).getEupmyundongName()+" "
                +baddressDTOArrayList.get(position).getDongliName());

//        if(baddressDTOArrayList.get(position).getDongliName().isEmpty()){
//            Log.e(TAG, "=== 읍면동 없는 경우 ===" );
//            holder.detailAddressTxt.setVisibility(View.GONE);
//        }else{
//            Log.e(TAG, "=== 읍면동 있는 경우 ===" );
//            holder.detailAddressTxt.setVisibility(View.VISIBLE);
//            holder.detailAddressTxt.setText("관련 주소 : "+ baddressDTOArrayList.get(position).getDongliName());
//        }



    }

    @Override
    public int getItemCount() {
        return baddressDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView addressTxt, detailAddressTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            addressTxt=(TextView)itemView.findViewById(R.id.addressTxt);
            detailAddressTxt=(TextView)itemView.findViewById(R.id.detailAddressTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos  = getAdapterPosition();
                    listItemSelectedInterface.onItemSelected(view,getAdapterPosition());

                    Log.e(TAG, "=== getAdapterPosition ===" +getAdapterPosition());
                }
            });


        }
    }
}
