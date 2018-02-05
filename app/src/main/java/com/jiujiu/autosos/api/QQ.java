package com.jiujiu.autosos.api;

/**
 * Created by Administrator on 2018/2/2.
 */

public class QQ {
    private static final QQ ourInstance = new QQ();

    public static QQ getInstance() {
        return ourInstance;
    }

    private QQ() {
    }
}
