package com.remote.controller.utils;

import android.content.Context;
import android.os.Environment;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.remote.controller.bean.FileLineItem;
import com.remote.controller.constant.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Chen Haitao on 2015/11/17.
 */
public class CSVUtils {

    private CSVUtils() {

    }

    private static CSVUtils instance = null;

    public static synchronized CSVUtils getInstance() {
        if (instance == null) {
            instance = new CSVUtils();
        }
        return instance;
    }

    public void create(String fileName, int version, String description, ArrayList<String> columns, ArrayList<FileLineItem> lines) throws IOException {

        if (fileName == null || fileName.isEmpty()) {
            return;
        }

        File extDir = Environment.getExternalStorageDirectory();
        File fullFilename;
        if (version == Constant.FileFormat.VERION_CSV) {
            fullFilename = new File(extDir, fileName + ".csv");
        } else {
            fullFilename = new File(extDir, fileName);
        }

        if (fullFilename.exists()) {
            fullFilename.delete();
        }

        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fullFilename), Charset.forName("GBK"));

//        CSVWriter writer = new CSVWriter(new FileWriter(fullFilename), ',');
        CSVWriter writer = new CSVWriter(out, ',');
        String[] entries;
        entries = new String[]{"版本", String.valueOf(version)};
        writer.writeNext(entries);//写入版本信息，默认1为csv格式

        entries = new String[]{"文件描述", description};
        writer.writeNext(entries);//写入文件描述

        entries = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            entries[i] = columns.get(i);
        }
        writer.writeNext(entries);//写入栏信息


        String[] temp = new String[columns.size()];
        int index = 1;
        for (FileLineItem item : lines) {
            temp[0] = String.valueOf(index++);
            temp[1] = item.getCommand();
            temp[2] = item.getParameter();
            temp[3] = item.getMemo();
            writer.writeNext(temp);//写入所有行的指令
        }


        out.close();
        writer.close();

    }

    public void save(String path, int version, String description, ArrayList<String> columns, ArrayList<FileLineItem> lines) throws IOException {

        if (path == null || path.isEmpty()) {
            return;
        }

        L.i("save path : " + path);

        File fullFilename = new File(path);

        if (fullFilename.exists()) {
            fullFilename.delete();
        }

        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fullFilename), Charset.forName("GBK"));

//        CSVWriter writer = new CSVWriter(new FileWriter(fullFilename), ',');
        CSVWriter writer = new CSVWriter(out, ',');
        String[] entries;
        entries = new String[]{"版本", String.valueOf(version)};
        writer.writeNext(entries);//写入版本信息，默认1为csv格式

        entries = new String[]{"文件描述", description};
        writer.writeNext(entries);//写入文件描述

        entries = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            entries[i] = columns.get(i);
        }
        writer.writeNext(entries);//写入栏信息

        int index = 1;
        String[] temp = new String[columns.size()];
        for (FileLineItem item : lines) {
            temp[0] = String.valueOf(index++);
            temp[1] = item.getCommand();
            temp[2] = item.getParameter();
            temp[3] = item.getMemo();
            writer.writeNext(temp);//写入所有行的指令
        }

        out.close();
        writer.close();
    }

    /**
     * @param path
     * @return
     */
    public ArrayList<FileLineItem> read(Context context, String path) {
        InputStreamReader in;
        try {
            in = new InputStreamReader(new FileInputStream(path), Charset.forName("GBK"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            L.e("找不到文件");
            return null;
        }

        CSVReader reader = new CSVReader(in, ',', '"', 1);

        ArrayList<FileLineItem> commands = new ArrayList<>();
        FileLineItem command;

        String[] nextLine;
        String fileDesc;

        try {
            if ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                L.d(nextLine[0] + ";" + nextLine[1]);
                fileDesc = nextLine[1];
                SPUtils.put(context, Constant.SPKEY.FILE_DESC, fileDesc);
                File tempFile = new File(path);
                if (tempFile != null) {
                    String fullName = tempFile.getName();
                    SPUtils.put(context, Constant.SPKEY.FILE_NAME, StrUtils.getFileNameNoEx(fullName));
                    SPUtils.put(context, Constant.SPKEY.FILE_PATH, path);
                } else {
                    L.e("create temp file fail");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                L.d(nextLine[0] + ";" + nextLine[1] + ";" + nextLine[2] + ";" + nextLine[3]);
                command = new FileLineItem();
                command.setNo(Integer.parseInt(nextLine[0]));
                command.setCommand(nextLine[1]);
                command.setParameter(nextLine[2]);
                command.setMemo(nextLine[3]);
                commands.add(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            in.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return commands;
    }

    public byte[] readBytes(Context context, String path) {

        return null;
    }

    public void close() {

    }

    public void save() {

    }

    public void saveAs(String fileName) {

    }
}
