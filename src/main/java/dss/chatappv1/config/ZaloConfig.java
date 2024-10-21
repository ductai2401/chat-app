package dss.chatappv1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.util.Properties;

@Configuration
public class ZaloConfig {
    @Value("${zalo.oa.access.token}")
    private String accessToken;

    @Value("${zalo.oa.id}")
    private String oaId;

    public String getAccessToken() {
        return accessToken;
    }

    public String getOaId(){
        return oaId;
    }

}
