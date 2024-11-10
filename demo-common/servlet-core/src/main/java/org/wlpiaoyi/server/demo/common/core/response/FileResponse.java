package org.wlpiaoyi.server.demo.common.core.response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpHeaders;
import org.wlpiaoyi.framework.utils.MapUtils;
import org.wlpiaoyi.framework.utils.ValueUtils;
import org.wlpiaoyi.framework.utils.exception.BusinessException;
import org.wlpiaoyi.framework.utils.exception.SystemException;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p><b>{@code @description:}</b>  </p>
 * <p><b>{@code @date:}</b>         2024-03-18 10:14:39</p>
 * <p><b>{@code @author:}</b>       wlpiaoyi</p>
 * <p><b>{@code @version:}</b>      1.0</p>
 */
@Getter
@Slf4j
public class FileResponse {

    private static final int BUFFER_SIZE = 1024;
    private final Map<String, String> contentTypeMap;

    private FileResponse(Map<String, String> contentTypeMap){
        this.contentTypeMap = contentTypeMap;
    }

    public static FileResponse getInstance(Map<String, String> contentTypeMap){
        return new FileResponse(contentTypeMap);
    }

    public static FileResponse getDefaultInstance(){
        return new FileResponse(new HashMap(){{
            put("jpg",      "image/jpeg");
            put("jpeg",     "image/jpeg");
            put("png",      "image/png");
            put("gif",      "image/gif");

            put("pdf",      "application/pdf");
            put("docx",     "application/msword");
            put("xlsx",     "application/vnd.ms-excel");

            put("mp4",      "video/mp4");
            put("mp3",      "audio/mp3");

            put("default",  "application/octet-stream");
        }});
    }


    /**
     * <p><b>{@code @description:}</b>
     * 分片下载任务处理
     * </p>
     *
     * <p><b>{@code @param}</b> <b>dataInput</b>
     * {@link BufferedInputStream}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>funcMap</b>
     * {@link Map}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>request</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>response</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/3/13 13:19</p>
     * <p><b>{@code @return:}</b>{@link boolean}</p>
     * <p><b>{@code @author:}</b>wlpia</p>
     */
    private boolean handlePartDownload(BufferedInputStream dataInput, Map funcMap, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String range = request.getHeader(HttpHeaders.RANGE);
        if (ValueUtils.isNotBlank(range) && !"null".equals(range)) {
            long point = 0L;
            // tell the client to allow accept-ranges
            response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            int writerType = MapUtils.getInteger(funcMap, "writerType");
            long contentLength = MapUtils.getLong(funcMap, "contentLength");
            long fileLength = MapUtils.getLong(funcMap, "fileLength");
            String readType = MapUtils.getString(funcMap, "readType");
            String rangBytes = range.replaceFirst("bytes=", "");
            String contentRange;
            if (rangBytes.endsWith("-")) { // bytes=270000-
                writerType = 1;
                point = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
                /* 客户端请求的是270000之后的字节（包括bytes下标索引为270000的字节） */
                contentLength = fileLength - point;
                /*
                 断点开始
                 响应的格式
                 Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
                 */
                contentRange = "bytes " + Long.toString(point) + "-" +
                        Long.toString(fileLength - 1) + "/" +
                        Long.toString(fileLength);
            } else { // bytes=270000-320000
                writerType = 2;
                long startIndex = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
                long endIndex = Long.parseLong(rangBytes.substring(rangBytes.indexOf("-") + 1));
                point = startIndex;
                /* 客户端请求的是 270000-320000 之间的字节 */
                contentLength = endIndex - startIndex;
                contentLength ++;
                /*
                 断点开始
                 响应的格式
                 Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
                 */
                contentRange = range.replace("=", " ") + "/" + Long.toString(fileLength);
            }
            response.setHeader(HttpHeaders.CONTENT_RANGE, contentRange);
            dataInput.skip(point);

            funcMap.put("writerType", writerType);
            funcMap.put("contentLength", contentLength);
            funcMap.put("fileLength", fileLength);
            funcMap.put("readType", readType);
            return true;
        }
        return false;
    }

    private boolean flushIO(Flushable flushIO){
        if(flushIO == null){
            throw new BusinessException("Flush failed, outputStream is null");
        }
        try {
            flushIO.flush();
        } catch (IOException e) {
            log.warn("Flush alert failed:{}", e.getMessage());
            return false;
        }
        return true;
    }

    private boolean closeOutputStream(OutputStream outputStream){
        if(outputStream == null){
            throw new BusinessException("Close output failed, outputStream is null");
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            log.warn("Close output failed:{}", e.getMessage());
            return false;
        }
        return true;
    }

