package com.example.wallpaperunsplash_advancedandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wallpaperunsplash_advancedandroid.models.FlashUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity {
    TextInputEditText tvEmail, tvPass, tvUsername, tvTel;
    Button btnCreate;
    FlashUser flUser;
    CircleImageView ivAvatar;
    Uri imgUri;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fDb;
    private FirebaseStorage fStore;
    private StorageReference refStore;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ivAvatar = findViewById(R.id.imgAvaCre);
        tvEmail = findViewById(R.id.tvEmailCre);
        tvPass = findViewById(R.id.tvPassCre);
        tvUsername = findViewById(R.id.tvUsernameCre);
        tvTel = findViewById(R.id.tvTelCre);
        btnCreate = findViewById(R.id.btnCreate);

        fStore = FirebaseStorage.getInstance();
        refStore = fStore.getReference();
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        fDb = FirebaseDatabase.getInstance();
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = tvEmail.getText().toString();
                password = tvPass.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Create Successful!!!", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    flUser = new FlashUser(user.getUid(), email, "avt1", tvUsername.getText().toString(), tvTel.getText().toString());
                                    DatabaseReference databaseReference = fDb.getReference();
                                    databaseReference.child("users").child(flUser.getUserID())
                                            .setValue(flUser)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignupActivity.this, "Cant put Info to Firebase", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    uploadAvatar(user.getUid());
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "cant create account", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imgUri = data.getData();
            ivAvatar.setImageURI(imgUri);
        }
    }

    private void uploadAvatar(String userID) {
        //final String randomKey = UUID.randomUUID().toString();
        StorageReference riverRef = refStore.child("avatar/"+userID);

        riverRef.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(SignupActivity.this, "Upload Avatar Okk", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "Cant upload Avatar!!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        //Toast.makeText(SignupActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}