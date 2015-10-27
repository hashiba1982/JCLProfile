package com.chialung.jclprofile.Module;

import android.os.Build;
import android.os.Message;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

public class IpartHttp {
    //public static String X_IPART_M = null;
    HttpParams httpParameters = null;
    public DefaultHttpClient httpclient = null;
    private String URL = null;
    private static long startTime = System.currentTimeMillis();
    private HashMap<String, String> moreHeader = null;

    public IpartHttp() {
        this.httpParameters = new BasicHttpParams();
        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
        HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
        HttpConnectionParams.setSoTimeout(httpParameters, 30000);
        this.httpclient = getNewHttpClient();
        //Set_X_IPART_M();
    }

    public void test() {
        /*AssetManager assManager = getActivity().getAssets();
        InputStream is = null;
        try {
            is = assManager.open("cacerts");
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream caInput = new BufferedInputStream(is);
        trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(caInput, "123456".toCharArray());
        SSLSocketFactory sf = new org.apache.http.conn.ssl.SSLSocketFactory(trustStore);*/
    }

    public DefaultHttpClient getNewHttpClient() {
        try {
            /*Security.addProvider(new BouncyCastleProvider());
            AssetManager assManager = MainActivity.getMain().getAssets();
            InputStream is = null;
            try {
                is = assManager.open("myKeystore.bks");
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            // SLSocketFactory sf = null;
//              trustStore.load(null, null);
            //sf = new SSLSocketFactory(trustStore);
            //sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);*/

            /*KeyStore trustStore = KeyStore.getInstance("BKS");
            trustStore.load(is, "123456".toCharArray());*/
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactoryEx sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(this.httpParameters, registry);
            return new DefaultHttpClient(ccm, this.httpParameters);
        } catch (Exception e) {
            return new DefaultHttpClient(this.httpParameters);
        }
    }

    public void setCookie(CookieStore cookieStore) {
        if (cookieStore != null) {
            httpclient.setCookieStore(cookieStore);
        }
    }

    public IpartHttpResult doPost(String RequestURL, List<NameValuePair> m_params) {
        return this.doPost(RequestURL, m_params, false);
    }

    public IpartHttpResult doPost(String RequestURL, MultipartEntity m_entity) {
        return this.doPost(RequestURL, m_entity, false);
    }

