package demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
    
    @Autowired
    private LocaleChangeInterceptor localeChangeInterceptor;
    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    private StringHttpMessageConverter stringHttpMessageConverter;
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer getPropertySource() {
        return  new PropertySourcesPlaceholderConfigurer();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor);
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter);
        converters.add(stringHttpMessageConverter);
        super.configureMessageConverters(converters);
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
        internalResourceViewResolver.setContentType("text/html");
        internalResourceViewResolver.setPrefix("/WEB-INF/views/");
        internalResourceViewResolver.setSuffix(".jsp");
        registry.viewResolver(internalResourceViewResolver);
    }
    
}
