package org.wlpiaoyi.server.demo.utils.request;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.lang.Nullable;
import org.wlpiaoyi.framework.utils.ValueUtils;

/**
 * {@code @author:}         wlpiaoyi
 * {@code @description:}    TODO
 * {@code @date:}           2023/9/16 12:41
 * {@code @version:}:       1.0
 */
public class Condition {

    @Nullable
    private static String cleanIdentifier(@Nullable String param) {
        if (param == null) {
            return null;
        } else {
            StringBuilder paramBuilder = new StringBuilder();

            for(int i = 0; i < param.length(); ++i) {
                char c = param.charAt(i);
                if (Character.isJavaIdentifierPart(c)) {
                    paramBuilder.append(c);
                }
            }

            return paramBuilder.toString();
        }
    }

    public static <T> IPage<T> getPage(Query query) {
        Page<T> page = new Page(ValueUtils.toLong(query.getCurrent(), 1L), ValueUtils.toLong(query.getSize(), 10L));
        String[] ascArr = ValueUtils.toStringArray(query.getAscs());
        String[] descArr = ascArr;
        int var4 = ascArr.length;

        int var5;
        for(var5 = 0; var5 < var4; ++var5) {
            String asc = descArr[var5];
            page.addOrder(OrderItem.asc(cleanIdentifier(asc)));
        }

        descArr = ValueUtils.toStringArray(query.getDescs());
        String[] var8 = descArr;
        var5 = descArr.length;

        for(int var9 = 0; var9 < var5; ++var9) {
            String desc = var8[var9];
            page.addOrder(OrderItem.desc(cleanIdentifier(desc)));
        }

        return page;
    }
    public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
        return new QueryWrapper(entity);
    }

}
