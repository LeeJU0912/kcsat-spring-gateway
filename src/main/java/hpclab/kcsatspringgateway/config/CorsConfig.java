package hpclab.kcsatspringgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * CORS 설정을 담당하는 클래스입니다.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("https://csatmaker.site", "https://www.csatmaker.site"));
        config.setAllowCredentials(true);  // 자격 증명 허용
        config.addAllowedHeader("*");  // 모든 헤더 허용
        config.addAllowedMethod("*");  // 모든 HTTP 메서드 허용

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
