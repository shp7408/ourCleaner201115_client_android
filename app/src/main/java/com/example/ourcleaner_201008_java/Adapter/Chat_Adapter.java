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
import com.example.ourcleaner_201008_java.CircleTransform;
import com.example.ourcleaner_201008_java.DTO.ChatDTO;
import com.example.ourcleaner_201008_java.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/* 고객용화면에서
* 채팅방 */
public class Chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "Chat_Adapter";

    ArrayList<ChatDTO> chatDTOArrayList;
    Context mContext;


    public Chat_Adapter(ArrayList<ChatDTO> chatDTOArrayList, Context mContext) {
        this.chatDTOArrayList = chatDTOArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        /* 고객용 받은 메세지 */
        if(viewType == Code.ViewType.Client_Received_Message){
            view = inflater.inflate(R.layout.row_client_message_received, parent, false);
            return new ClientReceivedViewHolder(view);
        }

        /* 고객용 보낸 메세지 */
        else {
            view = inflater.inflate(R.layout.row_client_message_send, parent, false);
            return new ClientSendViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ChatDTO chatDTO = chatDTOArrayList.get(position);

        /* 고객용 받은 메세지 */
        if(holder instanceof ClientReceivedViewHolder){
            ((ClientReceivedViewHolder) holder).whoSendTxt.setText(chatDTO.getWhoSend());
//            ((ClientReceivedViewHolder) holder).receivedMessage.setText(chatDTO.getMsgStr());
            ((ClientReceivedViewHolder) holder).whoSendTimeTxt.setText(chatDTO.getGetTime());

            ((ClientReceivedViewHolder) holder).whoSendTimeTxt.setText(chatDTO.getGetTime());

            /* 고객이 받은 메세지 에서
            * 매니저의 프로필 */
            Picasso.get()
                    .load(chatDTOArrayList.get(position).getProfilePathStr())
                    .transform(new CircleTransform())
                    .into(((Chat_Adapter.ClientReceivedViewHolder) holder).managerProfile);

            /* 받은 메세지가 파일 경로인 경우 */
            if(chatDTO.getMsgStr().contains("uploads")){
                //메세지 내용이 이미지 경로 경우
                Log.d(TAG, "=== 메세지 내용이 이미지 경로 경우 ===" );
                ((ClientReceivedViewHolder) holder).receivedImg.setVisibility(View.VISIBLE);

                Glide.with(mContext)
                        .load(chatDTO.getMsgStr())
                        .into(((ClientReceivedViewHolder) holder).receivedImg);

                ((ClientReceivedViewHolder) holder).receivedMessage.setVisibility(View.GONE);

            }else{
                Log.d(TAG, "=== 메세지 내용 텍스트인 경우 ===" );
                ((ClientReceivedViewHolder) holder).receivedMessage.setVisibility(View.VISIBLE);
                ((ClientReceivedViewHolder) holder).receivedMessage.setText(chatDTO.getMsgStr());
                ((ClientReceivedViewHolder) holder).receivedImg.setVisibility(View.GONE);

            }

        }

        /* 고객용 보낸 메세지 */
        else if(holder instanceof ClientSendViewHolder){
//            ((ClientSendViewHolder) holder).sendMessage.setText(chatDTO.getMsgStr());
            ((ClientSendViewHolder) holder).sendTimeTxt.setText(chatDTO.getGetTime());

            if(chatDTO.getMsgStr().contains("uploads")){
                //메세지 내용이 이미지 경로 경우
                Log.d(TAG, "=== 메세지 내용이 이미지 경로 경우 ===" );
                ((ClientSendViewHolder) holder).sendImg.setVisibility(View.VISIBLE);


                Glide.with(mContext)
                        .load(chatDTO.getMsgStr())
                        .into(((ClientSendViewHolder) holder).sendImg);

                ((ClientSendViewHolder) holder).sendMessage.setVisibility(View.GONE);

            }else{
                Log.d(TAG, "=== 메세지 내용 텍스트인 경우 ===" );
                ((ClientSendViewHolder) holder).sendMessage.setVisibility(View.VISIBLE);
                ((ClientSendViewHolder) holder).sendMessage.setText(chatDTO.getMsgStr());
                ((ClientSendViewHolder) holder).sendImg.setVisibility(View.GONE);

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



    /* 고객용 받은 메세지 */
    public class ClientReceivedViewHolder extends RecyclerView.ViewHolder{

        /* 고객용 받은 메세지     상대방(매니저)가 보낸 메세지임!!!  */
        ImageView managerProfile;
        TextView whoSendTxt;
        TextView receivedMessage;
        TextView whoSendTimeTxt;

        RelativeLayout receivedLayout; //딱히 필요 없을듯..?
        ImageView receivedImg;

        public ClientReceivedViewHolder(@NonNull View itemView){
            super(itemView);

            /* 고객용 받은 메세지 */
            managerProfile = itemView.findViewById(R.id.managerProfile);
            whoSendTxt = itemView.findViewById(R.id.whoSendTxt);
            receivedMessage = itemView.findViewById(R.id.receivedMessage);
            whoSendTimeTxt = itemView.findViewById(R.id.whoSendTimeTxt);

            receivedLayout = itemView.findViewById(R.id.receivedLayout);
            receivedImg = itemView.findViewById(R.id.receivedImg);

        }
    }


    /* 내(고객)가 보낸 메세지 */
    public class ClientSendViewHolder extends RecyclerView.ViewHolder{

        TextView sendMessage;
        TextView sendTimeTxt;

        RelativeLayout sendLayout; //딱히 필요 없을듯..?
        ImageView sendImg;

        public ClientSendViewHolder(@NonNull View itemView){
            super(itemView);

            /* 내(고객)가 보낸 메세지 */
            sendMessage = itemView.findViewById(R.id.sendMessage);
            sendTimeTxt = itemView.findViewById(R.id.sendTimeTxt);

            sendLayout = itemView.findViewById(R.id.sendLayout);
            sendImg = itemView.findViewById(R.id.sendImg);

        }
    }




}
