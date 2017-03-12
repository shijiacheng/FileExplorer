package com.shijc.fileexplorer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.shijc.fileexplorer.R;

import java.text.DecimalFormat;

/**
 * Class:FileUtils
 * Description:
 * User: shijiacheng.
 * Date: 2017/3/12.
 */

public class FileUtils {
    /**
     * 格式化文件大小的显示方式
     *
     * @param fileSize
     *            文件的大小
     * @return 返回格式化后的文件的显示字符串，形如：128KB
     */
    public static String formatFileSize(long fileSize) {
        if(fileSize==0){
            return "0 B";
        }
        String fileSizeStr = "";
        if (fileSize < 1024) {
            fileSizeStr = fileSize + " B";
        } else if (fileSize < 1048576) {
            DecimalFormat df = new DecimalFormat("#.0");
            fileSizeStr = df.format((double) fileSize / 1024) + " KB";
        } else if (fileSize < 1073741824) {
            DecimalFormat df = new DecimalFormat("#.0");
            fileSizeStr = df.format((double) fileSize / 1048576) + " MB";
        } else {
            DecimalFormat df = new DecimalFormat("#.00");
            fileSizeStr = df.format((double) fileSize / 1073741824) + " GB";
        }
        return fileSizeStr;
    }


    /**
     * 获得文件的拓展名
     *
     * @param fileName
     *            文件名
     * @return 返回文件的拓展名
     */
    public static String getExt(String fileName) {
        String ext = "";
        if(null!=fileName){
            int pointIndex = fileName.lastIndexOf(".");
            if (pointIndex > -1) {
                ext = fileName.substring(pointIndex + 1).toLowerCase();
            }
        }
        return ext;
    }


    /**
     * 获取各种文件的图标
     *
     * @param cxt
     * @param suffix
     * @return
     */
    public static Bitmap getFileBm(Context cxt, String suffix) {
        if ("pdf".equals(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_file_pdf);
        } else if ("ppt".equalsIgnoreCase(suffix)
                || "pptx".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_file_ppt);
        } else if ("xls".equalsIgnoreCase(suffix)
                || "xlsx".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_file_xls);
        } else if ("doc".equalsIgnoreCase(suffix)
                || "docx".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_file_doc);
        } else if ("txt".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_file_txt);
        } else if ("rar".equalsIgnoreCase(suffix)
                || "zip".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_file_rar);
        } else if ("png".equalsIgnoreCase(suffix)
                || "gif".equalsIgnoreCase(suffix)
                || "bmp".equalsIgnoreCase(suffix)
                || "jpg".equalsIgnoreCase(suffix)
                || "jpeg".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_file_image);
        } else if ("mp4".equalsIgnoreCase(suffix)
                || "avi".equalsIgnoreCase(suffix)
                || "mov".equalsIgnoreCase(suffix)
                || "flv".equalsIgnoreCase(suffix)
                || "wmv".equalsIgnoreCase(suffix)
                || "rmvb".equalsIgnoreCase(suffix)
                || "rm".equalsIgnoreCase(suffix)
                || "3gp".equalsIgnoreCase(suffix)
                || "mpg".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_files_vedio);
        } else if ("mp3".equalsIgnoreCase(suffix)
                || "wma".equalsIgnoreCase(suffix)
                || "amr".equalsIgnoreCase(suffix)
                || "ape".equalsIgnoreCase(suffix)
                || "wav".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_attach_audio);
        }
        return BitmapFactory.decodeResource(cxt.getResources(),
                R.drawable.file_icon_unknow);
    }
}