    public IpartHttpResult doPost(String RequestURL, MultipartEntity m_entity, boolean isGetCookie) {
        if (!true) {
            RareFunction.debug("ERROR LOG CODE 999", 3);
        }
        try {
            URL = RequestURL;
            // show_cookie();
            //Set_X_IPART_M();
            HttpPost httppost = new HttpPost(RequestURL);
            httppost.setHeader("User-Agent", null);
            httppost.setHeader("dn", Build.MODEL);
            httppost.setHeader("Accept-Encoding", "gzip");
            if (moreHeader != null) {
                Set<String> set = moreHeader.keySet();
                Iterator<String> it = set.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    httppost.setHeader(key, moreHeader.get(key));
                }
            }
            httppost.setEntity(m_entity);
            return this.GetResultObj(httpclient.execute(httppost), isGetCookie);
        } catch (ConnectTimeoutException e) {
            Error_log.ipart_ErrProcess(e, RequestURL);
        } catch (Exception e) {
            e.printStackTrace();
            // Error_log.ipart_ErrProcess(e, RequestURL);
        } finally {
            //httpclient.getConnectionManager().shutdown();
        }
        return (new IpartHttpResult(Error_log.OTHER_EXCEPTION_CODE, null));
    }

    public IpartHttpResult doPost(String RequestURL, List<NameValuePair> m_params, boolean isGetCookie) {
        if (!true) {
            return (new IpartHttpResult(Error_log.OTHER_EXCEPTION_CODE, null));
        }
        URL = RequestURL;
        RareFunction.debug("IpartHttp", "POST:" + URL);
        try {
            // show_cookie();
            HttpPost httppost = new HttpPost(RequestURL);
            httppost.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            httppost.setHeader("User-Agent", null);
            httppost.setHeader("Accept-Encoding", "gzip");
            httppost.setHeader("dn", Build.MODEL);
            if (moreHeader != null) {
                Set<String> set = moreHeader.keySet();
                Iterator<String> it = set.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    httppost.setHeader(key, moreHeader.get(key));
                }
            }
            httppost.setEntity(new UrlEncodedFormEntity(m_params, HTTP.UTF_8));
            return this.GetResultObj(httpclient.execute(httppost), isGetCookie);
        } catch (ConnectTimeoutException e) {
            Error_log.ipart_ErrProcess(e, RequestURL);
        } catch (Exception e) {
            // Error_log.ipart_ErrProcess(e, RequestURL);
        } finally {
            //httpclient.getConnectionManager().shutdown();
        }
        return (new IpartHttpResult(Error_log.OTHER_EXCEPTION_CODE, null));
    }

    public IpartHttpResult doGet(String RequestURL) {
        return this.doGet(RequestURL, false);
    }

    public IpartHttpResult doDelete(String RequestURL) {
        if (!true) {
            return (new IpartHttpResult(Error_log.OTHER_EXCEPTION_CODE, null));
        }
        try {
            URL = RequestURL;
            HttpDelete httpdel = new HttpDelete(RequestURL);
            httpdel.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            httpdel.setHeader("User-Agent", null);
            httpdel.setHeader("Accept-Encoding", "gzip");
            httpdel.setHeader("dn", Build.MODEL);
            if (moreHeader != null) {
                Set<String> set = moreHeader.keySet();
                Iterator<String> it = set.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    httpdel.setHeader(key, moreHeader.get(key));
                }
            }
            return this.GetResultObj(httpclient.execute(httpdel));
        } catch (ConnectTimeoutException e) {
            Error_log.ipart_ErrProcess(e, RequestURL);
        } catch (Exception e) {
            Error_log.ipart_ErrProcess(e, RequestURL);
        } finally {
            //httpclient.getConnectionManager().shutdown();
        }
        return (new IpartHttpResult(Error_log.OTHER_EXCEPTION_CODE, null));
    }

    public IpartHttpResult doPut(String RequestURL, StringEntity stringEntity) {
        if (!true) {
            return (new IpartHttpResult(Error_log.OTHER_EXCEPTION_CODE, null));
        }
        try {
            URL = RequestURL;
            HttpPut httpput = new HttpPut(RequestURL);
            httpput.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            httpput.setHeader("User-Agent", null);
            httpput.setHeader("Accept-Encoding", "gzip");
            httpput.setHeader("dn", Build.MODEL);
            if (moreHeader != null) {
                Set<String> set = moreHeader.keySet();
                Iterator<String> it = set.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    httpput.setHeader(key, moreHeader.get(key));
                }
            }
            httpput.setEntity(stringEntity);
            return this.GetResultObj(httpclient.execute(httpput));
        } catch (ConnectTimeoutException e) {
            Error_log.ipart_ErrProcess(e, RequestURL);
        } catch (Exception e) {
            Error_log.ipart_ErrProcess(e, RequestURL);
        } finally {
            //httpclient.getConnectionManager().shutdown();
        }
        return (new IpartHttpResult(Error_log.OTHER_EXCEPTION_CODE, null));
    }

    public IpartHttpResult doPut(String RequestURL, FileEntity fileEntity) {
        if (!true) {
            return (new IpartHttpResult(Error_log.OTHER_EXCEPTION_CODE, null));
        }
        try {
            URL = RequestURL;
            HttpPut httpput = new HttpPut(RequestURL);
            httpput.setHeader("User-Agent", null);
            httpput.setHeader("Accept-Encoding", "gzip");
            httpput.setHeader("dn", Build.MODEL);
            if (moreHeader != null) {
                Set<String> set = moreHeader.keySet();
                Iterator<String> it = set.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    httpput.setHeader(key, moreHeader.get(key));
                }
            }
            httpput.setEntity(fileEntity);
            return this.GetResultObj(httpclient.execute(httpput));
        } catch (ConnectTimeoutException e) {
            Error_log.ipart_ErrProcess(e, RequestURL);
        } catch (Exception e) {
            Error_log.ipart_ErrProcess(e, RequestURL);
        } finally {
            //httpclient.getConnectionManager().shutdown();
        }
        return (new IpartHttpResult(Error_log.OTHER_EXCEPTION_CODE, null));
    }

    public IpartHttpResult doGet(String RequestURL, boolean isGetCookie) {
        if (!true) {
            return (new IpartHttpResult(Error_log.OTHER_EXCEPTION_CODE, null));
        }
        URL = RequestURL;
        RareFunction.debug("IpartHttp", "GET:" + URL);
        try {
            // show_cookie();
            HttpGet httpget = new HttpGet(RequestURL);
            httpget.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            httpget.setHeader("User-Agent", null);
            httpget.setHeader("dn", Build.MODEL);
            httpget.setHeader("Accept-Encoding", "gzip");
            if (moreHeader != null) {
                Set<String> set = moreHeader.keySet();
                Iterator<String> it = set.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    httpget.setHeader(key, moreHeader.get(key));
                }
            }
            return this.GetResultObj(httpclient.execute(httpget), isGetCookie);
        } catch (ConnectTimeoutException e) {
            Error_log.ipart_ErrProcess(e, RequestURL);
        } catch (Exception e) {
            e.printStackTrace();
            // Error_log.ipart_ErrProcess(e, RequestURL);
        } finally {
            //httpclient.getConnectionManager().shutdown();
        }
        return (new IpartHttpResult(Error_log.OTHER_EXCEPTION_CODE, null));
    }

    public IpartHttpResult GetResultObj(HttpResponse response) {
        return GetResultObj(response, false);
    }

    public IpartHttpResult GetResultObj(HttpResponse response, boolean isGetCookie) {
        try {
            // httpclient.getConnectionManager().shutdown();
            RareFunction.debug("IpartHttp", "Http return Code:" + response.getStatusLine().getStatusCode() + " = " + URL, 2);
            int ServerName;
            try {
                ServerName = Integer.parseInt(response.getFirstHeader("X-IPAIR").getValue());
            } catch (Exception e) {
                ServerName = 0;
            }
            Message msg;
            String res_str = null;
            int length = 0;
            switch (response.getStatusLine().getStatusCode()) {
                case HttpStatus.SC_OK:// 200
                    try {
                        /*Header[] all=response.getAllHeaders();
                        for(Header h:all){
							RareFunction.debug("IpartHttp",h.getName()+"="+h.getValue(), 3);
						}*/
                        try {
                            Header contentEncoding = response.getFirstHeader("Content-Encoding");
                            if ((contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip"))) {
                                InputStream instream = response.getEntity().getContent();
                                instream = new GZIPInputStream(instream);
                                ByteArrayOutputStream o = new ByteArrayOutputStream(instream.available());
                                byte[] inn = new byte[instream.available()];
                                while ((instream.read(inn)) > -1) {
                                    o.write(inn);
                                }
                                length = o.size();
                                res_str = new String(o.toByteArray(), "utf-8");
                                o.close();
                                instream.close();
                            } else {
                                Header ContentLength = response.getFirstHeader("Content-Length");
                                if (ContentLength != null) {
                                    length = Integer.parseInt(ContentLength.getValue());
                                }
                                res_str = EntityUtils.toString(response.getEntity());
                            }
                        } catch (Exception e) {
                            RareFunction.debug("IpartHttp", "NO HEADER", 4);
                            res_str = EntityUtils.toString(response.getEntity());
                        }
                    } catch (Exception e2) {
                        Error_log.ipart_ErrProcess(e2, "IpartHttp " + ServerName + " UNGZIP FAIL");
                        res_str = EntityUtils.toString(response.getEntity());
                    }
                    // RareFunction.debug("IpartHttp", res_str, 3);
                    if (isGetCookie) {
                        return new IpartHttpResult(response.getStatusLine().getStatusCode(), res_str, httpclient.getCookieStore(), ServerName, length);
                    } else {
                        return new IpartHttpResult(response.getStatusLine().getStatusCode(), res_str, null, ServerName, length);
                    }
                case HttpStatus.SC_CREATED:// 201
                    try {
                        Header contentEncoding = response.getFirstHeader("Content-Encoding");
                        if ((contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip"))) {
                            InputStream instream = response.getEntity().getContent();
                            instream = new GZIPInputStream(instream);
                            ByteArrayOutputStream o = new ByteArrayOutputStream(instream.available());
                            byte[] inn = new byte[instream.available()];
                            while ((instream.read(inn)) > -1) {
                                o.write(inn);
                            }
                            length = o.size();
                            res_str = new String(o.toByteArray(), "utf-8");
                            o.close();
                            instream.close();
                        } else {
                            Header ContentLength = response.getFirstHeader("Content-Length");
                            if (ContentLength != null) {
                                length = Integer.parseInt(ContentLength.getValue());
                            }
                            res_str = EntityUtils.toString(response.getEntity());
                        }
                    } catch (Exception e2) {
                        Error_log.ipart_ErrProcess(e2, "IpartHttp " + ServerName + " UNGZIP FAIL");
                        res_str = EntityUtils.toString(response.getEntity());
                    }
                    if (isGetCookie) {
                        return new IpartHttpResult(response.getStatusLine().getStatusCode(), res_str, httpclient.getCookieStore(), ServerName, length);
                    } else {
                        return new IpartHttpResult(response.getStatusLine().getStatusCode(), res_str, null, ServerName, length);
                    }
                case HttpStatus.SC_FORBIDDEN:
                    //if (ServerName==1) {
                    msg = new Message();
                    msg.arg1 = ServerName;
                    msg.arg2 = response.getStatusLine().getStatusCode();
                    msg.obj = URL;
                    Error_log.ipart_ErrProcess(HttpStatus.SC_FORBIDDEN, null, msg, "");
                    //}
                    return (new IpartHttpResult(response.getStatusLine().getStatusCode(), null));
                case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                    //if (ServerName==1) {
                    msg = new Message();
                    msg.arg1 = ServerName;
                    msg.arg2 = response.getStatusLine().getStatusCode();
                    msg.obj = URL;
                    Error_log.ipart_ErrProcess(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, msg, "");
                    //}
                    RareFunction.debug("IpartHttp", "Http fail Code:" + response.getStatusLine().getStatusCode() + " = " + URL, 3);
                    return (new IpartHttpResult(response.getStatusLine().getStatusCode(), null));
                case HttpStatus.SC_BAD_GATEWAY:
                    //if (ServerName==1) {
                    msg = new Message();
                    msg.arg1 = ServerName;
                    msg.arg2 = response.getStatusLine().getStatusCode();
                    msg.obj = URL;
                    Error_log.ipart_ErrProcess(HttpStatus.SC_BAD_GATEWAY, null, msg, "");
                    //}
                    RareFunction.debug("IpartHttp", "Http fail Code:" + response.getStatusLine().getStatusCode() + " = " + URL, 3);
                    return (new IpartHttpResult(response.getStatusLine().getStatusCode(), null));
                case HttpStatus.SC_GATEWAY_TIMEOUT:
                    //if (ServerName==1) {
                    msg = new Message();
                    msg.arg1 = ServerName;
                    msg.arg2 = response.getStatusLine().getStatusCode();
                    msg.obj = URL;
                    Error_log.ipart_ErrProcess(HttpStatus.SC_GATEWAY_TIMEOUT, null, msg, "");
                    //}
                    RareFunction.debug("IpartHttp", "Http fail Code:" + response.getStatusLine().getStatusCode() + " = " + URL, 3);
                    return (new IpartHttpResult(response.getStatusLine().getStatusCode(), null));
                default:
                    return (new IpartHttpResult(response.getStatusLine().getStatusCode(), null, null, ServerName, 0));
            }
        } catch (Exception e) {
            return (new IpartHttpResult(response.getStatusLine().getStatusCode(), null));
        }
    }

/*    public void show_cookie() {
        if (UserConfig.m_cookieStore.getCookies().size() == 0) {
            List<Cookie> list = UserConfig.m_cookieStore.getCookies();
            Iterator<Cookie> it = list.iterator();
            while (it.hasNext()) {
                Cookie temp = it.next();
                RareFunction.debug("IpartHttp", "IpartHttp Cookie [" + temp.getName() + "] " + temp.getValue(), 2);
            }
            this.setCookie(UserConfig.m_cookieStore);
            RareFunction.debug("IpartHttp", "SET COOKIE OK", 2);
        } else {
            RareFunction.debug("IpartHttp", "IpartHttp Cookie is null", 3);
        }
    }*/

    public void setMoreHeader(HashMap<String, String> moreHeader) {
        this.moreHeader = moreHeader;
    }

    private Inflater decompresser = new Inflater();
}

