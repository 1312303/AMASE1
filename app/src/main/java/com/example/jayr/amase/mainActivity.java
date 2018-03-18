package com.example.jayr.amase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mainActivity extends AppCompatActivity {
    private TextView lbl;
    private ImageView mView;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private ProgressDialog progDialog;
    private ListView mInfoList;
    private Button btnReply;
    private Spinner mSpinnerPermit;
    RecyclerView myRv;
    String imageURL;
    Boolean change = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);
        mView = (ImageView)findViewById(R.id.iView);
        mInfoList = (ListView)findViewById(R.id.lView);
        btnReply = (Button)findViewById(R.id.btnSend);
        mSpinnerPermit = (Spinner)findViewById(R.id.spinner);
        myRv = (RecyclerView)findViewById(R.id.myRecycle);

        //FCM token

        progDialog = new ProgressDialog(this);
        progDialog.setMessage("Loading...");
        progDialog.setTitle("Fetching Data");
        progDialog.setProgressStyle(progDialog.STYLE_SPINNER);
        progDialog.show();
        db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference();
        //Load Data
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
                progDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                    Toast.makeText(mainActivity.this,info.getUrl(),Toast.LENGTH_SHORT).show();
                   if (change){
                      DatabaseReference dref =FirebaseDatabase.getInstance().getReference().child("Visitors").child("1").child("permission");
                      dref.setValue(mSpinnerPermit.getSelectedItem().toString());
                  } else {
                      Toast.makeText(mainActivity.this ,"Unable to change",Toast.LENGTH_LONG).show();
                  }

               } catch (Exception e){
                   e.printStackTrace();
               }

            }
        });

//
//https://firebasestorage.googleapis.com/v0/b/db-project-6b419.appspot.com/o/images%2Fjayr.jpg?alt=media&token=ec5beb95-6f32-4fce-8c66-adbdb68ab7e8
//https://firebasestorage.googleapis.com/v0/b/db-project-6b419.appspot.com/o/images%2Fexample.jpg?alt=media&token=423a7f38-8d5f-4b3a-85b3-2aef7f157669
//raymundo12231978
    }
    public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder>{
        List<infoClass> listarray;
        public void myAdapter(List<infoClass> list){
            this.listarray = list;
        }

        @Override
        public void onBindViewHolder(myViewHolder holder, int position) {
            infoClass data = new infoClass();
            holder.
        }

        @Override
        public myAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview,parent,false);
            return new myViewHolder(view);
        }
        public class myViewHolder extends RecyclerView.ViewHolder {
            public myViewHolder(View itemView) {
                super(itemView);

            }
        }

        @Override
        public int getItemCount() {
            return listarray.size();
        }
    }
    protected void onStart() {
        super.onStart();

    }
        infoClass info = new infoClass();
    private void showData(DataSnapshot dataSnapshot){
        for(DataSnapshot ds : dataSnapshot.getChildren()){

            info.setVisitorName(ds.child("1").getValue(infoClass.class).getVisitorName());
            info.setPermission(ds.child("1").getValue(infoClass.class).getPermission());
            info.setUrl(ds.child("1").getValue(infoClass.class).getUrl());
            info.setTenant(ds.child("1").getValue(infoClass.class).getTenant());

            Log.d("Show data:","Vname:" +info.getVisitorName());
            Log.d("Show data:","Permit" +info.getPermission());
            Log.d("Show data:","URL"+info.getUrl());
           // Glide.with(mainActivity.this)
           //          .load(info.getUrl())
           //          .fitCenter()
            //         .into(mView);
            //ArrayList<String> infoList = new ArrayList<>();
            //infoList.add(info.getVisitorName());
            //infoList.add(info.getPermission());
            //ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,infoList);
            //mInfoList.setAdapter(arrayAdapter);
        }
    }
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent login = new Intent(mainActivity.this,loginActivity.class);
                        startActivity(login);
                        progDialog.setTitle("Logging Out");
                        progDialog.show();
                        //infoList.clear();
                        auth.signOut();
                        mainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
//https://www.youtube.com/watch?v=qsKlbTHwBKY tut