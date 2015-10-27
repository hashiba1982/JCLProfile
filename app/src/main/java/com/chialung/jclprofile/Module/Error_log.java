package com.chialung.jclprofile.Module;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.net.ParseException;
import android.os.Build;
import android.os.Message;
import android.os.RemoteException;
import android.webkit.WebView;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//統一回收EXCEPTION的地方
public class Error_log {
    public static byte no_login_count = 0;
    // ERROR REPORT CODE
    // ///////////////////////////////////////////////////////////////////////////////////////////
    public final static short NEED_NOTICE_CODE = 9790;
    public final static short OLDOSVER_EXCEPTION_CODE = 100;
    public final static short OS_EXCEPTION_CODE = 8877;
    public final static short OTHER_PACKAGE_EXCEPTION_CODE = 7788;
    public final static short SOUND_ERROR_CODE = 4466;
    public final static short WEBVIEW_JS_EXCEPTION_CODE = 700;
    public final static short CONNECT_TIMEOUT_EXCEPTION_CODE = 900;
    public final static short PARSE_EXCEPTION_CODE = 901;
    public final static short IO_EXCEPTION_CODE = 902;
    public final static short IllegalStateException_CODE = 903;
    public final static short WEBVIEW_EXCEPTION_CODE = 997;
    public final static short WEBVIEW_BIGERROR_CODE = 995;
    public final static short IAP_MESSAGE_CODE = 3838;
    public final static short IMPORTMENT_ERROR_CODE = 994;
    public final static short INTERRUPTEDEXCEPTION_CODE = 998;
    public final static short OTHER_EXCEPTION_CODE = 999;
    public final static short GOOGLE_CASH_CODE = 777;
    public final static short GZIP_EXCEPTION_CODE = 7789;
    public final static short MAMCACHE_EXCEPTION_CODE = 7790;
    public final static short TEMP_EXCEPTION_CODE = 104;// 短時間的紀錄留言版UNO為空的LOG
    public final static short RemoteException_CODE = 6790;

