package com.example.fengs.campusattendance;

import android.graphics.Rect;
import android.util.Log;

import com.arcsoft.facedetection.AFD_FSDKEngine;
import com.arcsoft.facedetection.AFD_FSDKError;
import com.arcsoft.facedetection.AFD_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facetracking.AFT_FSDKEngine;
import com.arcsoft.facetracking.AFT_FSDKError;
import com.arcsoft.facetracking.AFT_FSDKFace;

import java.util.ArrayList;
import java.util.List;

public class FaceRecognition {

    private static String APPID = "FT6NUE7YNUTpsY8MU9MsYoDbznky3HGTCfrnZBkcgGtJ";
    private static String FT_KEY = "E2gjivyni7Cs54McJ2rvCjGZfKdBUZpPmxrvf89sU2bq";
	private static String FD_KEY = "E2gjivyni7Cs54McJ2rvCjGgpitJU6fLsKfvHxSBJLHm";
	private static String FR_KEY = "E2gjivyni7Cs54McJ2rvCjH4JvfqjsHSqzBenDLwSPG8";
    private static String AGE_KEY = "E2gjivyni7Cs54McJ2rvCjHRo8TLy5zMstBKouonKTqx";
    private static String GENDER_KEY = "E2gjivyni7Cs54McJ2rvCjHYxXiWSQDYq1XW5TpG8UQh";
    AFT_FSDKEngine AFT_engine;
    AFT_FSDKError AFT_err;

    public FaceRecognition() {
        AFT_engine = new AFT_FSDKEngine();

        //初始化人脸跟踪引擎，使用时请替换申请的APPID和SDKKEY
        AFT_err = AFT_engine.AFT_FSDK_InitialFaceEngine(APPID, FT_KEY, AFT_FSDKEngine.AFT_OPF_0_HIGHER_EXT, 16, 5);
        Log.d("com.arcsoft", "AFT_FSDK_InitialFaceEngine =" + AFT_err.getCode());

    }

    public List<AFT_FSDKFace> FaceTrackingProcess(byte[] data, int width, int height) {

        // 用来存放检测到的人脸信息列表
        List<AFT_FSDKFace> result = new ArrayList<>();

        //输入的data数据为NV21格式（如Camera里NV21格式的preview数据），其中height不能为奇数，人脸跟踪返回结果保存在result。
        AFT_err = AFT_engine.AFT_FSDK_FaceFeatureDetect(data, width, height, AFT_FSDKEngine.CP_PAF_NV21, result);
        Log.d("com.arcsoft", "AFT_FSDK_FaceFeatureDetect =" + AFT_err.getCode());
        Log.d("com.arcsoft", "Face=" + result.size());
        for (AFT_FSDKFace face : result) {
            Log.d("com.arcsoft", "Face:" + face.toString());
        }

        return result;
    }

    public void FinishFaceTracking() {
        //销毁人脸跟踪引擎
        AFT_err = AFT_engine.AFT_FSDK_UninitialFaceEngine();
        Log.d("com.arcsoft", "AFT_FSDK_UninitialFaceEngine =" + AFT_err.getCode());
    }

    public static List<AFD_FSDKFace> singleFaceDetectionProcess(byte[] data, int width, int height) {
        AFD_FSDKEngine engine = new AFD_FSDKEngine();

        // 用来存放检测到的人脸信息列表
        List<AFD_FSDKFace> result = new ArrayList<>();

        //初始化人脸检测引擎，使用时请替换申请的APPID和SDKKEY
        AFD_FSDKError err = engine.AFD_FSDK_InitialFaceEngine(APPID, FD_KEY, AFD_FSDKEngine.AFD_OPF_0_HIGHER_EXT, 16, 5);
        Log.d("com.arcsoft", "AFD_FSDK_InitialFaceEngine = " + err.getCode());

        //输入的data数据为NV21格式（如Camera里NV21格式的preview数据），其中height不能为奇数，人脸检测返回结果保存在result。
        err = engine.AFD_FSDK_StillImageFaceDetection(data, width, height, AFD_FSDKEngine.CP_PAF_NV21, result);
        Log.d("com.arcsoft", "AFD_FSDK_StillImageFaceDetection =" + err.getCode());
        Log.d("com.arcsoft", "Face=" + result.size());
        for (AFD_FSDKFace face : result) {
            Log.d("com.arcsoft", "Face:" + face.toString());
        }

        //销毁人脸检测引擎
        err = engine.AFD_FSDK_UninitialFaceEngine();
        Log.d("com.arcsoft", "AFD_FSDK_UninitialFaceEngine =" + err.getCode());

        return result;
    }

    public static AFR_FSDKFace singleGetFaceFeature(byte[] data, int width, int height, Rect faceRect, int faceDegree) {
        AFR_FSDKEngine engine = new AFR_FSDKEngine();

        //用来存放提取到的人脸信息
        AFR_FSDKFace faceFeature = new AFR_FSDKFace();

        //初始化人脸识别引擎，使用时请替换申请的APPID 和SDKKEY
        AFR_FSDKError error = engine.AFR_FSDK_InitialEngine(APPID, FR_KEY);
        Log.d("com.arcsoft", "AFR_FSDK_InitialEngine = " + error.getCode());

        //输入的data数据为NV21格式（如Camera里NV21格式的preview数据）；人脸坐标一般使用人脸检测返回的Rect传入；人脸角度请按照人脸检测引擎返回的值传入。
        error = engine.AFR_FSDK_ExtractFRFeature(data, width, height, AFR_FSDKEngine.CP_PAF_NV21, new Rect(faceRect), faceDegree, faceFeature);
        Log.d("com.arcsoft", "Face=" + faceFeature.getFeatureData()[0]+ "," + faceFeature.getFeatureData()[1] + "," + faceFeature.getFeatureData()[2] + "," + error.getCode());

        //销毁人脸识别引擎
        error = engine.AFR_FSDK_UninitialEngine();
        Log.d("com.arcsoft", "AFR_FSDK_UninitialEngine : " + error.getCode());

        return faceFeature;
    }

    public static AFD_FSDKFace AFD_getMaxFace(List<AFD_FSDKFace> result) {

        int area_t;
        int area_max = 0;
        Rect rect_t;
        AFD_FSDKFace face_max = null;

        if (!result.isEmpty()) {
            for (AFD_FSDKFace face : result) {
                rect_t = face.getRect();
                area_t = rect_t.width() * rect_t.height();
                if (area_t > area_max) {
                    area_max = area_t;
                    face_max = face;
                }
            }
        }

        return face_max;
    }

    public static AFT_FSDKFace AFT_getMaxFace(List<AFT_FSDKFace> result) {

        int area_t;
        int area_max = 0;
        Rect rect_t;
        AFT_FSDKFace face_max = null;

        if (!result.isEmpty()) {
            for (AFT_FSDKFace face : result) {
                rect_t = face.getRect();
                area_t = rect_t.width() * rect_t.height();
                if (area_t > area_max) {
                    area_max = area_t;
                    face_max = face;
                }
            }
        }

        return face_max;
    }
}
