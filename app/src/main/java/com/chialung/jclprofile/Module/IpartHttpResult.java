package com.chialung.jclprofile.Module;

import org.apache.http.client.CookieStore;

public class IpartHttpResult {
	private int state_code = 0;
	private String result_str = "";
    private int serverHeader = -1;
	private CookieStore cookieStore = null;



    private int ContentLength = 0;
    public int getserverHeader() {
        return serverHeader;
    }
    public int getContentLength() {
        return ContentLength;
    }

    public void setContentLength(int contentLength) {
        ContentLength = contentLength;
    }

    public int getServerHeader() {
        return serverHeader;
    }

    public void setServerHeader(int serverHeader) {
        this.serverHeader = serverHeader;
    }
	public int getState_code() {
		return state_code;
	}

	public void setState_code(int state_code) {
		this.state_code = state_code;
	}

	public String getResult_str() {
		return result_str;
	}

	public void setResult_str(String result_str) {
		this.result_str = result_str;
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	public IpartHttpResult(short code, String str) {
		this.state_code = code;
		this.result_str = str;
	}

	public IpartHttpResult(int code, String str) {
		this.state_code = code;
		this.result_str = str;
	}

	public IpartHttpResult(short code, String str, CookieStore cookieStore) {
		this.state_code = code;
		this.result_str = str;
		this.cookieStore = cookieStore;
	}

	public IpartHttpResult(int code, String str, CookieStore cookieStore) {
		this.state_code = code;
		this.result_str = str;
		this.cookieStore = cookieStore;
	}

	public IpartHttpResult(short code, String str, CookieStore cookieStore, int serverHeader, int ContentLength) {
		this.state_code = code;
		this.result_str = str;
		this.cookieStore = cookieStore;
		this.serverHeader = serverHeader;
        this.ContentLength=ContentLength;
	}

	public IpartHttpResult(int code, String str, CookieStore cookieStore, int serverHeader, int ContentLength) {
		this.state_code = code;
		this.result_str = str;
		this.cookieStore = cookieStore;
		this.serverHeader = serverHeader;
        this.ContentLength=ContentLength;
	}
}
