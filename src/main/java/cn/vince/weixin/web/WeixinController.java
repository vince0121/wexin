package cn.vince.weixin.web;

import cn.vince.weixin.util.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@Controller
public class WeixinController {
    private final static String TOKEN = "vince0121";

    @RequestMapping(value = "/valid_Token", method = RequestMethod.GET)
    @ResponseBody
    public String validToken(@RequestParam String signature, @RequestParam String timestamp, @RequestParam String nonce, @RequestParam String echostr) {
        String[] arr = {signature, timestamp, nonce, echostr};
        for (String string : arr) {
            if (StringUtils.isEmpty(string)) {
                return "";
            }
        }
        String[] arr1 = {TOKEN, timestamp, nonce};
        Arrays.sort(arr1);
        String s = StringUtils.arrayToDelimitedString(arr1, "");
        String sha1 = CommonUtil.getStrSHA1(s);
        if (sha1.equals(signature)) {
            return echostr;
        } else
            return "";
    }
}
