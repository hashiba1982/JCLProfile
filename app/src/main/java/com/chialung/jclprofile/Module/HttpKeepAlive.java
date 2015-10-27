package com.chialung.jclprofile.Module;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * Created by Ainaru on 13-12-30.
 */
public class HttpKeepAlive {
    public static IpartHttpResult connect(KeepRequestNode RN) {
        return GetConnect(RN, 0);
    }

    public static IpartHttpResult connect(KeepRequestNode RN, int waiting) {
        return GetConnect(RN, waiting);
    }

    public static IpartHttpResult GetConnect(KeepRequestNode RN, int waiting) {
        System.setProperty("http.keepAlive", "true");
        System.setProperty("http.maxConnections", "15");
        IpartHttpResult IHR = null;
        try {
            int ServerName = -1;
            byte buf[] = new byte[2048];
            URL a = new URL(RN.RequestUri);
            HttpURLConnection conn = (HttpURLConnection) a.openConnection();
            if (waiting > 0) {
                conn.setReadTimeout(waiting);
                conn.setConnectTimeout(waiting);
            } else {
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
            }
            if (RN.extraHeader != null) {
                Set<Map.Entry<String, String>> set = RN.extraHeader.entrySet();
                Iterator<Map.Entry<String, String>> it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> m = it.next();
                    conn.setRequestProperty(m.getKey(), m.getValue());
                }
            }
            conn.setRequestProperty("Accept-Encoding", "gzip");
            //conn.setDoOutput(true);
            if (RN.Cookie != null) {
                conn.setRequestProperty("Cookie", RN.Cookie);
            }
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream is = null;
                Map<String, List<String>> headers = conn.getHeaderFields();
                if (headers != null) {
                    Set<String> keys = headers.keySet();
                    if (keys.contains("X-IPAIR")) {
                        ServerName = Integer.parseInt(conn.getHeaderField("X-IPAIR"));
                    }
                    if (keys.contains("Content-Encoding")) {
                        if ("gzip".equals(conn.getHeaderField("Content-Encoding"))) {
                            is = new GZIPInputStream(conn.getInputStream());
                        } else {
                            is = conn.getInputStream();
                        }
                    }else {
                        is = conn.getInputStream();
                    }
                } else {
                    is = conn.getInputStream();
                }
                if (is != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int len = 0;
                    while ((len = is.read(buf)) != -1) {
                        baos.write(buf, 0, len);
                    }
                    is.close();
                    IHR = new IpartHttpResult(conn.getResponseCode(), new String(baos.toByteArray()), null, ServerName,baos.size());
                } else {
                    RareFunction.debug("IS=null", 4);
                    IHR = new IpartHttpResult(conn.getResponseCode(),"", null, ServerName,0);
                }
            } else if (RN.errCount++ < 3) {
                return connect(RN, waiting);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (RN.errCount++ < 3) {
                return connect(RN, waiting);
            }
        }
        return IHR;
    }

    public static IpartHttpResult PostConnect(KeepRequestNode RN, int waiting) {
        System.setProperty("http.keepAlive", "true");
        System.setProperty("http.maxConnections", "15");
        IpartHttpResult IHR = null;
        try {
            int ServerName = -1;
            byte buf[] = new byte[2048];
            URL a = new URL(RN.RequestUri);
            HttpURLConnection conn = (HttpURLConnection) a.openConnection();
            if (waiting > 0) {
                conn.setReadTimeout(waiting);
                conn.setConnectTimeout(waiting);
            } else {
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
            }
            if (RN.extraHeader != null) {
                Set<Map.Entry<String, String>> set = RN.extraHeader.entrySet();
                Iterator<Map.Entry<String, String>> it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> m = it.next();
                    conn.setRequestProperty(m.getKey(), m.getValue());
                }
            }
            conn.setRequestProperty("Accept-Encoding", "gzip");
            conn.setRequestProperty("Cookie", RN.Cookie);
             /*if ((RN.Type & 2) == 2) {
                conn.setRequestMethod("POST");
            } else {*/
            conn.setRequestMethod("GET");
            //}
            /*
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
connection.setRequestProperty("Content-Language", "zh-tw");
conn.setUseCaches (false);
conn.setDoInput(true);
conn.setDoOutput(true);
DataOutputStream wr = new DataOutputStream (conn.getOutputStream ());
wr.writeBytes (urlParameters);
wr.flush ();
wr.close ();
             */
            if (conn.getResponseCode() == 200) {
                InputStream is = null;
                Map<String, List<String>> headers = conn.getHeaderFields();
                if (headers != null) {
                    Set<String> keys = headers.keySet();
                    if (keys.contains("X-IPAIR")) {
                        ServerName = Integer.parseInt(conn.getHeaderField("X-IPAIR"));
                    }
                    if (keys.contains("Content-Encoding")) {
                        if ("gzip".equals(conn.getHeaderField("Content-Encoding"))) {
                            is = new GZIPInputStream(is);
                        } else {
                            is = conn.getInputStream();
                        }
                    }
                } else {
                    is = conn.getInputStream();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                is.close();
                IHR = new IpartHttpResult(conn.getResponseCode(), new String(baos.toByteArray()), null, ServerName,baos.size());
            } else if (RN.errCount++ < 3) {
                return connect(RN, waiting);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (RN.errCount++ < 3) {
                return connect(RN, waiting);
            }
        }
        return IHR;
    }
}

