package org.wlpiaoyi.server.demo.utils.web.support.impl.encrypt;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.server.demo.utils.web.WebUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/2/18 10:39
 * {@code @version:}:       1.0
 */
class ResponseWrapper extends HttpServletResponseWrapper {


    private final ByteArrayOutputStream bufferStream;
    private final ServletOutputStream outputStream;
    private final PrintWriter writer;

//    private Map<String, String> headers;
    /**
     *
     * @param response
     * @return:
     * @author: wlpia
     * @date: 2023/12/25 14:57
     */
    public ResponseWrapper(@NonNull HttpServletResponse response) throws IOException {
        super(response);
        Collection<String> allEn = response.getHeaderNames();
        this.bufferStream = new ByteArrayOutputStream();
        this.outputStream = new WrappedOutputStream(bufferStream);
        this.writer = new PrintWriter(new OutputStreamWriter(
                bufferStream, this.getCharacterEncoding()
        ));
    }

//    @Override
//    public Collection<String> getHeaders(String name) {
//        return super.getHeaders(name);
//    }
//
//    @Override
//    public String getHeader(String name) {
//        String value = super.getHeader(name);
//        if(ValueUtils.isBlank(value)){
//            return value;
//        }
//        if(name.toUpperCase(Locale.ROOT).equals(HttpHeaders.CONTENT_TYPE.toUpperCase())
//                || name.toUpperCase(Locale.ROOT).equals(HttpHeaders.ACCEPT.toUpperCase())){
//            if(value.startsWith(WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG)){
//                value = value.substring(WebUtils.ENCRYPT_CONTENT_TYPE_HEAD_TAG.length());
//            }
//        }
//        return value;
//    }
//
//    @Override
//    public Collection<String> getHeaderNames() {
//        return super.getHeaderNames();
//    }

    /** 重载父类获取outputstream的方法 */
    @Override
    public ServletOutputStream getOutputStream() {
        return this.outputStream;
    }

    /** 重载父类获取writer的方法 */
    @Override
    public PrintWriter getWriter() {
        return this.writer;
    }

    /** 重载父类获取flushBuffer的方法 */
    @Override
    public void flushBuffer() throws IOException {
        if (this.outputStream != null) {
            this.outputStream.flush();
        }
        if (this.writer != null) {
            this.writer.flush();
        }
    }

    @Override
    public void reset() {
        this.bufferStream.reset();
    }

    /** 将out、writer中的数据强制输出到WapperedResponse的buffer里面，否则取不到数据 */
    public byte[] getResponseData() throws IOException {
        this.flushBuffer();
        return this.bufferStream.toByteArray();
    }
    public void writeBuffer(byte[] buffer) throws IOException {
        this.bufferStream.write(buffer); ;
    }

    /**
     * 内部类，对ServletOutputStream进行包装
     */
    private static class WrappedOutputStream extends ServletOutputStream {
        private final ByteArrayOutputStream bos;

        public WrappedOutputStream(@NonNull ByteArrayOutputStream stream) {
            this.bos = stream;
        }

        @Override
        public void write(int b) {
            this.bos.write(b);
        }

        @Override
        public void write(@NonNull byte @NotNull [] b) {
            this.bos.write(b, 0, b.length);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }

}
