package org.wlpiaoyi.server.demo.utils.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-09 14:56:18</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
public class WebUtilsTest {

    @Test
    public void patternUri(){
        Assert.assertTrue(WebUtils.patternUri("/sys/", new String[]{"(/sys/.*|/test/.*)","(^(?!/test/test2).*$)"}));
        Assert.assertTrue(WebUtils.patternUri("/sys/abc", new String[]{"(/sys/.*|/test/.*)","(^(?!/test/test2).*$)"}));
        Assert.assertTrue(WebUtils.patternUri("/test/abc?a=1", new String[]{"(/sys/.*|/test/.*)","(^(?!/test/test2).*$)"}));
        Assert.assertTrue(WebUtils.patternUri("/test/lakl", new String[]{"(/sys/.*|/test/.*)","(^(?!/test/test2).*$)"}));
        Assert.assertFalse(WebUtils.patternUri("/test/test2", new String[]{"(/sys/.*|/test/.*)","(^(?!/test/test2).*$)"}));
        Assert.assertFalse(WebUtils.patternUri("/test/test2/lajdl", new String[]{"(/sys/.*|/test/.*)","(^(?!/test/test2).*$)"}));
    }

}
