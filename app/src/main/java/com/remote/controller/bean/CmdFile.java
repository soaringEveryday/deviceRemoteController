package com.remote.controller.bean;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chen Haitao on 2015/11/17.
 */
public class CmdFile extends SugarRecord implements Serializable {

    /**
     * 版本
     */
    private int version;

    /**
     * 文件描述
     */
    private String description;

    /**
     * 列名
     */
    private String[] columnTitles;

    /**
     * 指令列表
     */
    private List<FileLineItem> commands;


    /**
     * 文件名
     */
    private String fileName;


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getColumnTitles() {
        return columnTitles;
    }

    public void setColumnTitles(String[] columnTitles) {
        this.columnTitles = columnTitles;
    }

    public List<FileLineItem> getCommands() {
        return commands;
    }

    public void setCommands(List<FileLineItem> commands) {
        this.commands = commands;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "CmdFile [version=" + version + ", description=" + description + ", commands=" + commands + "]";
    }
}
