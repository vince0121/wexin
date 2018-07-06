package cn.vince.wexin;

import cn.vince.weixin.WexinApplication;
import cn.vince.weixin.service.WexinService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = WexinApplication.class)
@RunWith(SpringRunner.class)
public class WexinServiceTest {
    @Autowired
    private WexinService wexinService;

    @Test
    public void accessTokenTest() {
        String accessToken = wexinService.getAccessToken();
    }
}
