package com.spogss.sportifycommunity.model;

import android.view.View;

/**
 * Created by samue on 27.03.2018.
 */

public class ListMenuModel {
    private Integer vid;

    private int icon;
    private String title;
    private String counter;

    private View.OnClickListener onClickListener;

    private boolean isGroupHeader = false;

    public ListMenuModel(String title) {
        this(null, -1,title,null);
        isGroupHeader = true;
    }

    public ListMenuModel(Integer viewId, int icon, String title, String counter) {
        super();
        this.vid = viewId;
        this.icon = icon;
        this.title = title;
        this.counter = counter;
    }

    public ListMenuModel(int icon, String title, String counter) {
        this(null, icon, title, counter);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public boolean isGroupHeader() {
        return isGroupHeader;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Integer getViewId() {
        return vid;
    }
}

