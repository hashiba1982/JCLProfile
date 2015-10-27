package com.chialung.jclprofile.Module;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StatFs;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ainaru on 14-2-6.
 */
public class RareFunction {


    public static String DateTimeFormat(Date date) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        return new SimpleDateFormat("MM-dd HH:mm").format(date);
    }

    // 檢查還有沒有空間
    public static boolean isCacheAvaiableSpace(long sizeKB) {
        try {
            String sdcard = "";
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long ablocks = statFs.getAvailableBlocks();
            float available = (float) ((blockSize / 1024) * ablocks);// KB
            if (sizeKB > available) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static void debug(String message, int lv) {
        debug("AiOut", message, lv);
    }

    public static void debug(String message) {
        debug("AiOut", message, 0);
    }

    public static void debug(String tag, String message) {
        debug(tag, message, 0);
    }

    public static void debug(String tag, int message, int i) {
        debug(tag, String.valueOf(message), i);
    }

    public static void debug(String tag, String message, int i) {
        if (tag == null || message == null) {
            return;
        }
        if (i > 2) {
            switch (i) {
                case 0:
                    Log.v(tag, message);
                    break;
                case 1:
                    Log.d(tag, message);
                    break;
                case 2:
                    Log.i(tag, message);
                    break;
                case 3:
                    Log.w(tag, message);
                    break;
                case 4:
                    Log.e(tag, message);
                    break;
            }
        }
    }

    public static boolean StartOtherApp(Activity context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // we found the activity
            // now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public static boolean CheckPackageExist(Context context, String PackageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(PackageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getMacAddress(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        return wifiInf.getMacAddress();
    }

    //檢查統編
    public static boolean checkBussinessID(String id) {
        if (id.length() != 8) {
            return false;
        }
        int v[] = {1, 2, 1, 2, 1, 2, 4, 1};
        int A1[] = new int[8];
        int A2[] = new int[8];
        int B = 0;
        int B1 = 0;
        for (int i = 0; i < v.length; i++) {
            A1[i] = Integer.parseInt(String.valueOf(id.charAt(i))) * v[i];
        }
        for (int i = 0; i < A1.length; i++) {
            if (A1[i] < 10) {
                A2[i] = A1[i];
            } else {
                A2[i] = Integer.parseInt(String.valueOf(A1[i]).substring(0, 1)) + Integer.parseInt(String.valueOf(A1[i]).substring(1, 2));
            }
        }
        for (int i = 0; i < A2.length; i++) {
            B = B + A2[i];
        }
        if (B % 10 == 0) {
            return true;
        } else {
            if (id.charAt(6) == 7) {
                B = A2[0] + A2[1] + A2[2] + A2[3] + A2[4] + A2[5] + 0 + A2[7];
                B1 = A2[0] + A2[1] + A2[2] + A2[3] + A2[4] + A2[5] + 1 + A2[7];
                if ((B % 10 == 0) || (B1 % 10 == 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    //你的app是不是可見的
    public static boolean isAppInTopView(Context m_context) {
        ActivityManager activityManager = (ActivityManager) m_context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(1);
        debug(services.get(0).topActivity.getPackageName().toString() + "=" + m_context.getApplicationContext().getPackageName().toString(), 3);
        if (services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(m_context.getApplicationContext().getPackageName().toString())) {
            return true;
        }
        return false;
    }

    //自殺
    public static boolean killSelf(Context m_context) {
        try {
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //app還活者嗎?
    public static boolean isAppRunning(Context m_context) {
        ActivityManager activityManager = (ActivityManager) m_context.getSystemService(m_context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < procInfos.size(); i++) {
            if (procInfos.get(i).processName.equals("com.ipart.android")) {
                return true;
            }
        }
        return false;
    }

    public static String encrypt(String s) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-256");
            sha.update(s.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return byte2hex(sha.digest());
    }

    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    // 計算字串應該佔的字元數
    public static int getLength(String str) {
        float res = 0;
        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            if (temp.getBytes().length == temp.length()) {
                res += 1;// 英數
            } else {
                res += 2;
            }
        }
        return (int) res;
    }

    public static Bitmap convertBitmapToCorrectOrientation(Bitmap photo, int rotation) {
        int width = photo.getWidth();
        int height = photo.getHeight();
        Matrix matrix = new Matrix();
        matrix.preRotate(rotation);
        return Bitmap.createBitmap(photo, 0, 0, width, height, matrix, false);
    }

    //取得圖片標頭的旋轉角度
    public static int getBitmapDegrees(String filePath) {
        int exifOrientation = 0;
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {

        }
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static Bitmap reSize(Bitmap bit, int MaxPx) {
        return reSize(bit, MaxPx, 100);
    }

    //重切圖片至某個大小
    public static Bitmap reSize(Bitmap bit, int MaxPx, int quality) {
        if (bit == null) {
            return bit;
        }
        int width = bit.getWidth(), height = bit.getHeight();
        if (width < MaxPx && height < MaxPx) {
            return bit;// 傳進來的圖比要的小，不處理
        }
        float ratio = 1;// 縮放比例
        if (width > height && width > MaxPx) {
            // 這張圖比較寬，依寬度進行等比例縮放
            ratio = (float) MaxPx / (float) width;
        } else if (height > MaxPx) {
            // 這張圖比較高，依高度進行等比例縮放
            ratio = (float) MaxPx / (float) height;
        }
        //Utils.debug("AiOut","ratio:"+ratio,2);
        if (ratio >= 1) {
            // 如果比例不需要縮小，返回原圖，如果你有放大需求，可以修改這邊
            return bit;
        } else {
            int newW = (int) (width * ratio), newH = (int) (height * ratio);
            if (newW <= 0 || newH <= 0) {
                // 你的縮放比例可能太誇張了，設一個預設的比例
                if (width > height && width > MaxPx) {
                    ratio = (float) 1024.0 / (float) width;
                } else if (height > MaxPx) {
                    ratio = (float) 1024.0 / (float) height;
                }
                newW = (int) (width * ratio);
                newH = (int) (height * ratio);
            }
            //Utils.debug("AiOut","new:"+newW+","+newH,3);
            Bitmap marker = Bitmap.createBitmap(newW, newH, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(marker);
            try {
                canvas.drawBitmap(bit, new Rect(0, 0, width, height), new Rect(0, 0, newW, newH), null);
            } catch (Exception e) {
            }
            // return new BitmapDrawable(marker); //如果你需要的是Drawable 用這行
            bit.recycle();
            return (marker);
        }
    }


    //切成正方形
    public static Bitmap getRectBitmap(Bitmap bit) {
        return getRectBitmap(bit, -1);
    }

    public static Bitmap getRectBitmap(Bitmap bit, int sidePX) {
        if (bit == null) {
            return bit;
        }
        int width = bit.getWidth(), height = bit.getHeight();
        int rect_side = (width > height) ? height : width;
        if (sidePX <= 0) {
            sidePX = rect_side;
        }
        RareFunction.debug("SidePx:" + sidePX, 1);
        Bitmap marker = Bitmap.createBitmap(sidePX, sidePX, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(marker);
        try {
            int wdiff = 0, hdiff = 0;
            if (width > rect_side) {
                wdiff = (width - rect_side) / 4;
            }
            if (height > rect_side) {
                hdiff = (height - rect_side) / 4;
            }
            canvas.drawBitmap(bit, new Rect(wdiff, hdiff, rect_side + wdiff, rect_side + hdiff), new Rect(0, 0, sidePX, sidePX), null);
        } catch (Exception e) {

        }
        // return new BitmapDrawable(marker);
        return (marker);
    }



    public static byte[] getFile(File f) throws IOException {
        debug("AiOut", "FILE: " + f.getAbsolutePath());
        if (f.exists()) {
            InputStream is = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            is = new FileInputStream(f);
            byte[] b = new byte[4096];
            int len = -1;
            while ((len = is.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            return baos.toByteArray();
        } else {
            debug("AiOut", "FILE NOT EXIST");
        }
        return null;
    }

    // 因為不知道UTILS裡面的為何要傳context 怕踩到雷，放一份整理的在這
    public static Bitmap decodeToBmp(byte[] imgArr) {
        return decodeToBmp(imgArr, 0, 0);
    }

    public static Bitmap decodeToBmp(byte[] imgArr, int needWidth, int needHeight) {
        try {
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            if (needWidth > 0 && needHeight > 0) {
                o2.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(imgArr, 0, imgArr.length, o2);
                o2.inSampleSize = calculateInSampleSize(o2, needWidth, needHeight);
            } else {
                o2.inSampleSize = 1;
            }
            o2.inJustDecodeBounds = false;
            o2.inDither = false;
            o2.inPurgeable = true;
            o2.inPreferredConfig = Bitmap.Config.RGB_565;
            o2.inInputShareable = true;
            return BitmapFactory.decodeByteArray(imgArr, 0, imgArr.length, o2);
        } catch (Exception e) {

        }
        return null;
    }

    public static Bitmap decodeImage(final ContentResolver resolver, final Uri uri,
                                     final int MAX_DIM)
            throws FileNotFoundException {

        // Get original dimensions
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(resolver.openInputStream(uri), null, o);
        } catch (SecurityException se) {
            se.printStackTrace();
            return null;
        }

        final int origWidth = o.outWidth;
        final int origHeight = o.outHeight;

        // Holds returned bitmap
        Bitmap bitmap;

        o.inJustDecodeBounds = false;
        o.inScaled = false;
        o.inPurgeable = true;
        o.inInputShareable = true;
        o.inDither = true;
        o.inPreferredConfig = Bitmap.Config.RGB_565;

        if (origWidth > MAX_DIM || origHeight > MAX_DIM) {
            int k = 1;
            int tmpHeight = origHeight, tmpWidth = origWidth;
            while ((tmpWidth / 2) >= MAX_DIM || (tmpHeight / 2) >= MAX_DIM) {
                tmpWidth /= 2;
                tmpHeight /= 2;
                k *= 2;
            }
            o.inSampleSize = k;

            bitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri), null, o);
        } else {
            bitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri), null, o);
        }



        return bitmap;
    }

    // 這個算法比Utils中的快
    public static String getMD5(String data_str) {
        byte[] source = data_str.getBytes();
        String s = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return s;
    }

    public static WifiInfo getWifiInfo(Context context) {
        // wifi
        // IntentFilter wifiIntentFilter = new IntentFilter();
        // wifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi_service.getConnectionInfo();
    }

    public static String getUserAgentString(Context context) {
        WebView webview = new WebView(context);
        return webview.getSettings().getUserAgentString();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String build_GetRequest_varString(String[][] data) {
        if (data != null) {
            StringBuffer sb = new StringBuffer(512);
            for (int i = 0; i < data.length; i++) {
                if (data[i] != null) {
                    if (data[i].length > 1) {
                        sb.append("&").append(data[i][0]).append("=").append(data[i][1]);
                    }
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * 根据手机的分辨率? dp 的?位 ?成? px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context == null) {
            return (int) (dpValue * 2);
        } else {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources()
                    .getDisplayMetrics());
        }
    }

    /**
     * 根据手机的分辨率? px(像素) 的?位 ?成? dp
     */
    public static int px2dip(Context context, float pxValue) {
        if (context == null) {
            return (int) (pxValue / 2);
        } else {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }
    }

    // 從URL切出檔名
    public static String getFileNameFromUrl(String url) {
        if (url != null) {
            int pos = url.lastIndexOf("/");
            if (pos > -1) {
                return url.substring(pos + 1, url.length());
            }
        }
        return url;
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    // 檢查字串是不是具有英文以外的字
    public static boolean isAllEnglish(String str) {
        return (str.getBytes().length == str.length()) ? true : false;
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        return bitmap;
    }

    public static Object InputObj(File path, String filename) {
        try {
            File objInfo = new File(path, getMD5(filename));
            Object obj = null;
            if (objInfo.exists()) {
                FileInputStream fin;
                fin = new FileInputStream(objInfo.getAbsolutePath());
                ObjectInputStream ooin = new ObjectInputStream(fin);
                obj = ooin.readObject();
                ooin.close();
                fin.close();
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeDB(Object obj, File dir, String filename) {
        try {
            if (!dir.isDirectory() || dir.exists()) {
                try {
                    dir.mkdir();
                } catch (Exception e) {
                }
            }
            File bufFile = new File(dir, getMD5(filename));
            if (bufFile.canWrite()) {
                try {
                    bufFile.delete();
                } catch (Exception e) {
                }
            }
            bufFile.createNewFile();
            ObjectOutputStream bufFile1 = new ObjectOutputStream(new FileOutputStream(bufFile));
            bufFile1.writeObject(obj);
            bufFile1.flush();
            bufFile1.close();
            bufFile1 = null;
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT > 10) {
                // 2.X的系統，不回報 NoSpaceError
            }
        }
    }

    public static String load_Assets(Context context, String Filename) {
        try {
            StringBuilder sb = new StringBuilder();
            InputStream is = context.getAssets().open(Filename);
            BufferedReader inn = new BufferedReader(new InputStreamReader(is));
            String str;
            while ((str = inn.readLine()) != null) {
                sb.append(str);
            }
            is.close();
            return sb.toString();
        } catch (Exception iOException) {
            iOException.printStackTrace();
            return null;
        }
    }

    public static String writeFile(byte[] byteArray, File dir, String filename) {
        try {
            if (!dir.isDirectory() || dir.exists()) {
                try {
                    dir.mkdir();
                } catch (Exception e) {
                    return null;// 路徑建立失敗
                }
            }
            /*int dotPos = filename.lastIndexOf(".");
            String subName = "";
            if (dotPos > -1) {
                subName = filename.substring(dotPos);
            }*/
            String saveName = getMD5(filename);
            File bufFile = new File(dir, saveName);
            if (bufFile.canWrite()) {
                try {
                    bufFile.delete();// 如果檔案已存在且可以寫入
                } catch (Exception e) {
                }
            }
            bufFile.createNewFile();
            OutputStream bufFile1 = new FileOutputStream(bufFile);
            bufFile1.write(byteArray);
            bufFile1.flush();
            bufFile1.close();
            bufFile1 = null;
            return bufFile.getAbsolutePath();
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT > 10) {
                // 2.X的系統，不回報 NoSpaceError
            }
            return null;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        while (height / inSampleSize > reqHeight || width / inSampleSize > reqWidth) {
            if (height > width) {
                inSampleSize = height / reqHeight;
                if (((double) height % (double) reqHeight) != 0) {
                    inSampleSize++;
                }
            } else {
                inSampleSize = width / reqWidth;
                if (((double) width % (double) reqWidth) != 0) {
                    inSampleSize++;
                }
            }
        }
        return inSampleSize;
    }

    public static void ToastMsg(Context m_context, String msg) {
        Toast.makeText(m_context, msg, Toast.LENGTH_SHORT).show();
    }

    public static String getImei(Context context) {
        String imei = null;
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        imei = mTelephonyMgr.getDeviceId();
        boolean isOk = true;
        try {
            if ("".equals(imei) || imei == null) {
                isOk = false;
            } else if (Long.parseLong(imei) == 0) {
                isOk = false;
            }
        } catch (Exception e) {
        }
        if (isOk) {
            return imei;
        } else {
            return "";
        }
    }

    public static void showMessageDialog(Context context, String title, String message) {
        try {
            if (!((Activity) context).isFinishing()) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                if (title != null) {
                    dialog.setTitle(title);
                }
                if (message != null) {
                    dialog.setMessage(message);
                }
                dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        } catch (Exception e) {

        }
    }

    public static String getRealPathFromURI(Activity context, Uri contentUri) {
        if (context != null && contentUri != null) {
            String[] proj = {MediaStore.MediaColumns.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int s = contentUri.getPath().indexOf("http");
            if (s > -1) {
                return contentUri.getPath().substring(s, contentUri.getPath().length());
            } else if (cursor != null) {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(column_index);
            } else {
                //Utils.debug("AiOut","cursor null",3);
                return contentUri.toString().replace("file://", "");
            }
        } else {
            return "";
        }
    }

/*    public static byte[] getByteArrayFromUrl(String url) {
        String result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            URI uri = new URI(url);
            HttpGet method = new HttpGet(uri);
            HttpResponse res = client.execute(method);
            InputStream stream = res.getEntity().getContent();
            byte[] data = new byte[4096];
            int len = -1;
            while ((len = stream.read(data)) != -1) {
                baos.write(data, 0, len);
            }
        } catch (Exception e) {

        }
        return baos.toByteArray();
    }*/

    public static String KeyHashes(Activity self){
        try {//取得fb key的code
            PackageInfo pinfo = self.getPackageManager()
                    .getPackageInfo("com.ipart.android", PackageManager.GET_SIGNATURES);
            for (Signature signature : pinfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("AiOut:", "KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (Exception e) {e.printStackTrace();}
        return "";
    }

}
