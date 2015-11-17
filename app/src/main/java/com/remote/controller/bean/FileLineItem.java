package com.remote.controller.bean;

import java.io.Serializable;

/**
 * csv文件中的一行
 *
 * Created by Chen Haitao on 2015/11/14.
 */
public class FileLineItem implements Serializable {

    /**
     * 序号
     */
    private int no;

    /**
     * 指令
     */
    private String command;

    /**
     * 参数
     */
    private String parameter;

    /**
     * 备注
     */
    private String memo;

    public FileLineItem(int no, String command, String parameter, String memo) {
        this.no = no;
        this.command = command;
        this.parameter = parameter;
        this.memo = memo;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    @Override
    public String toString() {
        return "FileLineItem [no=" + no + ", command=" + command + ", parameter=" + parameter + ", memo=" + memo + "]";
    }
}
