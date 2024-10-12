package org.wlpiaoyi.server.demo.utils.web;

import org.wlpiaoyi.framework.utils.ValueUtils;

import java.util.regex.Pattern;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-08 17:38:55</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
public class WebUtils {

    public static final String HEADER_TOKEN_KEY = "token";
    public static final String ENCRYPT_CONTENT_TYPE_HEAD_TAG = "#ENCRYPT#";


    /**
     * <p><b>{@code @description:}</b>
     * TODO
     * </p>
     *
     * <p><b>@param</b> <b>uri</b>
     * {@link String}
     * </p>
     *
     * <p><b>@param</b> <b>regexes</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/9 9:47</p>
     * <p><b>{@code @return:}</b>{@link boolean}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static boolean patternUri(final String uri, final String[] regexes){
        if(ValueUtils.isBlank(regexes)){
            return false;
        }
        for (String regex : regexes) {
            if(!Pattern.matches(regex, uri)){
                return false;
            }
        }
        return true;
    }
}
