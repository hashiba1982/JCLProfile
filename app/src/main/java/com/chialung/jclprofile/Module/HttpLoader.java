package com.chialung.jclprofile.Module;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//統一的HTTP LOADER
//為了不想再開一堆CLASS當LOADER 以後都會朝向這隻集中
public class HttpLoader implements Runnable {
    public final static String EXECUTE_RESULT = "result";
    public final static String USETIME = "usedtime";
    private Handler m_handler;
    private Message m_message = new Message();
    public MultipartEntity m_entity =
            new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
    private List<NameValuePair> m_params = new ArrayList<NameValuePair>();
    private boolean isSuccess = false, isOnlyCache = false;
    private byte HttpType = 1;
    /*
         *    1 GET
         *    2 POST
         *    4 PUT
         *    8 DELETE
         *    16 Have File
         *    32 Keep-Alive
         * */
    private boolean isUpdateCookie = false;
    private boolean setJsonObjCheck = false;
    private boolean setJsonArrayCheck = false;
    private HashMap<String, String> moreHeader = null;
    private int CacheReturnCode = 0;
    String URL = null;
    StringBuilder sb = new StringBuilder();
    Bundle bundle = new Bundle();
    String get_para = null;
    long cache_vaild_time = 0;
    private int success_return = 1, fail_return = -1, thread_num = -1;
    Activity activity = null;
    FileEntity fileEntity = null;
    private long StartTime = 0;

    public static CookieStore m_cookieStore;

    public HttpLoader(Activity activity, String URL, Handler handler, int success_return) {
        this.activity = activity;
        init(URL, handler, success_return, -1);
    }

    public HttpLoader(Activity activity, String URL, Handler handler, int success_return, Bundle bundle) {
        this.activity = activity;
        this.bundle = bundle;
        init(URL, handler, success_return, -1);
    }

    public HttpLoader(Activity activity, String URL, Handler handler, int success_return, int fail_return) {
        this.activity = activity;
        init(URL, handler, success_return, fail_return);
    }

    public HttpLoader(Activity activity, String URL, Handler handler, int success_return, int fail_return, Bundle bundle) {
        this.activity = activity;
        this.bundle = bundle;
        init(URL, handler, success_return, fail_return);
    }

    public HttpLoader(String URL, Handler handler, int success_return) {
        init(URL, handler, success_return, -1);
    }

    public HttpLoader(String URL, Handler handler, int success_return, int fail_return) {
        init(URL, handler, success_return, fail_return);
    }

    public void init(String URL, Handler handler, int success_return, int fail_return) {
        try {
            IpartDictionary.init(2);
        } catch (Exception e) {

        }
        this.URL = URL;
        this.m_handler = handler;
        this.success_return = success_return;
        this.fail_return = fail_return;
    }

    public HttpLoader SetCache(int return_code, long vaildTime, boolean onlyCache) {
        CacheReturnCode = return_code;
        cache_vaild_time = (vaildTime > 0) ? vaildTime : 0;
        isOnlyCache = onlyCache;
        return this;
    }

    public HttpLoader SetCache(int return_code, long vaildTime) {
        return SetCache(return_code, vaildTime, false);
    }

