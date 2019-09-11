package com.github.grpcx.listener;

import com.github.grpcx.GrpcServer;
import org.springframework.context.SmartLifecycle;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class SmartLifecycleListener implements SmartLifecycle {

    private boolean isRunningFlag() {
        return runningFlag;
    }

    private void setRunningFlag(boolean runningFlag) {
        this.runningFlag = runningFlag;
    }

    private boolean runningFlag = false;

    @Override
    public void stop(Runnable callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("do stop with callback param");
                //设置为false，表示已经不在执行中了
                setRunningFlag(false);
                //callback中有个CountDownLatch实例，总数是SmartLifecycle对象的数量，
                //此方法被回调时CountDownLatch实例才会减一，初始化容器的线程一直在wait中；
                callback.run();
            }
        }).start();

    }

    @Override
    public void start() {
        System.out.println("do start");

        try {
            GrpcServer.strart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        //设置为false，表示正在执行中
        setRunningFlag(true);
    }

    @Override
    public void stop() {
        System.out.println("do stop");
        //设置为false，表示已经不在执行中了
        setRunningFlag(false);
    }

    @Override
    public int getPhase() {
        return 666;
    }

    @Override
    public boolean isRunning() {
        return isRunningFlag();
    }

    @Override
    public boolean isAutoStartup() {
        //只有设置为true，start方法才会被回调
        return true;
    }
}
