package com.example.wallpaperunsplash_advancedandroid.fragmentMain;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wallpaperunsplash_advancedandroid.MainActivity;
import com.example.wallpaperunsplash_advancedandroid.R;
import com.example.wallpaperunsplash_advancedandroid.SigninActivity;
import com.example.wallpaperunsplash_advancedandroid.fragmentOfAccount.FavoriteFragment;
import com.example.wallpaperunsplash_advancedandroid.fragmentOfAccount.UploadedFragment;
import com.example.wallpaperunsplash_advancedandroid.models.FlashUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    private static final int RESULT_OK = -1;
    Uri imgUri;
    TextView txtIsNull, txtLogin, tvNameUser, tvTel, tvUserName;
    ImageView ivAvatar, btnLogout;
    TabLayout tabLayout;
    FloatingActionButton fab;
    FirebaseAuth mAuth;
    public static String userID, owner;
    FirebaseUser currentUser;
    FirebaseStorage fStore;
    StorageReference refStore;
    FirebaseDatabase fData;
    DatabaseReference root;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNameUser = view.findViewById(R.id.tvNameUser);
        tvTel = view.findViewById(R.id.tvTelUser);
        tvUserName = view.findViewById(R.id.tvUsername);
        owner = tvNameUser.getText().toString();
        txtIsNull = view.findViewById(R.id.isNullUser);
        txtLogin = view.findViewById(R.id.txtLogintoLogin);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        btnLogout = view.findViewById(R.id.btnLogout);

        tabLayout = view.findViewById(R.id.tabLayoutAccount);
        fab = view.findViewById(R.id.fab);

        fData = FirebaseDatabase.getInstance();
        fStore = FirebaseStorage.getInstance();
        refStore = fStore.getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //User information
        if(currentUser==null){
            btnLogout.setVisibility(GONE);
            tvUserName.setVisibility(GONE);
            tvTel.setVisibility(GONE);
            tvNameUser.setText("demo FlashApp");
            tabLayout.setVisibility(GONE);
            fab.setVisibility(GONE);
            txtLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SigninActivity.class);
                    startActivity(intent);
                }
            });
        } else{
            userID = currentUser.getUid();
            DatabaseReference dbRef = fData.getReference();
            dbRef.child("users").child(userID).get()
                    .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            FlashUser user = dataSnapshot.getValue(FlashUser.class);
                            tvNameUser.setText(user.getNameOfUser());
                            tvTel.setText("Tel: " + user.getTel());
                            tvUserName.setText(user.getEmail());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Cant get user's info", Toast.LENGTH_SHORT).show();
                }
            });
            txtIsNull.setVisibility(GONE);
            txtLogin.setVisibility(GONE);
            downloadAvatar(userID);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.signOut();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    getActivity().finish();
                    startActivity(intent);
                }
            });
            setFragment(0);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    setFragment(tab.getPosition());
                    if(tab.getPosition() == 0){
                        fab.setVisibility(VISIBLE);
                    }else {
                        fab.setVisibility(GONE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture(); // also upload photo to firebase
                //uploadPhoto(userID);
            }
        });
    }
    private void downloadAvatar(String Uid){
        StorageReference pathRef = refStore.child("avatar/"+Uid);
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ivAvatar);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Cant load avatar", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setFragment(int pos){
        Fragment fr = null;
        switch (pos){
            case 0:
                fr = new UploadedFragment();
                break;
            case 1:
                fr = new FavoriteFragment();
        }
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.contentAccount, fr);
        ft.commit();
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(intent, 1000);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imgUri = data.getData();
            uploadPhoto(userID, imgUri);
        }
    }
    private void uploadPhoto(String userID, Uri currentUri) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riverRef = refStore.child("photoUploaded/" + userID +"/"+randomKey);

        riverRef.putFile(currentUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riverRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                root = fData.getReference().child("users").child(userID).child("photoUploaded");
                                // imgLink = root.push().getKey();
                                root.child(randomKey).setValue(uri.toString());
                                progressDialog.dismiss();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Cant upload Photo!!!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0*snapshot.getBytesTransferred()/snapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    }
                });
    }
    private void deletePhoto(String userID, String keyId){
        StorageReference riverRef = refStore.child("photoUploaded/" + userID +"/"+keyId);

    }
}