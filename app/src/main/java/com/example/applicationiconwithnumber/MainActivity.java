package com.example.applicationiconwithnumber;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    private static int count = 1;

    // OjO Hay que añadir dos permisos en el Manifest.
    // <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
    // <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // shortcutAdd("Application Icon with Number", count);
                deleteShortcut(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Enviado el Broadcast", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.increment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteShortcut(getApplicationContext());
                //shortcutDel("Application Icon with Number");
                count++;
                ((TextView) findViewById(R.id.number)).setText(Integer.toString(count));
                //shortcutAdd("Application Icon with Number", count);
                addShortcut(getApplicationContext(), count);
            }
        });

        ((TextView) findViewById(R.id.number)).setText(Integer.toString(count));
    }

    private Intent getShortcutIntent() {
        Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
//        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        return shortcutIntent;
    }

    // https://stackoverflow.com/questions/14499123/installing-shortcut-wihout-duplicating-native-handly-created-one
    public void addShortcut(Context context, int number)
    {
        CreateBitmapForShortcut cbfs = new CreateBitmapForShortcut(R.mipmap.ic_launcher);
        Bitmap bitmap = cbfs.generate(this, number);
        ImageView icn = findViewById(R.id.icon);
        icn.setImageBitmap(bitmap);
        this.manageShortcutAction(context, "com.android.launcher.action.INSTALL_SHORTCUT", getShortcutIntent(), bitmap);
        // this.manageShortcutAction(context, Intent.ACTION_CREATE_SHORTCUT, shortcutIntent, bitmap);
    }
    public void deleteShortcut(Context context)
    {
        // this.manageShortcutAction(context, "com.android.launcher.action.UNINSTALL_SHORTCUT", shortcutIntent, bitmap);
        this.manageShortcutAction(context, "com.android.launcher.action.UNINSTALL_SHORTCUT", getShortcutIntent(), null);
//        ShortcutManager scm = (ShortcutManager) getApplicationContext().getSystemService( SHORTCUT_SERVICE);
//        scm.disableShortcuts();
    }
    private void manageShortcutAction(Context context, String intentAction, Intent launchIntent, Bitmap bitmap)
    {
        Context applicationContext = context.getApplicationContext();
        ApplicationInfo appInfo = applicationContext.getApplicationInfo();
        PackageManager packageManager= applicationContext.getPackageManager();
        String applicationName = (String) packageManager.getApplicationLabel(appInfo);

        Intent shortcut = new Intent(intentAction);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, applicationName); // Shortcut name
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);// Setup activity should be shortcut object
        if( null != bitmap) {
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
            shortcut.putExtra("duplicate", false); // Just create once
        }
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(applicationContext, appInfo.icon));// Set shortcut icon

        applicationContext.sendBroadcast(shortcut);
    }

    // References: https://developer.android.com/guide/topics/ui/shortcuts/index.html
    // References: https://stackoverflow.com/questions/1103027/how-to-change-an-application-icon-programmatically-in-android
    private void shortcutAdd(String name, int number) {
        PackageManager pm = getApplicationContext().getPackageManager();
        String packageName = this.getClass().getPackage().getName();

        try {
            ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
            CreateBitmapForShortcut cbfs = new CreateBitmapForShortcut(R.mipmap.ic_launcher);
            Bitmap bitmap = cbfs.generate(this, number);
            ImageView icn = findViewById(R.id.icon);
            icn.setImageBitmap(bitmap);

            if (info != null) {
                // Intent to Start activity
                //Intent shortcutIntent = pm
                //        .getLaunchIntentForPackage(packageName);
                Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
                shortcutIntent.setAction( Intent.ACTION_SEND);
                if (shortcutIntent != null) {
                    // Decorate the shortcutBuilder
                    Intent addIntent = new Intent();
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, info.loadLabel(pm));
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
                    addIntent.putExtra("duplicate", false);

                    // Inform launcher to create shortcutBuilder
//                    addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                    addIntent.setAction(Intent.ACTION_CREATE_SHORTCUT);
                    getApplicationContext().sendBroadcast(addIntent);
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e( "MIAPP", " Error al añadir el icono del shortcutBuilder de la app",  e);
        }
    }

    private void shortcutDel(String name) {
        PackageManager pm = getApplicationContext().getPackageManager();
        String packageName = this.getClass().getPackage().getName();
        // Intent to Start activity
        ApplicationInfo info = null;
        try {
            info = pm.getApplicationInfo(packageName, 0);

            if (info != null) {
                Intent shortcutIntent = pm
                        .getLaunchIntentForPackage(packageName);
                if (shortcutIntent != null) {
                    // Decorate the shortcut
                    Intent delIntent = new Intent();
                    delIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
                    delIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
                    delIntent.putExtra("duplicate", false);

                    // Inform launcher to remove shortcut
                    delIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
                    getApplicationContext().sendBroadcast(delIntent);
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e( "MIAPP", " Error al añadir el icono del shortcut de la app",  e);
        }
    }
}
