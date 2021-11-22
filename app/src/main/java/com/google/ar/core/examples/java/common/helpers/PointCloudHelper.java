package com.google.ar.core.examples.java.common.helpers;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.opengl.Matrix;

import com.google.ar.core.Camera;
import com.google.ar.core.CameraIntrinsics;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Pose;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

public class PointCloudHelper {
    static final float FLT_EPSILON = 1.19209290E-07f;
    static final float MAX_DELTA = 1.0E-10f;
    static final float maxDistinct = 8000f;

    private static FloatBuffer CreateDirectBuffer(int size) {
        //做出buffer
        return ByteBuffer.allocateDirect(size).asFloatBuffer();
    }


    //region Refence DrawAR
    public static FloatBuffer CalcPointCloud(float[] viewProjMtx, int width, int height, int step) {
        //寬和高
        int wSize = width / step;
        int hSize = height / step;
        int size = wSize * hSize * 4 * 4;
        float[] worldMatrix = new float[16];
        FloatBuffer result = CreateDirectBuffer(size);
        Matrix.invertM(worldMatrix, 0, viewProjMtx, 0);
        float fHeight = (float) height;
        float fWidth = (float) width;
        for (int v = 0; v < height; v += step) {
            float y = (float) (fHeight - v) * 2f / fHeight - 1f;
            for (int u = 0; u < width; u += step) {
                float x = (float) u * 2f / fWidth - 1f;
                float[] nearScreenPoint = {x, y, -1f, 1f};
                float[] farScreenPoint = {x, y, 1f, 1f};
                float[] nearPlanPoint = new float[4];
                float[] farPlanPoint = new float[4];
                Matrix.multiplyMV(nearPlanPoint, 0, worldMatrix, 0, nearScreenPoint, 0);
                Matrix.multiplyMV(farPlanPoint, 0, worldMatrix, 0, farScreenPoint, 0);
                float[] direction = {
                        farPlanPoint[0] / farPlanPoint[3],
                        farPlanPoint[1] / farPlanPoint[3],
                        farPlanPoint[2] / farPlanPoint[3]
                };
                float[] origin = {
                        nearPlanPoint[0] / nearPlanPoint[3],
                        nearPlanPoint[1] / nearPlanPoint[3],
                        nearPlanPoint[2] / nearPlanPoint[3]
                };
                direction = sub(direction, origin);
                direction = normalize(direction);
                float[] worldPoint = add(origin, direction);
                result.put(worldPoint);
            }
        }
        return result;
    }

    public static float[] screenPointToRay(float x, float y, float d, int width, int height, float[] viewProjMtx) {
        float nord = d > 0 ? (float) d / maxDistinct : 0f;
        float[] nearScreenPoint = {
                x * 2f / (float) width - 1f,

                ((float) height - y) * 2f / (float) height - 1f,
//                (float)(y)*2f/(float)height-1f,
                -1f,
                1f
        };
        float[] farScreenPoint = {
                nearScreenPoint[0],
                nearScreenPoint[1],
                1f,
                1f
        };

        float[] invertedProjectionMatrix = new float[16];
        float[] nearPlanePoint = new float[4];
        float[] farPlanePoint = new float[4];

        Matrix.setIdentityM(invertedProjectionMatrix, 0);
        Matrix.invertM(invertedProjectionMatrix, 0, viewProjMtx, 0);
        Matrix.multiplyMV(nearPlanePoint, 0, invertedProjectionMatrix, 0, nearScreenPoint, 0);
        Matrix.multiplyMV(farPlanePoint, 0, invertedProjectionMatrix, 0, farScreenPoint, 0);
        float[] direction = {
                farPlanePoint[0] / farPlanePoint[3],
                farPlanePoint[1] / farPlanePoint[3],
                farPlanePoint[2] / farPlanePoint[3]
        };

        float[] origin = {
                nearPlanePoint[0] / nearPlanePoint[3],
                nearPlanePoint[1] / nearPlanePoint[3],
                nearPlanePoint[2] / nearPlanePoint[3]
        };

        direction = sub(direction, origin);
        direction = normalize(direction);
        if (d > 0) {
            direction = scale(direction, d / 1000f);
//            direction=scale(direction,1f);
        }
        float[] point = add(origin, direction);
        float[] result = {
                point[0],
                point[1],
                point[2],
                1f
        };
        return result;
    }

