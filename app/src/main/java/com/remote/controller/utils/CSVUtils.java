package com.remote.controller.utils;

import android.os.Environment;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Chen Haitao on 2015/11/17.
 */
public class CSVUtils{

    private CSVUtils() {

    }

    private static CSVUtils instance = null;

    public static synchronized CSVUtils getInstance() {
        if (instance == null) {
            instance = new CSVUtils();
        }
        return instance;
    }

    public void create(String fileName, String version, String description) throws IOException {

        if (fileName == null || fileName.isEmpty()) {
            return;
        }

        File extDir = Environment.getExternalStorageDirectory();
        File fullFilename = new File(extDir, fileName);

        try {
            fullFilename.createNewFile();
            fullFilename.setWritable(Boolean.TRUE);
        } catch (IOException e) {
            e.printStackTrace();
        }


        CSVWriter writer = new CSVWriter(new FileWriter(fullFilename), ',');
        String[] entries = new String[]{"版本", version};
        writer.writeNext(entries);
        entries = new String[]{"文件描述", description};
        writer.writeNext(entries);
        writer.close();

//        File tempFile = File.createTempFile("csvWriterTest", ".csv");
//        tempFile.deleteOnExit();
//        CSVWriter writer = new CSVWriter(new FileWriter(tempFile));
//
//        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
//        strat.setType(FileLineItem.class);
//        String[] columns = new String[] {"no", "command", "parameter", "memo"};
//        strat.setColumnMapping(columns);
//
//        List<FileLineItem> datas = new ArrayList<>();
//        datas.add(new FileLineItem(1, "LineTo", "100", ""));
//        datas.add(new FileLineItem(1, "MoveTo", "1100", ""));
//
//
//        BeanToCsv<FileLineItem> btc = new BeanToCsv<>();
//        btc.write(strat, writer, datas);
    }

    public void read() {

    }

    public void close() {

    }

    public void save() {

    }

    public void saveAs(String fileName) {

    }
}
