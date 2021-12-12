package com.google.ar.core.examples.java.helloar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;


import cn.pedant.SweetAlert.SweetAlertDialog;

public class MenuActivity extends Activity {

    Button logout;
    public CardView cardModel, cardBrowse, cardPosition;
    ImageButton leader,coupon;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        cardModel = (CardView) findViewById(R.id.modelingButton);
        cardBrowse = (CardView) findViewById(R.id.browsingButton);
        cardPosition = (CardView) findViewById(R.id.positioningButton);
        logout = (Button)findViewById(R.id.buttonLogout);
        leader = (ImageButton)findViewById(R.id.leaderIcon);
        coupon = (ImageButton)findViewById(R.id.couponIcon);

        leader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this,LeaderActivity.class);
                startActivity(intent);
            }
        });
        coupon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this,Coupon.class);
                startActivity(intent);
            }
        });

        cardModel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this,ScanActivity.class);
                startActivity(intent);
            }
        });

        cardBrowse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this,BrowsingActivity.class);
                intent.putExtra("CheckPlace","None");
                startActivity(intent);
            }
        });

        cardPosition.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //待補上定位的Activity
                Intent intent = new Intent(MenuActivity.this,MapActivity.class);
                startActivity(intent);
            }
        });
        TextView userStarView=(TextView)findViewById(R.id.starText);
        userStarView.setText(userStar.star+"");

        logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(MenuActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Well Done!")
                        .setContentText("You got 5 stars!")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                Intent intent = new Intent(MenuActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .show();

            }
        });
    }
}
