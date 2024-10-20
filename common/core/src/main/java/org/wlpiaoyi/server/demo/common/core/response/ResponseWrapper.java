package org.wlpiaoyi.server.demo.common.core.response;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.wlpiaoyi.framework.utils.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/2/18 10:39
 * {@code @version:}:       1.0
 */
public class ResponseWrapper extends HttpServletResponseWrapper {


    private final ByteArrayOutputStream bufferStream;
    private final ServletOutputStream outputStream;
    private final PrintWriter writer;

    /**
     *
     * @param response
     * @return:
     * @author: wlpia
     * @date: 2023/12/25 14:57
     */
    public ResponseWrapper(@NonNull HttpServletResponse response) throws IOException {
        super(response);
        this.bufferStream = new ByteArrayOutputStream();
        this.outputStream = new WrappedOutputStream(bufferStream);
        this.writer = new PrintWriter(new OutputStreamWriter(
                bufferStream, this.getCharacterEncoding()
        ));
    }


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
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

    }

    /**
     * The default behavior of this method is to call sendError(int sc, String msg) on the wrapped response object.
     */
    @Override
    public void sendError(int sc, String msg) throws IOException {
        this.setStatus(sc);
        this.outputStream.write(GsonBuilder.gsonDefault().toJson(R.data(sc, msg)).getBytes());
    }

    /**
     * The default behavior of this method is to call sendError(int sc) on the wrapped response object.
     */
    @Override
    public void sendError(int sc) throws IOException {
        this.setStatus(sc);
        this.outputStream.write(GsonBuilder.gsonDefault().toJson(R.data(sc)).getBytes());
    }



}
