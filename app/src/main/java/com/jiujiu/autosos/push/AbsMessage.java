package com.jiujiu.autosos.push;

/**
 * 推送消息的抽象类
 * @author yangweiquan
 * @date 2017年08月10日
 */
public abstract class AbsMessage implements Cloneable {

    /**
     * 接收消息的userId
     */
    private long userId;
    /**
     * 推送消息的内容
     */
    private String content;

    /**
     * 推送消息的业务类型
     * @return
     */
    public abstract String bizType();

    /**
     * 推送消息的操作类型
     * @return
     */
    public abstract String operation();

    /**
     * 推送消息的标题
     * @return
     */
    public abstract String title();

    /**
     * 点击通知跳转的页面
     * 不能为空，如果想什么页面都不跳转，可以传TO_INDEX
     * @return
     */
    public abstract String pageTo();

    /**
     * 是否忽略操作
     * @return
     */
    public boolean ignoreOperation() {
        return false;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public AbsMessage clone() {
        AbsMessage message = null;
        try {
            message = (AbsMessage) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }
}
