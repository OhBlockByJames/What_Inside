package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class BrowsingActivity extends Activity {

    private static final String TAG = "Snapshots";
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private MyAdapter myAdapter;
    private ArrayList<Location> list;
    private ArrayList<String>b;
    private ArrayList<String>u;
    private String[] buildinglist;
    private String[] usersList;
    public Object object;
    public String currentSelectedBuilding;
    public String userUID;
    public Spinner buildings;
    public ArrayAdapter<String> ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase= FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing);

        //get the spinner from the xml.
        buildings = findViewById(R.id.BuildingSpinner);

        recyclerView = findViewById(R.id.LocationList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                b=new ArrayList<>();
                for (DataSnapshot buildingSnapshots : snapshot.getChildren()){
                    String buildingName=buildingSnapshots.getKey();
                    if (buildingName!=" message") {
                        b.add(buildingName);
                    }
                }
                myAdapter.notifyDataSetChanged();
                buildinglist= new String[b.size()];
                for(int i=0;i<b.size();i++){
                    buildinglist[i]=b.get(i);
                }
                ad = new ArrayAdapter<String>(BrowsingActivity.this, android.R.layout.simple_spinner_item,buildinglist);
                buildings.setAdapter(ad);
                buildings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        u=new ArrayList<>();
                        object=parent.getSelectedItem();
                        String firstNode=object.toString();
                        currentSelectedBuilding=firstNode;
                        DataSnapshot users=snapshot.child(firstNode);
                        for(DataSnapshot ds:users.getChildren()){
                            u.add(ds.getKey());
                        }
                        usersList=new String[u.size()];
                        for(int i=0;i<u.size();i++){
                            usersList[i]=u.get(i);
                        }
                        //看看有沒有取值?
                        checkIfIntentPassed();
                        setLowerSpinners();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BrowsingActivity.this, "Failed to read value",
                        Toast.LENGTH_SHORT).show();
            }
        });
        Button deleteBtn=findViewById(R.id.delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.removeValue();
                Toast.makeText(BrowsingActivity.this, "刪除資料庫成功",
                        Toast.LENGTH_LONG).show();
            }
        });
        Button browseBtn=findViewById(R.id.browse_advance);
        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMiddlePoint();
            }
        });
    }
    public void setLowerSpinners(){
        Spinner users = findViewById(R.id.ClassroomSpinner);
        ArrayAdapter<String> us = new ArrayAdapter<String>(BrowsingActivity.this, android.R.layout.simple_spinner_item,usersList);
        users.setAdapter(us);
        //點擊listener
        users.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                object=parent.getSelectedItem();
                userUID=object.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void goToMiddlePoint(){
        Intent intent = new Intent(this, middlePoint.class);
        intent.putExtra("userUID", userUID);
        intent.putExtra("buildingName",currentSelectedBuilding);
        startActivity(intent);
    }
    public void checkIfIntentPassed(){
        Intent intent=getIntent();
        //Check Gates
        String place=intent.getStringExtra("CheckPlace");
        Log.d(TAG, "checkIfIntentPassed: "+place);
        if(place!="None"){
            buildings.setSelection(ad.getPosition(place));
        };

    }
}