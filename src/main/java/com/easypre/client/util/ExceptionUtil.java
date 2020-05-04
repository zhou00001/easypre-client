package com.easypre.client.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常处理工具
 *
 * @author zhoudc
 * @version 1.0
 * @since 1.0
 */
public class ExceptionUtil {
    /**
     * 获取异常信息
     * @param e
     * @return
     */
    public static String getExceptionMsg(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return e.getMessage()+"|"+sw.toString();
    }
}
