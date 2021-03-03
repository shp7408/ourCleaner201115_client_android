package com.example.ourcleaner_201008_java.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ourcleaner_201008_java.DTO.ChatDTO;
import com.example.ourcleaner_201008_java.R;

import java.util.ArrayList;

public class Manager_Chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "Manager_Chat_Adapter";

    ArrayList<ChatDTO> chatDTOArrayList;
    Context mContext;



    public Manager_Chat_Adapter(ArrayList<ChatDTO> chatDTOArrayList, Context mContext) {
        this.chatDTOArrayList = chatDTOArrayList;
        this.mContext = mContext;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        /* 매니저용 받은 메세지 */
        if(viewType == Code.ViewType.Manager_Received_Message){
            view = inflater.inflate(R.layout.row_manager_message_received, parent, false);
            return new Manager_Chat_Adapter.ManagerReceivedViewHolder(view);
        }

        /* 매니저용 보낸 메세지 */
        else {
            view = inflater.inflate(R.layout.row_manager_message_send, parent, false);
            return new Manager_Chat_Adapter.ManagerSendViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatDTO chatDTO = chatDTOArrayList.get(position);

        /* 매니저용 받은 메세지 */
        if(holder instanceof Manager_Chat_Adapter.ManagerReceivedViewHolder){
            ((Manager_Chat_Adapter.ManagerReceivedViewHolder) holder).whoSendTxt.setText(chatDTO.getWhoSend());
            ((Manager_Chat_Adapter.ManagerReceivedViewHolder) holder).whoSendTimeTxt.setText(chatDTO.getGetTime());

            if(chatDTO.getMsgStr().contains("uploads")){
                //메세지 내용이 이미지 경로 경우
                Log.d(TAG, "=== 메세지 내용이 이미지 경로 경우 ===" );
                ((Manager_Chat_Adapter.ManagerReceivedViewHolder) holder).receivedImg.setVisibility(View.VISIBLE);

//                Picasso.get()
//                        .load("http://52.79.179.66/uploads/1607755662978.png") ///var/www/html/uploads/0.png
//                        .into(((ManagerReceivedViewHolder) holder).receivedImg);
                Glide.with(mContext)
                        .load(chatDTO.getMsgStr())
                        .into(((ManagerReceivedViewHolder) holder).receivedImg);


                ((Manager_Chat_Adapter.ManagerReceivedViewHolder) holder).receivedMessage.setVisibility(View.GONE);

            }else{
                Log.d(TAG, "=== 메세지 내용 텍스트인 경우 ===" );
                ((Manager_Chat_Adapter.ManagerReceivedViewHolder) holder).receivedMessage.setVisibility(View.VISIBLE);
                ((Manager_Chat_Adapter.ManagerReceivedViewHolder) holder).receivedMessage.setText(chatDTO.getMsgStr());
                ((Manager_Chat_Adapter.ManagerReceivedViewHolder) holder).receivedImg.setVisibility(View.GONE);

            }


        }

        /* 매니저용 보낸 메세지 */
        else if(holder instanceof Manager_Chat_Adapter.ManagerSendViewHolder){
//            ((Manager_Chat_Adapter.ManagerSendViewHolder) holder).sendMessage.setText(chatDTO.getMsgStr());
            ((Manager_Chat_Adapter.ManagerSendViewHolder) holder).sendTimeTxt.setText(chatDTO.getGetTime());

            if(chatDTO.getMsgStr().contains("uploads")){
                //메세지 내용이 이미지 경로 경우
                Log.d(TAG, "=== 메세지 내용이 이미지 경로 경우 ===" );
                ((Manager_Chat_Adapter.ManagerSendViewHolder) holder).sendImg.setVisibility(View.VISIBLE);


                Glide.with(mContext)
                        .load(chatDTO.getMsgStr())
                        .into(((ManagerSendViewHolder) holder).sendImg);

                ((Manager_Chat_Adapter.ManagerSendViewHolder) holder).sendMessage.setVisibility(View.GONE);

            }else{
                Log.d(TAG, "=== 메세지 내용 텍스트인 경우 ===" );
                ((Manager_Chat_Adapter.ManagerSendViewHolder) holder).sendMessage.setVisibility(View.VISIBLE);
                ((Manager_Chat_Adapter.ManagerSendViewHolder) holder).sendMessage.setText(chatDTO.getMsgStr());
                ((Manager_Chat_Adapter.ManagerSendViewHolder) holder).sendImg.setVisibility(View.GONE);

            }
        }

    }

    @Override
    public int getItemCount() {
        return chatDTOArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chatDTOArrayList.get(position).getViewType();
    }


    /* 매니저용 받은 메세지 */
    public class ManagerReceivedViewHolder extends RecyclerView.ViewHolder{

        /* 매니저용 받은 메세지     상대방(매니저)가 보낸 메세지임!!!  */
        TextView whoSendTxt;
        TextView receivedMessage;
        TextView whoSendTimeTxt;

        RelativeLayout receivedLayout; //딱히 필요 없을듯..?
        ImageView receivedImg;

        public ManagerReceivedViewHolder(@NonNull View itemView){
            super(itemView);

            /* 고객용 받은 메세지 */
            whoSendTxt = itemView.findViewById(R.id.whoSendTxt);
            receivedMessage = itemView.findViewById(R.id.receivedMessage);
            whoSendTimeTxt = itemView.findViewById(R.id.whoSendTimeTxt);

            receivedLayout = itemView.findViewById(R.id.receivedLayout);
            receivedImg = itemView.findViewById(R.id.receivedImg);

        }
    }


    /* 매니저용 내가 보낸 메세지 */
    public class ManagerSendViewHolder extends RecyclerView.ViewHolder{

        TextView sendMessage;
        TextView sendTimeTxt;

        RelativeLayout sendLayout; //딱히 필요 없을듯..?
        ImageView sendImg;

        public ManagerSendViewHolder(@NonNull View itemView){
            super(itemView);

            /* 매니저용 내(매니저)가 보낸 메세지 */
            sendMessage = itemView.findViewById(R.id.sendMessage);
            sendTimeTxt = itemView.findViewById(R.id.sendTimeTxt);

            sendLayout = itemView.findViewById(R.id.sendLayout);
            sendImg = itemView.findViewById(R.id.sendImg);

        }
    }

}
