package com.chialung.jclprofile.jclModule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by chialung on 2014/5/27.
 */
public class LoadBmp {


    public final static short IMAGE_LOAD_COMPLETE = 10501;
    public final static short IMAGE_LOAD_FAIL = -10501;

    public static LruCache<String, Bitmap> lruCache = null;

    public LoadBmp(){


    }

    public static void loadBmp (final ImageView iview, String url){
        new AsyncTask<String, Void, Bitmap>()
        {
            @Override
            protected Bitmap doInBackground(String... params){
                String url = params[0];
                return getBitmapFromURL(url);
            }


            @Override
            protected void onPostExecute(Bitmap result){
                iview.setImageBitmap(result);
                super.onPostExecute(result);
            }
        }.execute(url);
    }

    //讀取網路圖片，型態為Bitmap
    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 將圖片存儲到LruCache
     */
    public static void addBitmapToLruCache(String key, Bitmap bitmap) {
        if (getBitmapFromLruCache(key) == null) {
            //RareFunction.debug("儲存進入到catche內", 3);
            lruCache.put(key, bitmap);
        }
    }

    /**
     * 從LruCache緩存獲取圖片
     */
    public static Bitmap getBitmapFromLruCache(String key) {
        return lruCache.get(key);
    }


    public static void getBitmap(final String bmpUrl, final ImageView imageView, final Handler handler){

        if (lruCache == null){
            Tools.debug("啟用catch", 3);
            // 獲取應用程序最大可用內存
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            // 設置圖片緩存大小為maxMemory的1/6
            int cacheSize = maxMemory/6;

            lruCache = new LruCache<String, Bitmap>(cacheSize){
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount();
                }
            };
        }

        try{
            Bitmap catchBmp = getBitmapFromLruCache(bmpUrl);
            //RareFunction.debug("bmp:"+bmpUrl, 3);
            if (catchBmp != null){
                //RareFunction.debug("catch有東西", 3);
/*                ImageView myImageView = imageView;
                myImageView.setTag(bmpUrl);

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("image", catchBmp);
                map.put("imageView", myImageView);*/
                if (imageView != null && catchBmp != null){
                    imageView.setImageBitmap(catchBmp);
                }
            }else{
                //RareFunction.debug("catch沒東西", 3);
                loadingBitmap(bmpUrl, imageView, handler);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void loadingBitmap(final String bmpUrl, final ImageView imageView, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    if (bmpUrl != null){
                        URL url = new URL(bmpUrl);
                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                        connection.setConnectTimeout(2000);
                        connection.setRequestMethod("GET");
                        connection.setDoInput(true);

                        int code = connection.getResponseCode();
                        if(code == 200){
                            //成功獲取圖片
                            InputStream input = connection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(input);

                            if (bitmap != null){
                                //RareFunction.debug("bitmap有東西,儲存到catch", 3);
                                addBitmapToLruCache(bmpUrl, bitmap);
                            }

                            //ImageView myImageView = imageView;
                            //myImageView.setTag(bmpUrl);

                            //ArrayList<HashMap<String, Object>> bmpset = new ArrayList<HashMap<String, Object>>();
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("image", bitmap);
                            map.put("imageView", imageView);
                            //bmpset.add(map);

                            //傳到到下面handler做儲存
                            myHandler.obtainMessage(IMAGE_LOAD_COMPLETE, map).sendToTarget();
                            //myHandler.obtainMessage(IMAGE_LOAD_COMPLETE, bitmap).sendToTarget();

                            if (handler != null){
                                Message msg = new Message();
                                msg.what = IMAGE_LOAD_COMPLETE;
                                handler.sendMessage(msg);
                            }
                        }else{
                            //圖片獲取失敗的狀態
                            myHandler.obtainMessage(IMAGE_LOAD_FAIL).sendToTarget();

                            if (handler != null){
                                Message msg = new Message();
                                msg.what = IMAGE_LOAD_FAIL;
                                handler.sendMessage(msg);
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case IMAGE_LOAD_COMPLETE:
                    Map<String, Object> getBmpset = (Map)msg.obj;
                    ImageView completeImageView = (ImageView)getBmpset.get("imageView");
                    Bitmap bmp = (Bitmap)getBmpset.get("image");
                    completeImageView.setImageBitmap(bmp);

                    //RareFunction.debug("圖片載入成功", 3);
                    break;

                case IMAGE_LOAD_FAIL:
                    Tools.debug("圖片載入失敗 Inside the LoadBmp", 3);
                    break;

            }
        }
    };

}

