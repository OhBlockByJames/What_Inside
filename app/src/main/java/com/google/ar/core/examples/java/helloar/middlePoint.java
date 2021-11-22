package com.google.ar.core.examples.java.helloar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.ar.core.examples.java.common.helpers.Point;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class middlePoint extends Activity {
    private DatabaseReference rootRef;
    private DatabaseReference childRef;

    private ValueEventListener valueEventListener;
    private static final String TAG ="upload...";

    public float colors[];
    public float vertex_list[];
    private TextView name_input;

    public ArrayList<com.google.ar.core.examples.java.common.helpers.Point>pp;
    //firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    public DatabaseReference mDatabase;
    public DatabaseReference BuildingName;
    public DatabaseReference UserName;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myNextChild;
    private FirebaseUser user;

    public String building;
    public String userUIDcode;
    //暫存
    private ArrayList<Point> points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle_point);
        rootRef= FirebaseDatabase.getInstance().getReference();

        Intent lastIntent=getIntent();
        building=lastIntent.getStringExtra("buildingName");
        userUIDcode=lastIntent.getStringExtra("userUID");

        Button middlebtn=findViewById(R.id.midbutton);
        middlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_intent_to_OpenGL();
            }
        });

        //Authentication
        mAuth= FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mAuth.getCurrentUser();
                if(user!=null){
                    Log.d(TAG, "onAuthStateChanged:signed_in");
                }
                else{
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        //重點:用Valuelistener監聽資料庫狀態，再用datasnapshot(資料快照)抓下資料
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //init arraylist
                ArrayList<Integer> color=new ArrayList<>();
                ArrayList<Float> vertex=new ArrayList<>();

                DataSnapshot buildingSnapshot=snapshot.child(building);
                DataSnapshot userUID=buildingSnapshot.child(userUIDcode);
                for(DataSnapshot ds:userUID.getChildren()){
                    Double X = ds.child("x").getValue(Double.class);
                    if (X != null) {
                        float X1 = Float.valueOf(String.valueOf(X));
                        vertex.add(X1);
                    } else {
                        Log.d(TAG, "onDataChange:null happened");
                    }
                    Double Y = ds.child("y").getValue(Double.class);
                    if (Y != null) {
                        float Y1 = Float.valueOf(String.valueOf(Y));
                        vertex.add(Y1);
                    } else {
                        Log.d(TAG, "onDataChange: null expected");
                    }
                    Double Z = ds.child("z").getValue(Double.class);
                    if (Z != null) {
                        float Z1 = Float.valueOf(String.valueOf(Z));
                        vertex.add(Z1);
                    } else {
                        Log.d(TAG, "onDataChange: null happened");
                    }
                    Integer R = ds.child("r").getValue(Integer.class);
                    if (R != null) {
                        color.add(R);

                    }
                    Integer G = ds.child("g").getValue(Integer.class);
                    if (G != null) {
                        color.add(G);
                    }
                    Integer B = ds.child("b").getValue(Integer.class);
                    if (B != null) {
                        color.add(B);
                    }
                    Integer A = ds.child("a").getValue(Integer.class);
                    if (A != null) {
                        color.add(A);
                    }

                    colors = new float[color.size()];
                    vertex_list = new float[vertex.size()];

                    for (int i = 0; i <= color.size() - 1; i++) {
                        float c = color.get(i);
                        colors[i] = c / 255;
                    }
                    for (int i = 0; i <= vertex.size() - 1; i++) {
                        vertex_list[i] = vertex.get(i);
                    }

                }


                /*for (DataSnapshot snapChild : snapshot.getChildren()) {
                    for (DataSnapshot ds:snapChild.getChildren()) {
                        //XYZ照下面這樣拿就好，但我不知道為啥float變成了double了...可能要轉個檔...之類的...?
                        Double X = ds.child("x").getValue(Double.class);
                        if (X != null) {
                            float X1 = Float.valueOf(String.valueOf(X));
                            vertex.add(X1);
                        } else {
                            Log.d(TAG, "onDataChange:null happened");
                        }
                        Double Y = ds.child("y").getValue(Double.class);
                        if (Y != null) {
                            float Y1 = Float.valueOf(String.valueOf(Y));
                            vertex.add(Y1);
                        } else {
                            Log.d(TAG, "onDataChange: null expected");
                        }
                        Double Z = ds.child("z").getValue(Double.class);
                        if (Z != null) {
                            float Z1 = Float.valueOf(String.valueOf(Z));
                            vertex.add(Z1);
                        } else {
                            Log.d(TAG, "onDataChange: null happened");
                        }
                        Integer R = ds.child("r").getValue(Integer.class);
                        if (R != null) {
                            color.add(R);

                        }
                        Integer G = ds.child("g").getValue(Integer.class);
                        if (G != null) {
                            color.add(G);
                        }
                        Integer B = ds.child("b").getValue(Integer.class);
                        if (B != null) {
                            color.add(B);
                        }
                        Integer A = ds.child("a").getValue(Integer.class);
                        if (A != null) {
                            color.add(A);
                        }
                    }

                    colors = new float[color.size()];
                    vertex_list = new float[vertex.size()];

                    for (int i = 0; i <= color.size() - 1; i++) {
                        float c = color.get(i);
                        colors[i] = c / 255;
                    }
                    for (int i = 0; i <= vertex.size() - 1; i++) {
                        vertex_list[i] = vertex.get(i);
                    }
                }*/
            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d(TAG, "onCancelled: error.");
            }
        };
        rootRef.addListenerForSingleValueEvent(valueEventListener);
        TextView tv=findViewById(R.id.textView4);
        tv.append("You entered : "+building+"\n");
        tv.append("your userID is : "+userUIDcode+"\n");
    }
    public void pass_intent_to_OpenGL(){
        Intent intent=new Intent(this,OpenGLdemo.class);
        intent.putExtra("ColorArray",colors);
        intent.putExtra("Vertex_Array",vertex_list);
        startActivity(intent);
    };
}