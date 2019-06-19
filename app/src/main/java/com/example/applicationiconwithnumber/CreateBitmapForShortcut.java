package com.example.applicationiconwithnumber;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class CreateBitmapForShortcut {
    private int width = 100;
    private int height = 100;
    private int iconId = 0;
    private int density = DisplayMetrics.DENSITY_XXXHIGH;
    private int backgroundColor = 0;
    private int backgroundX = 75;
    private int backgroundY = 30;
    private int backgroundRadius = 25;
    private Paint.Align textAlign = Paint.Align.CENTER;
    private int textColor = 0;
    private int textX = 75;
    private int textY = 50;
    private int textSize = 40;

    public CreateBitmapForShortcut(int iconId) {
        this.iconId = iconId;
        this.backgroundColor = Color.parseColor("#FF606060");
        this.textColor = Color.parseColor("#F0FFFFFF");
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBackgroundX() {
        return backgroundX;
    }

    public void setBackgroundX(int backgroundX) {
        this.backgroundX = backgroundX;
    }

    public int getBackgroundY() {
        return backgroundY;
    }

    public void setBackgroundY(int backgroundY) {
        this.backgroundY = backgroundY;
    }

    public int getBackgroundRadius() {
        return backgroundRadius;
    }

    public void setBackgroundRadius(int backgroundRadius) {
        this.backgroundRadius = backgroundRadius;
    }

    public Paint.Align getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(Paint.Align textAlign) {
        this.textAlign = textAlign;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextX() {
        return textX;
    }

    public void setTextX(int textX) {
        this.textX = textX;
    }

    public int getTextY() {
        return textY;
    }

    public void setTextY(int textY) {
        this.textY = textY;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public Bitmap generate(Context context, int number) {
        Bitmap bitmapIcon = null;// BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Drawable dr = context.getResources().getDrawableForDensity(iconId, density);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) dr;
        if(bitmapDrawable.getBitmap() != null) {
            bitmapIcon = bitmapDrawable.getBitmap();
        } else {
            if(dr.getIntrinsicWidth() <= 0 || dr.getIntrinsicHeight() <= 0) {
                bitmapIcon = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmapIcon = Bitmap.createBitmap(dr.getIntrinsicWidth(), dr.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmapIcon);
            dr.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            dr.draw(canvas);
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        new Canvas(bitmap).drawBitmap(Bitmap.createScaledBitmap(bitmapIcon, width, height, false), 0f, 0f, paint);
        createBackground(bitmap, paint);
        createText(bitmap, paint, number);

        return bitmap;
    }

    protected void createBackground(Bitmap bitmap, Paint paint) {
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);
        new Canvas(bitmap).drawCircle( backgroundX, backgroundY, backgroundRadius, paint);
    }

    protected void createText(Bitmap bitmap, Paint paint, int number) {
        paint.setTextAlign( textAlign);
        paint.setColor( textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        new Canvas(bitmap).drawText( Integer.toString( number), textX, textY, paint);
    }
}
