package com.shijc.fileexplorer.model;

/**
 * Class:ClassifyFileResult
 * Description:
 * User: shijiacheng.
 * Date: 2017/3/12.
 */

public class ClassifyFileResult {
    public static final int PICTURE = 1;
    public static final int MUSIC = 2;
    public static final int VIDEO = 3;
    public static final int DOCUMENT = 4;
    public static final int ZIP = 5;
    public static final int APK = 6;

    private int key;
    private String name;
    private long count;
    private long size;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