    private boolean closeInputStream(InputStream inputStream){
        if(inputStream == null){
            throw new BusinessException("Close input failed, inputStream is null");
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            log.warn("Close input failed:{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * <p><b>{@code @description:}</b>
     * 根据文件对象下载数据
     * </p>
     *
     * <p><b>{@code @param}</b> <b>file</b>
     * {@link File}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>funcMap</b>
     * {@link Map}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>request</b>
     * {@link HttpServletRequest}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>response</b>
     * {@link HttpServletResponse}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/3/13 13:20</p>
     * <p><b>{@code @author:}</b>wlpia</p>
     */
    public void download(File file, Map funcMap, HttpServletRequest request, HttpServletResponse response) throws SystemException {
        List<Closeable> closeables = new ArrayList<>();
        List<Flushable> flushables = new ArrayList<>();
        try{
            String contentType = MapUtils.getString(funcMap, "contentType");
            String fileName = MapUtils.getString(funcMap, "fileName");
            if(ValueUtils.isBlank(contentType)){
                contentType = contentTypeMap.get("default");
            }
            OutputStream dataOutput = response.getOutputStream();
            closeables.add(dataOutput);
            flushables.add(dataOutput);
            response.reset();
            response.setContentType(contentType);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            String readType = MapUtils.getString(funcMap, "readType", "inline");

            final long fileLength = file.length();

            /*
             0: 下载全部数据
             1: 分片下载(start-)
             2: 分片下载(start-end)
             */
            int writerType = 0;
            long contentLength = 0L;

            BufferedInputStream dataInput = new BufferedInputStream(Files.newInputStream(file.toPath()));
            closeables.add(dataInput);

            funcMap.put("writerType", writerType);
            funcMap.put("contentLength", contentLength);
            funcMap.put("fileLength", fileLength);
            funcMap.put("readType", readType);
            if(handlePartDownload(dataInput, funcMap, request, response)){
                writerType = MapUtils.getInteger(funcMap, "writerType");
                contentLength = MapUtils.getLong(funcMap, "contentLength");
                readType = MapUtils.getString(funcMap, "readType");
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            }else{
                response.setStatus(HttpServletResponse.SC_OK);
                contentLength = fileLength;
            }

            /*
             如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
             Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
             */
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
            response.setContentType(contentType);
            //设置文件长度
            response.setHeader("Content-disposition", readType + ";filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()));
            // response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileInfo.getName(), Charsets.UTF_8.name()));

            long readLength = 0;
            byte[] bytes = new byte[BUFFER_SIZE];
            if (writerType == 2) {
                int l;
                long clb = contentLength - BUFFER_SIZE;
                while (readLength <= clb) {
                    l = dataInput.read(bytes);
                    readLength += l;
                    dataOutput.write(bytes, 0, l);
                    if(!flushIO(dataOutput)){
                        throw new BusinessException("flush error");
                    }
                }
                clb = contentLength - readLength;
                if (clb > 0) {
                    l = dataInput.read(bytes, 0, (int) clb);
                    dataOutput.write(bytes, 0, l);
                    if(!flushIO(dataOutput)){
                        throw new BusinessException("flush error");
                    }
                }
            } else {
                int l;
                while ((l = dataInput.read(bytes)) != -1) {
                    dataOutput.write(bytes, 0, l);
                    if(!flushIO(dataOutput)){
                        throw new BusinessException("flush error");
                    }
                }
            }
        }catch (Exception e){
            if(e instanceof BusinessException){
                throw new BusinessException("biz error for localPath:" + file.getAbsolutePath(), e);
            }
            if(e instanceof SystemException){
                throw new SystemException("biz error for localPath:" + file.getAbsolutePath(), e);
            }
            if (e instanceof ClientAbortException){
                log.warn("write data error for localPath:{} message:{}", file.getAbsoluteFile(), e.toString());
                return;
            }
            throw new SystemException("file read error for localPath:" + file.getAbsolutePath(), e);
        }finally {
            if(ValueUtils.isNotBlank(flushables)){
                for (Flushable flushable : flushables){
                    try { flushable.flush(); } catch (IOException e) {
                        log.warn("download flush obj failed message:{}", e.toString());
                    }
                }
                flushables.clear();
            }
            if(ValueUtils.isNotBlank(closeables)){
                for (Closeable closeable : closeables){
                    try { closeable.close(); } catch (IOException e) {
                        log.warn("download close obj failed message:{}", e.toString());
                    }
                }
                closeables.clear();
            }
        }
    }

}
