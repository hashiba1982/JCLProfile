package com.chialung.jclprofile.jclModule;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by chialung on 2015/1/9.
 */
public class Tween {

    private Animation am;


    //動畫測試
 /*           Tween tween = new Tween();
            tween.setAlpha(this, rl_loading, 500, 1, 0, 1);*/


    //TranslateAnimation(float x1, float x2, float y1, float y2)
    //(x1,y1)移動到(x2,y2)
    public void setMove(Context context, View targetObj, int time, int count, float startX, float startY, float endX, float endY){
        startAnima(context, targetObj, time, count, 2, startX, endX, startY, endY);
    }

    //ScaleAnimation(float fromX, float toX, float fromY, float toY)
    //(fromX,toX)X軸從fromX的倍率放大/縮小至toX的倍率
    //(fromY,toY)X軸從fromY的倍率放大/縮小至toX的倍率
    public void setScale(Context context, View targetObj, int time, int count, float startScaleX, float startScaleY, float endScaleX, float endScaleY){
        startAnima(context, targetObj, time, count, 2, startScaleX, endScaleX, startScaleY, endScaleY);
    }

    //RotateAnimation(float fromDegrees, float toDegrees, float pivotX, float pivotY)
    //fromDegrees起始的旋轉角度、toDegrees最後的旋轉角度
    //pivotX、pivotX設定旋轉中心點
    public void setRotate(Context context, View targetObj, int time, int count, float startDegX, float startDegY, float endDegX, float endDegY){
        startAnima(context, targetObj, time, count, 3, startDegX, endDegX, startDegY, endDegY);
    }

    //AlphaAnimation(float fromAlpha, float toAlpha)
    //fromAlpha 起始透明度(Alpha)值
    //toAlpha 最後透明度(Alpha)值
    public void setAlpha(Context context, View targetObj, int time, int count, float startAlpha, float endAlpha){
        startAnima(context, targetObj, time, count, 4, startAlpha, endAlpha, 0, 0);
    }


    public void startAnima(Context context, View targetObj, int time, int count, int mode, float param1, float param2, float param3, float param4) {


        switch (mode){
            case 1:
                am = new TranslateAnimation(param1, param2, param3, param4);
                break;

            case 2:
                am = new ScaleAnimation(param1, param2, param3, param4);
                break;

            case 3:
                am = new RotateAnimation(param1, param2, param3, param4);
                break;

            case 4:
                am = new AlphaAnimation(param1, param2);
                break;
        }


        am.setDuration(time);
        am.setRepeatCount(count);
        targetObj.startAnimation(am);
    }

}
