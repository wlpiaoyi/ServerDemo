package org.wlpiaoyi.server.demo.common.tools.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.wlpiaoyi.framework.utils.ValueUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p><b>{@code @author:}</b>wlpiaoyi</p>
 * <p><b>{@code @description:}</b></p>
 * <p><b>{@code @date:}</b>2024-10-08 17:38:55</p>
 * <p><b>{@code @version:}:</b>1.0</p>
 */
@Slf4j
public class WebUtils {

    public static final String HEADER_TOKEN_KEY = "token";
    public static final String HEADER_SALT_KEY = "salt";
    public static final String HEADER_SIGN_KEY = "sign";
    public static final String ENCRYPT_CONTENT_TYPE_HEAD_TAG = "#ENCRYPT#";

    /**
     * <p><b>{@code @description:}</b>
     *
     * </p>
     *
     * <p><b>{@code @param}</b> <b>uri</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>regexes</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/10/9 9:47</p>
     * <p><b>{@code @return:}</b>{@link boolean}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static boolean mathPath(final String path, final String[] regexes){
        if(ValueUtils.isBlank(regexes)){
            return false;
        }
        for (String regex : regexes) {
            if(Pattern.matches(regex, path)){
                return true;
            }
        }
        return false;
    }

    /**
     * <p><b>{@code @description:}</b>
     * 生成token
     * </p>
     *
     * <p><b>{@code @param}</b> <b>header</b>
     * {@link Map<String, Object>}
     * JWT头部分信息【Header】
     * </p>
     *
     * <p><b>{@code @param}</b> <b>claims</b>
     * {@link Map<String, Object>}
     * 载核【claims】
     * </p>
     *
     * <p><b>{@code @param}</b> <b>outTime</b>
     * {@link int}
     * Token有效期
     * </p>
     *
     * <p><b>{@code @param}</b> <b>secretKey</b>
     * {@link int}
     * 密钥
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/9 21:20</p>
     * <p><b>{@code @return:}</b>{@link String}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static String generateJwtToken(Map<String, Object> header, Map<String, Object> claims, int outSecond, String secretKey) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, outSecond);
        // 生成Token
        return Jwts.builder()
                .setHeader(header)// 设置Header
                .setClaims(claims) // 设置载核
                .setExpiration(instance.getTime())// 设置生效时间
                .signWith(SignatureAlgorithm.ES256.HS256, secretKey) // 签名,这里采用私钥进行签名,不要泄露了自己的私钥信息
                .compact();
    }


    /**
     * <p><b>{@code @description:}</b>
     * 从token中获取JWT中的负载
     * </p>
     *
     * <p><b>{@code @param}</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>secretKey</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/9 20:11</p>
     * <p><b>{@code @return:}</b>{@link Claims}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static Claims getClaimsByToken(String token, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
    /**
     * <p><b>{@code @description:}</b>
     * 从token中获取JWT中的Header
     * </p>
     *
     * <p><b>{@code @param}</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>secretKey</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/9 21:08</p>
     * <p><b>{@code @return:}</b>{@link JwsHeader}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static JwsHeader getHeaderByToken(String token, String secretKey) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getHeader();

    }

    /**
     * <p><b>{@code @description:}</b>
     * 当原来的token没过期时是可以刷新的
     * </p>
     *
     * <p><b>{@code @param}</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>secretKey</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/9 21:32</p>
     * <p><b>{@code @return:}</b>{@link String}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public String refreshToken(String token, int outSecond, String secretKey) {
        if(ValueUtils.isBlank(token)){
            return null;
        }
        //token校验不通过
        Claims claims = getClaimsByToken(token, secretKey);
        if(claims == null){
            return null;
        }
        //token校验不通过
        JwsHeader jwsHeader = getHeaderByToken(token, secretKey);
        if(jwsHeader == null){
            return null;
        }
        //如果token已经过期，不支持刷新
        if(isExpiredForToken(token, secretKey)){
            return null;
        }
        return generateJwtToken(jwsHeader, claims, outSecond, secretKey);
    }

    /**
     * <p><b>{@code @description:}</b>
     * token是过期
     * </p>
     *
     * <p><b>{@code @param}</b> <b>token</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @param}</b> <b>secretKey</b>
     * {@link String}
     * </p>
     *
     * <p><b>{@code @date:}</b>2024/11/9 21:18</p>
     * <p><b>{@code @return:}</b>{@link boolean}</p>
     * <p><b>{@code @author:}</b>wlpiaoyi</p>
     */
    public static boolean isExpiredForToken(String token, String secretKey) {
        Claims claims = getClaimsByToken(token, secretKey);
        return claims.getExpiration().before(new Date());
    }


}