    //Default Area////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static List<NameValuePair> get_sendBox(Message msg, String Appendtext) {
        List<NameValuePair> m_params = new ArrayList<NameValuePair>();
        m_params.add(new BasicNameValuePair("PhoneName", Build.MODEL));
        if(msg!=null){
            m_params.add(new BasicNameValuePair("XIPART", String.valueOf(msg.arg1)));
            m_params.add(new BasicNameValuePair("append_data2", "HttpCode(" + ((msg.arg1 == 1) ? "iPair" : "Other") + ")=" + msg.arg2 + "[" + msg.obj + "]"));
        }else{
            m_params.add(new BasicNameValuePair("XIPART", "-1"));
            m_params.add(new BasicNameValuePair("append_data2",""));
        }
        m_params.add(new BasicNameValuePair("append_data", (Appendtext == null) ? "" : Appendtext));
        m_params.add(new BasicNameValuePair("user_telCode", String.valueOf(Build.VERSION.SDK_INT)));
        /*try {
            if (AppConfig.rootFrag != null) {
                m_params.add(new BasicNameValuePair("Network", (AppConfig.NETWORK_WORK ? ("Yes WIFI:" + ((AppConfig.WIFI_RSSI < 0) ? AppConfig.WIFI_RSSI + 100 : AppConfig.WIFI_RSSI)) : ("No")) + "[GCM=" + CommonFunction.getGCM_TOKEN(AppConfig.rootFrag) + "]"));
            } else {
                m_params.add(new BasicNameValuePair("Network", (AppConfig.NETWORK_WORK ? ("Yes WIFI:" + ((AppConfig.WIFI_RSSI < 0) ? AppConfig.WIFI_RSSI + 100 : AppConfig.WIFI_RSSI)) : ("No")) + "[Can`t get GCM Code]"));
            }
        } catch (Exception e) {
        }*/
        return m_params;
    }

    public static void send(List<NameValuePair> m_params) {
        //new SendReporter(AppConfig.HttpProtocol + AppConfig.REPORT_HOST + AppConfig.ERROR_REPORT_PATH, m_params).start();
    }

    public static void send_report(int errCode, Exception e, Message msg, String Appendtext) {
        try {
            // skip 不需要回報的類型
            if (e != null) {
                RareFunction.debug(e.getMessage());
                e.printStackTrace();
            }
            switch (errCode) {
                case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                    break;
                case HttpStatus.SC_NOT_FOUND:
                    break;
                case HttpStatus.SC_BAD_GATEWAY:
                    break;
                case PARSE_EXCEPTION_CODE:
                    break;
                case CONNECT_TIMEOUT_EXCEPTION_CODE:
                    RareFunction.debug("Error", "CONNECT_TIMEOUT:", 3);
                    return;
                default:
                    if (e!=null && e.getMessage() != null) {
                        if (Appendtext.indexOf("/api/apps/error_report/errSave.php") > -1) {
                            return;// 回報錯誤，直接丟掉
                        } else if (e.getMessage().equalsIgnoreCase("Invalid argument")) {
                            errCode = OTHER_PACKAGE_EXCEPTION_CODE;// 屬於底層HTTP元件錯誤
                        } else if (e.getMessage().indexOf("[PlainSocketImpl.java]->read") > -1) {
                            return;// TIME OUT ERROR
                        } else if (e.getMessage().equalsIgnoreCase("Connection to http://www.i-part.com.tw refused")) {
                            errCode = OS_EXCEPTION_CODE;// 原因不明，非APP問題
                        } else if (e.getMessage().equalsIgnoreCase("Connection to http://c.m.ipimg.com refused")) {
                            errCode = OS_EXCEPTION_CODE;// 原因不明，非APP問題
                        } else if (e.getMessage().equalsIgnoreCase("Unable to create application data")) {
                            errCode = OTHER_PACKAGE_EXCEPTION_CODE;// SSL SHAKE HAND ERROR
                        } else if (e.getMessage().equalsIgnoreCase("No buffer space available")) {
                            return;// memory error 沒解
                        } else if (e.getMessage().equalsIgnoreCase("Too many open files")) {
                            errCode = OS_EXCEPTION_CODE;// 超過作業系統規定上限
                        } else if (e.getMessage().indexOf("open failed") > -1) {
                            errCode = OS_EXCEPTION_CODE;// IO ERROR
                        } else if (e.getMessage().equalsIgnoreCase("The socket level is invalid")) {
                            return;// 沒有解決辦法，不管他
                        }
                    }
            }
            // 重整代號
            if (e != null) {
                // GZIP 解壓失敗
                try{
                if(e.getMessage()!=null && e.getMessage().indexOf("unknown format (magic number 213c)") > -1){
                errCode = GZIP_EXCEPTION_CODE;}
                }catch (Exception e2){};//skip
            } else if ("ERROR".equals(Appendtext)) {
                // GZIP 解壓失敗
                errCode = MAMCACHE_EXCEPTION_CODE;
            }
            // skip end
            List<NameValuePair> m_params = get_sendBox(msg, Appendtext);
            m_params.add(new BasicNameValuePair("act", "err"));
            m_params.add(new BasicNameValuePair("error_code", String.valueOf(errCode)));
            if (e != null) {
                StringBuffer temp = new StringBuffer();
                StackTraceElement[] Alllog = e.getStackTrace();
                for (StackTraceElement oneLog : Alllog) {
                    temp.append("[" + oneLog.getFileName() + "]->" + oneLog.getMethodName() + "   Line:" + oneLog
                            .getLineNumber() + "\n");
                }
                m_params.add(new BasicNameValuePair("err_msg", "[" + e.getMessage() + "]: " + temp.toString()));
            } else {
                m_params.add(new BasicNameValuePair("err_msg", "[none]: "));
            }
            send(m_params);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void send_WebViewreport(int errCode, String description, String text) {
        List<NameValuePair> m_params = get_sendBox(null, text);
        m_params.add(new BasicNameValuePair("act", "err"));
        m_params.add(new BasicNameValuePair("error_code", String.valueOf(errCode)));
        m_params.add(new BasicNameValuePair("err_msg", description));
        send(m_params);
    }

    public static void send_NoticeReport(int errCode, String description, String text) {
        List<NameValuePair> m_params = get_sendBox(null, text);
        m_params.add(new BasicNameValuePair("act", "err"));
        m_params.add(new BasicNameValuePair("error_code", String.valueOf(errCode)));
        m_params.add(new BasicNameValuePair("err_msg", description));
        send(m_params);
    }
    public static void ipart_ErrMsg(Context context,JSONObject json) throws JSONException {
        Error_log.ipart_ErrMsg(context, json.getString("sysType"), json.getString("sysDesc"), 0);
    }
    public static void ipart_ErrMsg(Context context, String sysType, String sysDesc) {
        Error_log.ipart_ErrMsg(context, sysType, sysDesc, 0);
    }

    public static void ipart_ErrMsg(Context context, String sysType, String sysDesc, int typeCode) {
        switch (sysType.hashCode()) {
            case 87:// W
                // Utils.showMessageDialog(context,
                // context.getResources().getString(R.string.ipartapp_string00000152),
                // sysDesc);
  /*              if (AppConfig.DEBUG_MODE) {
                    Toast.makeText(context, "Server警告:" + sysDesc, Toast.LENGTH_LONG).show();
                }*/
                break;
            case 69:// E
/*                if (AppConfig.DEBUG_MODE) {
                    Toast.makeText(context, "Server錯誤:" + sysDesc, Toast.LENGTH_LONG).show();
                }*/
                break;
        }
        try {
            if (sysDesc != null) {
                if (sysDesc.toLowerCase().indexOf("no login") > -1) {
                    if (Error_log.no_login_count++ > 2) {
                        typeCode |= 1;
                    }
                }
            }
        } catch (Exception e) {
            ((Activity) context).finish();
        }
        /*
		 * Type code:
		 * 1:restart
		 */
        if ((typeCode & 1) > 0) {
            RareFunction.debug("AiOut", "call logout", 2);
            /*try {
                if (AppConfig.rootFrag != null) {
                    AppConfig.rootFrag.callRelogin();
                } else {
                    AppConfig.isGoInit = false;
                    if (AppConfig.rootFrag != null) {
                        //((IPartApp) AppConfig.rootFrag.getApplication()).getCookie_center().clearLoginCache();
                        //AppConfig.rootFrag.onLogoutClick();
                        IPartApp app = (IPartApp) AppConfig.rootFrag.getApplication();
                        app.isMan2call = false;
                        AppConfig.m_userInfo = null;
                        AppConfig.setCookieStore(null);
                        Error_log.no_login_count = 0;
                    }
                    ((Activity) context).finish();
                    AppConfig.rootFrag.finish();
                }
            } catch (Exception e) {
                CommonFunction.callRelogin(AppConfig.rootFrag);
            }*/
        }
    }
    //Default Area////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void ipart_ErrProcess(ParseException e, Message msg, String AppendData) {
        Error_log.send_report(PARSE_EXCEPTION_CODE, e, msg, AppendData);
    }

    public static void ipart_ErrProcess(JSONException e, Message msg, String AppendData) {
        Error_log.send_report(PARSE_EXCEPTION_CODE, e, msg, AppendData);
    }

    public static void ipart_ErrProcess(Exception e, Message msg, String AppendData) {
        Error_log.send_report(OTHER_EXCEPTION_CODE, e, msg, AppendData);
    }
    public static void ipart_ErrProcess(Exception e, Message msg) {
        if(msg!=null){
            //Error_log.send_report(OTHER_EXCEPTION_CODE, e, msg, msg.getData().getString(HttpLoader.EXECUTE_RESULT));
        }
    }
    public static void ipart_ErrProcess(int errCode,Exception e, Message msg) {
        if(msg!=null){
            //Error_log.send_report(errCode, e, msg, msg.getData().getString(HttpLoader.EXECUTE_RESULT));
        }
    }
    public static void ipart_ErrProcess(Exception e, String AppendData) {
        Error_log.send_report(OTHER_EXCEPTION_CODE, e, null, AppendData);
    }

    public static void ipart_ErrProcess(int errCode, Exception e, Message msg, String AppendData) {
        Error_log.send_report(errCode, e, msg, AppendData);
    }

    public static void ipart_ErrProcess(Exception e) {
        Error_log.send_report(OTHER_EXCEPTION_CODE, e, null, "");
    }
    public static void ipart_ErrProcess(SendIntentException e) {
        Error_log.send_report(GOOGLE_CASH_CODE, e, null, "");
    }

    public static void ipart_ErrProcess(RemoteException e) {
        Error_log.send_report(GOOGLE_CASH_CODE, e, null, "");
    }

    public static void ipart_ErrProcess(InterruptedException e) {
        Error_log.send_report(INTERRUPTEDEXCEPTION_CODE, e, null, "");
    }

    public static void ipart_ErrProcess(ConnectTimeoutException e) {
        Error_log.send_report(CONNECT_TIMEOUT_EXCEPTION_CODE, e, null, "");
    }

    public static void ipart_ErrProcess(IOException e) {
        Error_log.send_report(CONNECT_TIMEOUT_EXCEPTION_CODE, e, null, "");
    }
    public static void HTML_Check(String URL, String html) {
        // 例外
        if (html.indexOf("self.location.href") > -1 || html.indexOf("<body onload=\"fnReturn();\">") > -1) {
            // 過水頁
        } else {
            //Utils.debug("AiOut", "Call URL " + URL, 4);
            //Utils.debug("AiOut", "Call HTML " + html.length(), 3);
            //Utils.debug("AiOut", "Call HTML " + html, 4);
            boolean isSend = false;
            if (html.length() < 170) {
                send_WebViewreport(WEBVIEW_BIGERROR_CODE, "[SOS Report]:", "[URL=" + URL + "],[HTML=" + html + "]");
                isSend = true;
            }
            if (!isSend) {
                String[] data = {"no input file specified", "502 Bad Request", "Access denied.", "403 Forbidden", "500 Internal Server Error", "404 Not Found", "400 Bad Request"};
                for (String find : data) {
                    if (html.indexOf(find) > -1) {
                        send_WebViewreport(WEBVIEW_BIGERROR_CODE, "[SOS Report]:", "[URL=" + URL + "],[HTML=" + html + "]");
                        break;
                    }
                }
            }
        }
    }
    public static void ipart_webViewErr(WebView view, int errorCode, String description, String failingUrl) {
        // -2 =Http 404
        if (errorCode == 404 || errorCode == 500 || errorCode == 403 || errorCode == 400) {
            Error_log.send_WebViewreport(WEBVIEW_BIGERROR_CODE, "[WebViewErr]:" + description, "[OriginUrl=" + view
                    .getOriginalUrl() + "],[failingUrl=" + failingUrl + "]");
        } else {
            Error_log.send_WebViewreport(WEBVIEW_EXCEPTION_CODE, "[WebViewErr]:" + description, "[OriginUrl=" + view
                    .getOriginalUrl() + "],[failingUrl=" + failingUrl + "]");
        }

/*        if (AppConfig.DEBUG_MODE) {
            //Toast.makeText(AppConfig.rootFrag, "[WebViewErr]:" + description, Toast.LENGTH_SHORT).show();
        }*/
    }
}
