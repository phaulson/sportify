package com.spogss.sportifypro.gmodel;

/**
 * Created by samue on 27.03.2018.
 */

public class ListMenuModel {

    private int icon;
    private String title;
    private String counter;

    private boolean isGroupHeader = false;

    public ListMenuModel(String title) {
        this(-1,title,null);
        isGroupHeader = true;
    }

    public ListMenuModel(int icon, String title, String counter) {
        super();
        this.icon = icon;
        this.title = title;
        this.counter = counter;
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
}

