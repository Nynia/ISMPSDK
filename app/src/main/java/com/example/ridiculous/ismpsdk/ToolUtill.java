package com.example.ridiculous.ismpsdk;
import java.text.SimpleDateFormat;

/**
 * Created by Ridiculous on 2016/5/30.
 */
public class ToolUtill {
    public static String getTimestamp() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String atTime = sdf.format(new java.util.Date(System.currentTimeMillis()));
        return atTime;
    }
}
