package net.planet01.oauthauthorizationserver.config;

import net.planet01.oauthauthorizationserver.filter.ApplicationAccessFilter;
import net.planet01.oauthauthorizationserver.security.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationAccessFilterConfig {

    @Autowired
    private CustomUserDetailService userService;


    @Bean
    public FilterRegistrationBean<ApplicationAccessFilter> registerPostCommentsRateLimiter(){
        FilterRegistrationBean<ApplicationAccessFilter> registrationBean  = new FilterRegistrationBean<>();

        registrationBean.setFilter(new ApplicationAccessFilter(userService));
        registrationBean.addUrlPatterns("/oauth/token");

        return registrationBean;
    }
}
