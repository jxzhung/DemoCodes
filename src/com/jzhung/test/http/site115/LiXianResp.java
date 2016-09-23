package com.jzhung.test.http.site115;

/**
 * Created by Jzhung on 2016/9/21.
 */
public class LiXianResp {

    /**
     * state : false
     * error_msg : 本月剩余配额为0，升级VIP可获得1500个配额
     * errno : 101
     * result : null
     * errcode : 10010
     */

    private boolean state;
    private String error_msg;
    private int errno;
    private Object result;
    private int errcode;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }
}
