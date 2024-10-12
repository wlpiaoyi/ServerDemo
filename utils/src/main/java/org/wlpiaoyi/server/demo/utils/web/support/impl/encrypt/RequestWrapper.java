package org.wlpiaoyi.server.demo.utils.web.support.impl.encrypt;


import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.HttpHeaders;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;

import javax.management.ValueExp;
import java.io.*;
import java.util.*;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/2/18 12:07
 * {@code @version:}:       1.0
 */
class RequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    private Map<String, String> headers;

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }


    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.headers = HashMap.newHashMap(4);
        Enumeration<String> allEn = request.getHeaderNames();
        while (allEn.hasMoreElements()){
            String name = allEn.nextElement();
            name = name.toUpperCase(Locale.ROOT);
            if(name.equals(HttpHeaders.CONTENT_TYPE.toUpperCase())
                    || name.equals(HttpHeaders.ACCEPT.toUpperCase())){
                String value = request.getHeader(name);
                if(value.startsWith(WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG)){
                    value = value.substring(WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG.length());
                }
                headers.put(name, value);
            }
        }
        this.body = this.toByteArray(request.getInputStream());
    }

    @Override
    public String getHeader(String name) {
        name = name.toUpperCase(Locale.ROOT);
        String value = super.getHeader(name);
        if (this.headers.containsKey(name)) {
            value = this.headers.get(name);
        }
        return value;
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        name = name.toUpperCase(Locale.ROOT);
        List<String> values = Collections.list(super.getHeaders(name));
        if (this.headers.containsKey(name)) {
            values = Arrays.asList(this.headers.get(name));
        }
        return Collections.enumeration(values);
    }

    private byte[] toByteArray(ServletInputStream inputStream) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n;
        while((n= inputStream.read(buffer)) != -1){
            byteStream.write(buffer, 0, n);
        }
        return byteStream.toByteArray();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteStream.read();
            }
        };
    }
}
