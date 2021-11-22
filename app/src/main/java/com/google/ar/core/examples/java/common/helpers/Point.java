package com.google.ar.core.examples.java.common.helpers;

//自定義了一個點-可以儲存各種資料
public class Point {
    public float x;
    public float y;
    public float z;
    public int a;
    public int r;
    public int g;
    public int b;
    public Point(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Point(float x, float y, float z, int a, int r, int g, int b) {
        this.x=x;
        this.y=y;
        this.z=z;
        this.a=a;
        this.r=r;
        this.g=g;
        this.b=b;
    }
}
