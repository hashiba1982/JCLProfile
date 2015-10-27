package com.chialung.jclprofile.Module;

import java.util.HashMap;

/**
 * Created by Ainaru on 13-12-30.
 */
public class KeepRequestNode {
    public KeepRequestNode(String url) {
        RequestUri=url;
    }
    public KeepRequestNode(String url, byte Type) {
        RequestUri=url;
        this.Type=Type;
    }
    public String RequestUri,Cookie=null;
    public byte Type = 1;//1 GET 2 POST
    public  byte errCount = 0;
    public HashMap<String, String> extraHeader = null;
}
