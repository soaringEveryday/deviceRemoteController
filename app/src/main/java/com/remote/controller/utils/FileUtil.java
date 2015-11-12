package com.qianmi.epos.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by su on 2015/7/8.
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    private static final String LOCAL = "dianguanjia";
    private static final String DATAFILE = "images";
    private static String localFiePath = Environment.getExternalStorageDirectory().getPath() + File.separator + LOCAL + File.separator;


    /**
     * 判断是否存在存储空间	 *
     *
     * @return
     */
    public static boolean isExitsdCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    private static boolean hasFile(String fileName) {
        File f = createFile(fileName);
        return null != f && f.exists();
    }

    public static File createFile(String fileName) {
        File dirRootFile = new File(localFiePath);
        if (!dirRootFile.exists()) {
            dirRootFile.mkdirs();
        }
        File dirFile = new File(localFiePath + DATAFILE + File.separator);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myCaptureFile = new File(localFiePath + DATAFILE + File.separator + fileName);
        if (myCaptureFile.exists()) {
            myCaptureFile.delete();
        }
        try {
            myCaptureFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }

    public static String getImageFile(String imageName) {
        File dirRootFile = new File(localFiePath);
        if (!dirRootFile.exists()) {
            dirRootFile.mkdirs();
        }
        File dirFile = new File(localFiePath + DATAFILE + File.separator);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myCaptureFile = new File(localFiePath + DATAFILE + File.separator + imageName + ".jpg");
        if (!myCaptureFile.exists()) {
            try {
                myCaptureFile.createNewFile();
                return myCaptureFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        return myCaptureFile.getAbsolutePath();
    }

    public static String processPicture(Bitmap bitmap) {
        ByteArrayOutputStream jpeg_data = new ByteArrayOutputStream();
        byte[] output = null;
        byte[] code = null;
        try {
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, jpeg_data)) {
                code = jpeg_data.toByteArray();
                output = Base64.encode(code, Base64.NO_WRAP);
                return new String(output);
            }
        } catch (Exception e) {
            return ("Error compressing image.");
        } finally {
            if (jpeg_data != null) {
                try {
                    jpeg_data.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            output = null;
            code = null;
            bitmap = null;
        }
        return "";
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 300) {    //循环判断如果压缩后图片是否大于300kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 15;//每次都减少15
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
    }

    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        if (bitmap != null) {
            return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
        } else {
            return null;
        }
    }

}
