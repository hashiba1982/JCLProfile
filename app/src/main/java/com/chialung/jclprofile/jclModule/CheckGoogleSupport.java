package com.chialung.jclprofile.jclModule;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by chialung on 2015/6/10.
 */
public class CheckGoogleSupport {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static boolean checkPlayServices(Activity m_activity) {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(m_activity.getApplicationContext());

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, m_activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();

            } else {
                Tools.debug("This device is not supported Google Play Server", 3);
            }
            return false;
        }
        return true;
    }

    public static boolean checkGoogleAccount(Activity m_activity){

        AccountManager accountManager = AccountManager.get(m_activity.getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType("com.google");

        if (accounts.length != 0){
            Tools.debug("This device has Google Account", 3);
            return true;
        }else{
            Tools.debug("This device do not have Google Account, please regist one", 3);
            return false;
        }
    }
}
