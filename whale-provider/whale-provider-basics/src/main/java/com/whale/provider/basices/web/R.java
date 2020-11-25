package com.whale.provider.basices.web;



import lombok.Data;

import java.io.Serializable;

/**
 * @author sy
 * @Date: 2020/3/27 11:26
 * @Description 返回对象
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = -1949931234174289546L;


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String msg;


    private boolean status;

    /**
     * 返回对象
     */
    private T data;



    public R() {
        super();
    }

    public R(Integer code) {
        super();
        this.code = code;
    }

    public R(Integer code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public R(Integer code, Throwable throwable) {
        super();
        this.code = code;
        this.msg = throwable.getMessage();
    }

    public R(Integer code, T data) {
        super();
        this.code = code;
        this.data = data;
    }

    public R(Integer code, String msg, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public R(Integer code, String msg, T data, boolean status) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.status=status;
    }



    public R(Integer code, boolean status, String msg, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.status=status;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        R<?> other = (R<?>) obj;
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        if (msg == null) {
            if (other.msg != null) {
                return false;
            }
        } else if (!msg.equals(other.msg)) {
            return false;
        }
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        return true;
    }


    public  static <T> R fail(Integer code, String msg){
        return new R<T>(code,false,msg ,null);
    }



    public  static <T> R fail(){
        return new R<T>(CodeStatus.SYS,false,"处理错误",null);
    }


    public static <T> R<T> fail(String msg){
        return new R<T>(CodeStatus.SYS,false,msg,null);
    }


    public static <T> R<T> ok(){
        return new R<T>(CodeStatus.OK,true,"处理成功",null);
    }

    public static <T> R<T> ok(String msg){
        return new R<T>(CodeStatus.OK,true,msg,null);
    }

    public static <T> R<T> ok(T data){
        return new R<T>(CodeStatus.OK,true,"处理成功",data);
    }



    /**
     * 通用状态码
     * <p>
     * Description:
     * </p>
     *
     * @author Lusifer
     * @version v1.0.0
     * @date 2019-07-30 05:02:49
     */
    public static class CodeStatus {


        /**
         * 请求成功
         */
        public static final int OK = 0;

        /**
         * 请求失败
         */
        public static final int SYS = -1;

        /**
         * 请求错误
         */
        public static final int REQUEST = 2000;
        //参数校验失败
        public static final int REQUEST_PARAM=2001;


        /**
         * 权限不足
         */
        public static final int POWER = 3000;



        public static final int LOG_ERR=3001;



        /**
         * 数据异常
         */
        public static final int DATA = 4000;

        /**
         * 未知异常
         */
        public static final int ANOMALY = 5000;

        /**
         * 网络异常
         */
        public static final int NET = 6000;


        /**
         * 令牌过期
         */
        public static final int EXPIRED = 7000;

        /**
         * 第三方错误
         */
        public static final int EXTERN = 8000;


        public static final int FEIGNFALL=9001;
    }



}
