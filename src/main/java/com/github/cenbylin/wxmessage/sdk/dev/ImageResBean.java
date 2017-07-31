package com.github.cenbylin.wxmessage.sdk.dev;

import java.io.InputStream;

/**
 * Created by Cenbylin on 2017/7/31.
 */
public class ImageResBean {
    private InputStream in;
    private String format;

    public ImageResBean(InputStream in, String format) {
        this.in = in;
        this.format = format;
    }

    public InputStream getIn() {
        return in;
    }

    public String getFormat() {
        return format;
    }
}
