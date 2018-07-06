package cn.vince.weixin.service.impl;

import cn.vince.weixin.service.WexinService;
import cn.vince.weixin.util.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

@Service("weixinService")
public class WexinServiceImpl implements WexinService {

    public static final String ACCESS_TOKEN = "access_token";
    private static Logger logger = LoggerFactory.getLogger(WexinServiceImpl.class);
    @Value("${weixin.appid-test}")
    private String APP_ID;
    @Value("${weixin.appsecret-test}")
    private String APP_SECRET;
    @Value("${weixin.get_access_token.url}")
    private String ACCESS_TOKEN_URL;
    @Value("${weixin.get_access_token.grant_type}")
    private String GRANT_TYPE;
    @Autowired
    private StringRedisTemplate template;

    @Override
    public String getAccessTokenFromWeixin() {
        try {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("grant_type", GRANT_TYPE);
            paramMap.put("appid", APP_ID);
            paramMap.put("secret", APP_SECRET);
            String response = HttpUtil.doGet(ACCESS_TOKEN_URL, paramMap);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            logger.info("访问微信获取accessToken接口，返回：" + rootNode.asText());
            String accessToken = rootNode.path(ACCESS_TOKEN).asText();
            if (!StringUtils.isEmpty(accessToken)) {
                return accessToken;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAccessToken() {
        try {
            String access_token = template.opsForValue().get(ACCESS_TOKEN);
            logger.info("从redis中获取access_token，access_token:" + access_token);
            if (StringUtils.isEmpty(access_token)) {
                access_token = getAccessTokenFromWeixin();
                logger.info("访问微信服务获取access_token，access_token:" + access_token);
                template.opsForValue().set(ACCESS_TOKEN, access_token, 7200 * 1000);
            }
            return access_token;
        } catch (Exception e) {
            logger.error("获取access_token失败，原因：", e);
            return null;
        }
    }
}
