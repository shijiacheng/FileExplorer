package com.shijc.fileexplorer.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import com.shijc.fileexplorer.AppContext;
import com.shijc.fileexplorer.R;

import java.io.File;
import java.io.FileNotFoundException;
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
                    R.drawable.icon_pdf);
        } else if ("ppt".equalsIgnoreCase(suffix)
                || "pptx".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_ppt);
        } else if ("xls".equalsIgnoreCase(suffix)
                || "xlsx".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_excel);
        } else if ("doc".equalsIgnoreCase(suffix)
                || "docx".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_word);
        } else if ("txt".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_txt);
        } else if ("rar".equalsIgnoreCase(suffix)
                || "zip".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_zip);
        } else if ("png".equalsIgnoreCase(suffix)
                || "gif".equalsIgnoreCase(suffix)
                || "bmp".equalsIgnoreCase(suffix)
                || "jpg".equalsIgnoreCase(suffix)
                || "jpeg".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_unknown);
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
                    R.drawable.icon_video);
        } else if ("mp3".equalsIgnoreCase(suffix)
                || "wma".equalsIgnoreCase(suffix)
                || "amr".equalsIgnoreCase(suffix)
                || "ape".equalsIgnoreCase(suffix)
                || "wav".equalsIgnoreCase(suffix)) {
            return BitmapFactory.decodeResource(cxt.getResources(),
                    R.drawable.icon_music);
        }
        return BitmapFactory.decodeResource(cxt.getResources(),
                R.drawable.icon_unknown);
    }


    public static void openLocalFile(String fileName,Context context){

        try {
            context.startActivity(getOpenFileIntent(fileName,null));
        }  catch (FileNotFoundException e) {
            Toast.makeText(context, R.string.ex_file_not_found, Toast.LENGTH_SHORT).show();
        } catch (ActivityNotFoundException e){
            Toast.makeText(context,context.getString( R.string.the_file_not_open, fileName), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 打开文件
     *
     * @param fileName
     *            文件的名称
     * @return
     */
    public static Intent getOpenFileIntent(String fileName, String ext) throws FileNotFoundException,ActivityNotFoundException{
        File file = new File(fileName);
        if (!file.exists())
            throw new FileNotFoundException(AppContext.getInstance().getString(R.string.ex_file_file_not_found));
		/* 取得扩展名 */
        String end = "";
        if (ext != null) {
            end = ext;
        }else {
            end = getExt(fileName);
        }

		/* 依扩展名的类型决定MimeType */
        if (end.equalsIgnoreCase("m4a") || end.equalsIgnoreCase("mp3") || end.equalsIgnoreCase("mid") || end.equalsIgnoreCase("xmf")
                || end.equalsIgnoreCase("ogg") || end.equalsIgnoreCase("wav") || end.equalsIgnoreCase("wma")) {
            return getAudioFileIntent(fileName);

        } else if (end.equalsIgnoreCase("3gp") || end.equalsIgnoreCase("mp4") || end.equalsIgnoreCase("avi") || end.equalsIgnoreCase("mov")
                || end.equalsIgnoreCase("wav") || end.equalsIgnoreCase("dmv") || end.equalsIgnoreCase("wmv") || end.equalsIgnoreCase("rmvb")
                || end.equalsIgnoreCase("rm")) {
            return getVideoFileIntent(fileName);
        } else if (end.equalsIgnoreCase("jpg") || end.equalsIgnoreCase("gif") || end.equalsIgnoreCase("png") || end.equalsIgnoreCase("jpeg")
                || end.equalsIgnoreCase("bmp")) {
            return getImageFileIntent(fileName);
        } else if (end.equalsIgnoreCase("apk")) {
            return getApkFileIntent(fileName);
        } else if (end.equalsIgnoreCase("ppt") || end.equalsIgnoreCase("pptx")) {
            return getPptFileIntent(fileName);
        } else if (end.equalsIgnoreCase("xls") || end.equalsIgnoreCase("xlsx")) {
            return getExcelFileIntent(fileName);
        } else if (end.equalsIgnoreCase("doc") || end.equalsIgnoreCase("docx")) {
            return getWordFileIntent(fileName);
        } else if (end.equalsIgnoreCase("pdf")) {
            return getPdfFileIntent(fileName);
        } else if (end.equalsIgnoreCase("chm")) {
            return getChmFileIntent(fileName);
        } else if (end.equalsIgnoreCase("txt")) {
            return getTextFileIntent(fileName, false);
        } else {
            try {
return getAllIntent(fileName);
            }catch (Exception r){

                throw new ActivityNotFoundException(AppContext.getInstance().getString(R.string.ex_file_not_open));
            }

        }
    }


    public static Intent getAllIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    public static Intent getApkFileIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    public static Intent getVideoFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    public static Intent getAudioFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    public static Intent getHtmlFileIntent(String param) {
        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    public static Intent getImageFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    public static Intent getPptFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    public static Intent getExcelFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    public static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    public static Intent getChmFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    public static Intent getTextFileIntent(String param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    public static Intent getPdfFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
}
