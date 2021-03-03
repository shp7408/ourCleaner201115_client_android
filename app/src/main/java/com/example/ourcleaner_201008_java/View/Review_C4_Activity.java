package com.example.ourcleaner_201008_java.View;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourcleaner_201008_java.Adapter.UriImageAdapter;
import com.example.ourcleaner_201008_java.DTO.ReviewDTO;
import com.example.ourcleaner_201008_java.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Review_C4_Activity extends AppCompatActivity {

    private static final String TAG = "리뷰작성4";

    //intent로 받아온 리뷰 객체
    ReviewDTO reviewDTO = new ReviewDTO();



    EditText reviewContentEdit;
    TextView letterNumTxt, imageNumTxt;

    RelativeLayout imagePlustLayout;

    RecyclerView recyclerView;
    int CODE_ALBUM_REQUEST = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review__c4_);
        Log.d(TAG, "=== onCreate ===" );

        /* intent로 리뷰 객체 받음 */
        Intent intent = getIntent();
        reviewDTO = (ReviewDTO) intent.getSerializableExtra("reviewDTO");

        reviewContentEdit = findViewById(R.id.reviewContentEdit);
        letterNumTxt = findViewById(R.id.letterNumTxt);
        imageNumTxt = findViewById(R.id.imageNumTxt);
        imagePlustLayout = findViewById(R.id.imagePlustLayout);

        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



        imagePlustLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, CODE_ALBUM_REQUEST);


//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                startActivityForResult(intent, CODE_ALBUM_REQUEST);
            }
        });





    }

    //앨범 이미지 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //갤러리 이미지 가져오기
        if (requestCode == CODE_ALBUM_REQUEST && resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "=== 갤러리 이미지 가져오기 ===" );
            ArrayList<Uri> uriList = new ArrayList<>();
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();

                if (clipData.getItemCount() > 5) { // 5개 초과하여 이미지를 선택한 경우

                    Toast.makeText(Review_C4_Activity.this, "사진은 5개까지 선택가능 합니다.", Toast.LENGTH_SHORT).show();
                    return;

                } else if (clipData.getItemCount() == 1) { //멀티선택에서 하나만 선택한 경우
                    Uri filePath = clipData.getItemAt(0).getUri();
                    uriList.add(filePath);
                    Log.d(TAG, "=== 멀티선택에서 하나만 선택한 경우 filePath ===" +filePath);

                } else if (clipData.getItemCount() > 1 && clipData.getItemCount() <= 5) { //1개초과  5개 이하의 이미지선택한 경우
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        uriList.add(clipData.getItemAt(i).getUri());
                        Log.d(TAG, "=== clipData.getItemAt(i).getUri() ===" +clipData.getItemAt(i).getUri());
                    }

                    Log.d(TAG, "=== uriList ===" + uriList.toString() );

                }
            }
            //리사이클러뷰에 보여주기
            UriImageAdapter adapter = new UriImageAdapter(uriList, Review_C4_Activity.this);
            recyclerView.setAdapter(adapter);
        }
    } //end of onActivityResult
}

