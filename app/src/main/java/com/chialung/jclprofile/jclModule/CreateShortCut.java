package com.chialung.jclprofile.jclModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by chialung on 2015/5/5.
 */
public class CreateShortCut {
    public void CreateShortCut(){

    }

    public void add(Context m_context, Class m_class){


        //探測Home screen有沒有加入shortcut
        SharedPreferences sharedPreferences = m_context.getSharedPreferences("checkShortcut", Context.MODE_PRIVATE);
        boolean isAddShortcut = sharedPreferences.getBoolean("PREF_KEY_SHORTCUT_ADDED", false);
        if (isAddShortcut) return;


        //加入shortcut
        Intent shortcutIntent = new Intent(m_context, m_class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent it = new Intent();
        it.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        it.putExtra(Intent.EXTRA_SHORTCUT_NAME, m_context.getApplicationInfo().loadLabel(m_context.getPackageManager()));
        it.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(m_context, m_context.getApplicationInfo().icon));
        it.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        m_context.sendBroadcast(it);

        // Remembering that ShortCut was already added
        SharedPreferences.Editor saveShortcut = sharedPreferences.edit();
        saveShortcut.putBoolean("PREF_KEY_SHORTCUT_ADDED", true);
        saveShortcut.commit();


    }
}
