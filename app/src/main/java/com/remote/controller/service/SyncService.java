package com.remote.controller.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.remote.controller.constant.Constant;
import com.remote.controller.network.ControllerManager;
import com.remote.controller.network.EventGenerator;

import java.util.Timer;
import java.util.TimerTask;

public class SyncService extends Service {

    private Context context;
    private Timer timer;

    public SyncService() {
        context = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer();
        timer.schedule(new SyncWork(), 0, Constant.SYNC_SEQ_TIME);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 后台定时任务
     */
    class SyncWork extends TimerTask {

        @Override
        public void run() {



            //读取设备运行状态
            ControllerManager.getInstance(context).sendData(EventGenerator.getInstance().generateData(Constant.EventCode.READ_RUNNING_STATE, null));

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            //示教界面读数据， 返回四个32位数据
            ControllerManager.getInstance(context).sendData(EventGenerator.getInstance().generateData(Constant.EventCode.READ_DATA_ON_SETTING, null));

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }


                //运行界面读数据， 返回四个32位数据
            ControllerManager.getInstance(context).sendData(EventGenerator.getInstance().generateData(Constant.EventCode.READ_DATA_ON_PLAY, null));


            //运行界面读运行次数
            ControllerManager.getInstance(context).sendData(EventGenerator.getInstance().generateData(Constant.EventCode.READ_PLAY_TIMES, null));

        }
    }
}
