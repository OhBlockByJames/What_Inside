package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLdemo extends Activity {
    private static final String TAG ="Intent";
    private GLSurfaceView mView;
    private Feature_Points mRenderer;

    public float colors[];
    public float vertex_list[];

    public float VertexArray[];
    public float ColorArray[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        Intent intent=getIntent();
        ColorArray=intent.getFloatArrayExtra("ColorArray");
        VertexArray=intent.getFloatArrayExtra("Vertex_Array");
        for(float f:VertexArray){
            Log.d(TAG, "onCreate: "+f);
        }
        mView = new GLSurfaceView(this);
        mRenderer = new Feature_Points(this);
        mView.setRenderer(mRenderer);
        setContentView(mView);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return mRenderer.onTouchEvent(event);
    }

    public class Feature_Points implements GLSurfaceView.Renderer {
        private Context mContext;
        private FloatBuffer mVertexBuffer = null;
        public float mAngleX = 0.0f;
        public float mAngleY = 0.0f;
        public float mAngleZ = 0.0f;
        private float mPreviousX;
        private float mPreviousY;
        private final float TOUCH_SCALE_FACTOR = 0.6f;
        private final float AXIS_SCALE_FACTOR = 1.5f;
        private final int AXIS_WIDTH = 10;
        private final float POINT_WIDTH = 20f;
        private int program;
        private int color_length;
        private int vertex_length;

        //建立顏色array(r,g,b,a)
        //宣告成全域變數


        //建立位置array(x,y,z)
        //宣告成全域變數


        public Feature_Points(Context context) {
            mContext = context;
        }

        public void onDrawFrame(GL10 gl) {
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();

            gl.glTranslatef(0.0f, 0.0f, -3.0f);
            gl.glRotatef(mAngleX, 1, 0, 0);
            gl.glRotatef(mAngleY, 0, 1, 0);
            gl.glRotatef(mAngleZ, 0, 0, 1);
            gl.glScalef(AXIS_SCALE_FACTOR, AXIS_SCALE_FACTOR, AXIS_SCALE_FACTOR);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
            //9/15 test
            /*float colors[]={
                    255,255,0,255,
                    255,255,0,255,
                    255,255,0,255,
                    255,255,0,255,
                    255,255,0,255,
                    55,255,0,255,
                    125,255,0,255,
                    255,0,0,255,
            };*/
            colors=ColorArray;
            Log.d("vertex length: ",""+vertex_length);
            //每四個r,g,b,a一組
            color_length=(colors.length/4)-1;
            Log.d("color length: ",""+color_length);

            //檢查點的座標數值是否和顏色數值相符
            if(color_length!=vertex_length){
                Log.d("OpenGL Error","座標和顏色組數不同");
            }
            //設定點大小
            gl.glPointSize(20);

            for(int index=0;index<=color_length;index++){

                //設定顏色 依序填入r,g,b,a
                gl.glColor4f(colors[index*4], colors[index*4+1],colors[index*4+2], colors[index*4+3]);
                /*
                GL10.GL_POINTS:繪製openGL類型:POINTS
                index:從array第幾項開始，利用for loop從0開始
                count:從array[index]開始數2個element進行繪製
                */
                gl.glDrawArrays(GL10.GL_POINTS,index,1);
            }
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
            gl.glEnable(GL10.GL_DEPTH_TEST);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

            // Get all the buffers ready
            setAllBuffers();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);
            float aspect = (float)width / height;
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustumf(-aspect, aspect, -1.0f, 1.0f, 1.0f, 10.0f);
        }

        private void setAllBuffers(){
            //9/15 test
            /*float vertex_list[]={
                    0.5f,0.5f,0.0f,1
            };*/
            vertex_list=VertexArray;
            ByteBuffer vbb = ByteBuffer.allocateDirect(vertex_list.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            mVertexBuffer = vbb.asFloatBuffer();
            mVertexBuffer.put(vertex_list);
            mVertexBuffer.position(0);
            vertex_length=(vertex_list.length/3)-1;
        }

        public boolean onTouchEvent(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float dx = x - mPreviousX;
                    float dy = y - mPreviousY;
                    mAngleY = (mAngleY + (int)(dx * TOUCH_SCALE_FACTOR) + 360) % 360;
                    mAngleX = (mAngleX + (int)(dy * TOUCH_SCALE_FACTOR) + 360) % 360;
                    break;
            }
            mPreviousX = x;
            mPreviousY = y;
            return true;
        }
    }
}