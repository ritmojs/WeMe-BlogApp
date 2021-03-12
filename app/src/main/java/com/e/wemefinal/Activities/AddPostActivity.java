package com.e.wemefinal.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.e.wemefinal.Model.Blog;
import com.e.wemefinal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
private ImageButton mPostImage;
private EditText mPostTitle;
private EditText mPostDesc;
private Button mSubmitButton;
private FirebaseUser mUser;
private FirebaseAuth mAuth;
private DatabaseReference mPostDatebase;
private ProgressDialog mProgress;
private Uri mImageUri;
private StorageReference mStorage;
private static final int GALLARY_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mStorage= FirebaseStorage.getInstance().getReference();
        mProgress=new ProgressDialog(this);
        mPostDatebase=FirebaseDatabase.getInstance().getReference().child("Blog");
        mPostImage= findViewById(R.id.PostAdd);
        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLARY_CODE);
            }
        });
        mPostTitle= findViewById(R.id.PostTitle);
        mPostDesc= findViewById(R.id.PostDesp);
        mSubmitButton= findViewById(R.id.submitPost);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLARY_CODE &&resultCode==RESULT_OK)
        {
            mImageUri=data.getData();
            mPostImage.setImageURI(mImageUri);
        }
    }

    private void startPosting() {
        mProgress.setMessage("Posting to WeMe...");
        mProgress.show();
        final String titleVal=mPostTitle.getText().toString().trim();
        final String descVal=mPostDesc.getText().toString().trim();
        if(titleVal !="" && descVal!="" && mImageUri!=null)
        {

            StorageReference filepath= mStorage.child("Blog Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadurl = taskSnapshot.getUploadSessionUri();
                    DatabaseReference newPost=mPostDatebase.push();
                    Map<String,String> dataToSave=new HashMap<>();
                    dataToSave.put("title",titleVal);
                    dataToSave.put("desc",descVal);
                    dataToSave.put("image",downloadurl.toString());
                    dataToSave.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    dataToSave.put("userid",mUser.getUid());
                    newPost.setValue(dataToSave);
                    mProgress.dismiss();

                }
            });

        }
    }
}
