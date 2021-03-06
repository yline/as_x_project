package com.yline.file.module.file.db;

import java.io.File;

/**
 * 文件，数据库，储存数据
 *
 * @author yline 2018/1/29 -- 11:28
 * @version 1.0.0
 */
public class FileDbModel {
    /**
     * 如果是文件，结尾不带 '/'
     * 如果是文件夹，结尾带 '/'
     */
    private String absolutePath;
    private int fileType;
    private boolean isDir; // 是否是文件夹，用于直接统计个数
    private byte[] data;

    public FileDbModel(String absolutePath, int fileType, boolean isDir, byte[] data) {
        this.absolutePath = isDir ? absolutePath + File.separator : absolutePath;
        this.fileType = fileType;
        this.isDir = isDir;
        this.data = data;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
}
