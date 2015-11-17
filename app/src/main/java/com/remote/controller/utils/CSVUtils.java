package com.remote.controller.utils;

import com.opencsv.CSVWriter;
import com.opencsv.bean.BeanToCsv;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.remote.controller.bean.FileLineItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public void create() throws IOException {
        File tempFile = File.createTempFile("csvWriterTest", ".csv");
        tempFile.deleteOnExit();
        CSVWriter writer = new CSVWriter(new FileWriter(tempFile));

        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(FileLineItem.class);
        String[] columns = new String[] {"no", "command", "parameter", "memo"};
        strat.setColumnMapping(columns);

        List<FileLineItem> datas = new ArrayList<>();
        datas.add(new FileLineItem(1, "LineTo", "100", ""));
        datas.add(new FileLineItem(1, "MoveTo", "1100", ""));


        BeanToCsv<FileLineItem> btc = new BeanToCsv<>();
        btc.write(strat, writer, datas);
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