    private static float[] add(float[] vector1, float[] vector2) {
        float[] result = new float[vector1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = vector1[i] + vector2[i];
        }
        return result;
    }

    private static float[] sub(float[] vector1, float[] vector2) {
        float[] result = new float[vector1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = vector1[i] - vector2[i];
        }
        return result;
    }

    private static float[] normalize(float[] vector) {
        float[] result = new float[vector.length];
        float pow = 0;
        for (int i = 0; i < vector.length; i++) {
            pow += vector[i] * vector[i];
        }
        float norm = 1f / (float) Math.sqrt(pow);
        for (int i = 0; i < vector.length; i++) {
            result[i] = vector[i] * norm;
        }
        return result;
    }

    private static float[] scale(float[] vector, float distinct) {
        float[] result = new float[vector.length];
        for (int i = 0; i < vector.length; i++) {
            result[i] = vector[i] * distinct;
        }
        return result;
    }
    //endregion

    //region 2D Rotate
    public static int[] Rotate(int x, int y, float deg) {
        double radians = Math.toRadians(deg);

        int[] result = {
                (int) (Math.cos(radians) * x - Math.sin(radians) * y),
                (int) (Math.sin(radians) * x + Math.cos(radians) * y)
        };
        return result;
    }
    //endregion

    //region Color Space
    public static byte[] YUVtoRGB(byte y, byte u, byte v) {
        int U = u - 128;
        int V = v - 128;
        byte[] result = {
                (byte) (y + (1.4075 * U)),
                (byte) (y - (03.455 * U - 0.7169 * V)),
                (byte) (y + 1.7790 * U)
        };
        return result;
    }

    public static byte[] RGBtoYUV(byte r, byte g, byte b) {

        byte[] result = {
                (byte) (r * 0.299 + g * 0.587 + b * 0.114),
                (byte) (r * -0.169 + g * -0.332 + b * 0.5 + 128),
                (byte) (r * 0.5 + g * -0.419 + b * -0.0813 + 128)
        };
        return result;
    }

    public static void WriteImageToSD(Context context, Image img) {
        File root = context.getExternalFilesDir(null).getAbsoluteFile();
        File path = new File(root.getAbsolutePath() + "/AR");
        if (path.exists() == false) {
            path.mkdir();
        }
        File imgFile = new File(path, String.format("AR_%d.jpg", img.getTimestamp()));
        try (FileOutputStream out = new FileOutputStream(imgFile)) {
            byte[] nv21;
            ByteBuffer yBuffer = img.getPlanes()[0].getBuffer();
            ByteBuffer uBuffer = img.getPlanes()[1].getBuffer();
            ByteBuffer vBuffer = img.getPlanes()[2].getBuffer();

            int ySize = yBuffer.remaining();
            int uSize = uBuffer.remaining();
            int vSize = vBuffer.remaining();

            nv21 = new byte[ySize + uSize + vSize];

            //U and V are swapped
            yBuffer.get(nv21, 0, ySize);
            vBuffer.get(nv21, ySize, vSize);
            uBuffer.get(nv21, ySize + vSize, uSize);

            YuvImage yuv = new YuvImage(nv21, ImageFormat.NV21, img.getWidth(), img.getHeight(), null);
            yuv.compressToJpeg(new Rect(0, 0, img.getWidth(), img.getHeight()), 100, out);

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
//            messageSnackbarHelper.showMessage(this,e.getMessage());
        }

    }
//endregion

}