    @Override
    public void run() {
        String Para;
        if (m_params.size() > 0) {
            for (NameValuePair pair : m_params) {
                sb.append("&").append(pair.getName()).append("=").append(pair.getValue());
            }
            Para = sb.toString();
            //Para = sb.toString().substring(0, sb.toString().lastIndexOf("&"));
        } else {
            Para = "";
        }
        IpartHttpResult IHR;
        IpartHttp IHPC = new IpartHttp();
        if (m_cookieStore == null) {
            RareFunction.debug("COOKIE NULL", 4);
        }
        //IHPC.setCookie(UserConfig.m_cookieStore);
        if (moreHeader != null) {
            IHPC.setMoreHeader(moreHeader);
        }
        // 統一處理HTTP CACHE
        boolean isHaveCache = false;
        if (CacheReturnCode != 0) {
            RequestData data = IpartDictionary.URLDB.get(URL);
            if (data != null) {
                RareFunction.debug("AiOut", "CT:" + (System.currentTimeMillis() - data.requestTime) + ":" + cache_vaild_time, 2);
                if (System.currentTimeMillis() - data.requestTime < cache_vaild_time) {
                    Message Cache_message = new Message();
                    Bundle Cachebundle = new Bundle();
                    Cache_message.what = CacheReturnCode;
                    Cachebundle.putString(EXECUTE_RESULT, data.returnData);
                    Cachebundle.putLong(USETIME, 0l);
                    Cache_message.setData(Cachebundle);
                    if (m_handler != null) {
                        RareFunction.debug("AiOut", "Cache Return", 2);
                        m_handler.sendMessage(Cache_message);
                        isHaveCache = true;
                    }
                }
            }
        }
        if (!(isOnlyCache && isHaveCache)) {
            RareFunction.debug("AiOut", "Normal Return", 2);
            StartTime = System.currentTimeMillis();
            if ((HttpType & 32) > 0) {
                KeepRequestNode kr = null;
                StringBuffer sb = new StringBuffer();
                kr = (new KeepRequestNode(URL + Para));
                if (m_cookieStore != null) {
                    synchronized (m_cookieStore) {
                        m_cookieStore.clearExpired(new Date());
                        List<Cookie> lt = m_cookieStore.getCookies();
                        Iterator<Cookie> it = lt.iterator();
                        while (it.hasNext()) {
                            Cookie temp = it.next();
                            sb.append(temp.getName() + "=" + temp.getValue() + ";");
                        }
                    }
                    kr.Cookie = sb.toString();
                }
                IHR = HttpKeepAlive.connect(kr);
            } else if ((HttpType & (4 | 16)) == (4 | 16)) {
                IHR = IHPC.doPut(URL, fileEntity);
            } else if ((HttpType & (4)) == (4)) {
                StringEntity stringEntity = null;
                try {
                    stringEntity = new StringEntity(Para);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                IHR = IHPC.doPut(URL, stringEntity);
            } else if ((HttpType & (8)) == (8)) {
                IHR = IHPC.doDelete(URL + Para);
            } else if ((HttpType & (18)) == (18)) {
                if (m_entity == null) {
                    m_entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
                }
                try {
                    if (m_params.size() > 0) {
                        for (NameValuePair pair : m_params) {
                            m_entity.addPart(pair.getName(), new StringBody(pair.getValue(), "text/plain", Charset.forName("UTF-8")));
                        }
                        m_params.clear();
                    }
                } catch (UnsupportedEncodingException e) {
                    Error_log.ipart_ErrProcess(e);
                }
                IHR = IHPC.doPost(URL, m_entity, isUpdateCookie);
            } else if ((HttpType & (2)) == (2)) {
                IHR = IHPC.doPost(URL, m_params, isUpdateCookie);
            } else {
                if (m_params.size() > 0) {
                    IHR = IHPC.doGet(URL + Para, isUpdateCookie);
                } else {
                    IHR = IHPC.doGet(URL, isUpdateCookie);
                }
            }
            if ((HttpType & 2) == 2) {
                m_message.obj = "[POST]" + this.URL + Para;//URI
            } else if ((HttpType & 8) == 8) {
                m_message.obj = "[DELETE]" + this.URL + Para;//URI
            } else if ((HttpType & 4) == 4) {
                m_message.obj = "[PUT]" + this.URL + Para;//URI
            } else {
                m_message.obj = "[GET]" + this.URL + Para;//URI
            }
            if (IHR != null) {
                try {//X-IPAIR HEADER  1 or 0
                    m_message.arg1 = IHR.getserverHeader();
                } catch (Exception ne) {
                    m_message.arg1 = -1;
                }
                m_message.arg2 = IHR.getState_code();//HTTP CODE
                if ((IHR.getState_code() == HttpStatus.SC_OK) || (IHR.getState_code() == HttpStatus.SC_CREATED)) {
                    if (isUpdateCookie) {
                        RareFunction.debug("AiOut", "UpdateCookie", 3);
                        m_cookieStore = (IHR.getCookieStore());
                        m_cookieStore.clearExpired(new Date());
                        //UserConfig.SaveCookie(AppConfig.CACHE_DIR);
                    }
                    bundle.putString(EXECUTE_RESULT, IHR.getResult_str());
                    RequestData data = new RequestData();
                    data.returnData = IHR.getResult_str();
                    data.requestTime = System.currentTimeMillis();
                    if (data.returnData != null && !data.returnData.equalsIgnoreCase("null") && !data.returnData.equalsIgnoreCase("")) {
                        isSuccess = true;
                        if (CacheReturnCode != 0) {
                            IpartDictionary.putURLDB(URL, data);
                        }
                    } else {
                        isSuccess = false;
                    }
                } else {
                    RareFunction.debug("NOT 200:", 3);
                }
            } else if (IHR != null && IHR.getState_code() == HttpStatus.SC_FORBIDDEN) {
                //RareFunction.debug("SC_FORBIDDEN:", 3);
                isSuccess = false;
            } else {
                //RareFunction.debug("IHR NULL", 3);
                isSuccess = false;
            }
            long SpendTime = System.currentTimeMillis() - StartTime;
            bundle.putLong(USETIME, SpendTime);
            /*if (IHR != null) {
                if (IHR.getContentLength() != 0 && SpendTime != 0) {
                    RareFunction.debug("length= " + IHR.getContentLength(), 1);
                    RareFunction.debug("Time= " + SpendTime, 1);
                    RareFunction.debug("SPEED= " + ((float) IHR.getContentLength() / (SpendTime / 1000.0)), 2);
                    AppConfig.NETWORK_SPEED = ((float) IHR.getContentLength() / (SpendTime / 1000.0));
                }
            }*/
            if (isSuccess) {
                m_message.what = success_return;
            } else {
                m_message.what = fail_return;
            }
            m_message.setData(bundle);
            try {
                Thread.sleep(10);
                if (m_handler != null && thread_num > -1) {
                    m_handler.sendMessage(m_message);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public HttpLoader set_paraData(String key, int value) {
        return set_paraData(key, String.valueOf(value));
    }

    public HttpLoader set_paraData(String key, double value) {
        return set_paraData(key, String.valueOf(value));
    }

    public HttpLoader set_paraData(String key, long value) {
        return set_paraData(key, String.valueOf(value));
    }

    public HttpLoader set_paraData(String key, short value) {
        return set_paraData(key, String.valueOf(value));
    }

    public HttpLoader set_paraData(String key, float value) {
        return set_paraData(key, String.valueOf(value));
    }

    public HttpLoader set_paraData(String key, byte value) {
        return set_paraData(key, String.valueOf(value));
    }

    public HttpLoader set_paraData(String key, String value) {
        if (value != null) {
            m_params.add(new BasicNameValuePair(key, value.trim()));
        } else {
            m_params.add(new BasicNameValuePair(key, "null"));
        }
        return this;
    }

    public HttpLoader set_appendData(String key, String value) {
        bundle.putString(key, value);
        return this;
    }

    public HttpLoader set_appendData(String key, Serializable Obj) {
        bundle.putSerializable(key, Obj);
        return this;
    }

    public HttpLoader set_paraData(File file) {
        HttpType |= 16;
        fileEntity = new FileEntity(file, "binary/octet-stream");
        return this;
    }

    public HttpLoader set_paraData(String FileName, File file) {
        HttpType |= 16;
        return set_paraData(FileName, file, true);
    }

    public HttpLoader set_paraData(String name, FileBody fb) {
        HttpType |= 16;
        if (m_entity == null) {
            m_entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,
                    Charset.forName("UTF-8"));
        }
        m_entity.addPart(new FormBodyPart(name, fb));
        return this;
    }

    public HttpLoader set_paraData(String FileName, File file, boolean needMIME) {
        HttpType |= 16;
        if (m_entity == null) {
            m_entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,
                    Charset.forName("UTF-8"));
        }
        if (needMIME) {
            if (FileName != null && file != null) {
                String fname = file.getAbsolutePath();
                int dotPos = fname.lastIndexOf(".");
                String subName = null;
                if (dotPos > -1) {
                    subName = fname.substring(dotPos);
                } else {
                    subName = "";
                }
                switch (subName.hashCode()) {
                    case 1475827:
                        RareFunction.debug("AiOut", ".jpg", 1);
                        //m_entity.addPart(FileName, new FileBody(file, "image/jpeg"));
                        break;
                    case 1481531:
                        RareFunction.debug("AiOut", ".png", 1);
                        //m_entity.addPart(FileName, new FileBody(file, "image/png"));
                        break;
                    default:
                        RareFunction.debug("AiOut", "other", 1);
                        m_entity.addPart(FileName, new FileBody(file));
                }
            }
        } else {
            m_entity.addPart(FileName, new FileBody(file));
        }
        return this;
    }

    // 自己帶給自己的資料
    public HttpLoader set_appendData(String key, int value) {
        bundle.putInt(key, value);
        return this;
    }

    public HttpLoader set_appendData(String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public HttpLoader setGet() {
        HttpType = 1;
        return this;
    }

    public HttpLoader setPut() {
        HttpType |= 4;
        return this;
    }

    public HttpLoader setUpdateCookie() {
        isUpdateCookie = true;
        return this;
    }

    public HttpLoader setPost() {
        HttpType |= 2;
        return this;
    }

    public HttpLoader setDelete() {
        HttpType = 8;
        return this;
    }

    public HttpLoader setFilePost() {
        HttpType = 2 | 16;
        return this;
    }

    public int start() {
        return start((byte) 1);
    }

    public int start(byte type) {
        thread_num = ThreadCenter.addThread(type, this);
        return thread_num;
    }

    public boolean kill() {
        if (thread_num > -1) {
            thread_num = -1;
            return ThreadCenter.stopThread(thread_num);
        } else {
            return false;
        }
    }

    public HttpLoader setKeepAlive() {
        HttpType |= 32;
        return this;
    }

    public HttpLoader setSetJsonCheck() {
        this.setJsonObjCheck = true;
        return this;
    }

    public HttpLoader setSetJsonArrayCheck() {
        this.setJsonArrayCheck = true;
        return this;
    }

    public HttpLoader setMoreHeader(HashMap<String, String> moreHeader) {
        this.moreHeader = moreHeader;
        return this;
    }

    public static void DropHttpCache() {
        IpartDictionary.DropCache();
    }
}
