package cn.vince.weixin.util;

import cn.vince.weixin.service.WexinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

public class RedisUtil {

    protected static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    @Autowired
    private static RedisTemplate redisTemplate;
    @Autowired
    private static WexinService wexinService;

    public static String getAccessToken(String key) {
        try {
            String access_token = (String) redisTemplate.opsForValue().get("access_token");
            logger.info("从redis中获取access_token，access_token:" + access_token);
            if (StringUtils.isEmpty(access_token)) {
                access_token = wexinService.getAccessToken();
                logger.info("访问微信服务获取access_token，access_token:" + access_token);
                redisTemplate.opsForValue().set("access_token", access_token, 1000 * 7000);
            }
            return access_token;
        } catch (Exception e) {
            logger.error("获取access_token失败，原因：", e);
            return "";
        }
    }
}
