package com.example.applicationiconwithnumber;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortcutAdd("Application Icon with Number", count);
            }
        });
        findViewById(R.id.increment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortcutDel("Application Icon with Number");
                count++;
                ((TextView) findViewById(R.id.number)).setText(Integer.toString(count));
                shortcutAdd("Application Icon with Number", count);
            }
        });
    }

    private void shortcutAdd(String name, int number) {
        PackageManager pm = getApplicationContext().getPackageManager();
        String packageName = this.getClass().getPackage().getName();
        // Intent to Start activity
        ApplicationInfo info = null;
        try {
            info = pm.getApplicationInfo(packageName, 0);

            // Create bitmap with number in it -> very default. You probably want to give it a more stylish look
            Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Paint paint = new Paint();
            paint.setColor(0xFF808080); // gray
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(50);
            new Canvas(bitmap).drawText("" + number, 50, 50, paint);
//        ImageView icn = findViewById(R.mipmap.icon);
//        icn.setImageBitmap(bitmap);

            if (info != null) {
                Intent shortcutIntent = pm
                        .getLaunchIntentForPackage(packageName);
                if (shortcutIntent != null) {
                    // Decorate the shortcut
                    Intent addIntent = new Intent();
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, info.loadLabel(pm));
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
                    addIntent.putExtra("duplicate", false);

                    // Inform launcher to create shortcut
                    addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                    getApplicationContext().sendBroadcast(addIntent);
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e( "MIAPP", " Error al añadir el icono del shortcut de la app",  e);
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