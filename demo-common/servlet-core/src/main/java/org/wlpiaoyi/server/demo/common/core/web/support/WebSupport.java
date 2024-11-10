package org.wlpiaoyi.server.demo.common.core.web.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wlpiaoyi.server.demo.common.tools.web.domain.DoFilterEnum;

import java.util.Map;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-09 09:32:28</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
public interface WebSupport<REQ,RES> {


    /**
     * <p><b>{@code @description:}</b>
     * 获取请求的URI
     * </p>
     *
     * <p><b>{@code @param}</b> <b>servletRequest</b>
     * {@link REQ}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/9 10:07</p>
     * <p><b>{@code @return:}</b>{@link String}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    String getRequestURI(REQ servletRequest);

    /**
     * <p><b>{@code @description:}</b>
     * <br/><b>For example:</b> <br/>
     * WebUtils.patternUri("/sys/", new String[]{"(/sys/.*|/test/.*)","(^(?!/test/test2).*$)"})
     * </p>
     *
     * <p><b>{@code @param}</b> <b></b>
     * {@link }
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/9 10:07</p>
     * <p><b>{@code @return:}</b>{@link String[]}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    String[] getURIRegexes();

    /**
     * <p><b>{@code @description:}</b>
     * 根据之前按的执行结果确定是否执行当前对象
     * </p>
     *
     * <p><b>{@code @param}</b> <b>res</b>
     * {@link int}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/10 15:08</p>
     * <p><b>{@code @return:}</b>{@link boolean}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    default boolean shouldInDo(int res){
        if((res & DoFilterEnum.GoNext.getValue()) < 1){
            return false;
        }
        return true;
    }

    /**
     * <p><b>{@code @description:}</b>
     *
     * </p>
     *
     * <p><b>{@code @param}</b> <b>request</b>
     * {@link REQ}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>response</b>
     * {@link RES}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/9 10:24</p>
     * <p><b>{@code @return:}</b>{@link int}
     * <br/>
     * <b>0b1</b>  : 是否继续下一个Support,
     * <br/>
     * <b>0b10</b> : 是否关闭 Request 连接,
     * <br/>
     * <b>0b100</b>: 是否关闭 Response 连接,
     * </p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    int doFilter(REQ request, RES response, Map obj) throws Exception;


    /**
     * <p><b>{@code @description:}</b> 
     * TODO
     * </p>
     * 
     * <p><b>{@code @param}</b> <b>request</b>
     * {@link REQ}
     * </p>
     * 
     * <p><b>{@code @param}</b> <b>response</b>
     * {@link RES}
     * </p>
     * 
     * <p><b>{@code @param}</b> <b>obj</b>
     * {@link Map}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/12 14:39</p>
     * <p><b>{@code @return:}</b>{@link int}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    default int isSupportExecResponse(REQ request, RES response, Map obj){return -1;}

    default boolean canExecResponse(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }

    /**
     * <p><b>{@code @description:}</b>
     * TODO
     * </p>
     *
     * <p><b>{@code @param}</b> <b>request</b>
     * {@link REQ}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>response</b>
     * {@link RES}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>obj</b>
     * {@link Map}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>indexSupport</b>
     * {@link int}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>totalSupports</b>
     * {@link int}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/12 14:36</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    default void execResponse(REQ request, RES response, Map obj, int indexSupport, int totalSupports){}

}
