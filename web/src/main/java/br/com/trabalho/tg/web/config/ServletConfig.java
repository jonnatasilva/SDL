package br.com.trabalho.tg.web.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@Configuration
@EnableWebMvc
@ComponentScan("br.com.trabalho.tg")
public class ServletConfig extends WebMvcConfigurerAdapter {

   @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
   
   @Bean
   public SessionLocaleResolver localeResolver() {
	   SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
	   sessionLocaleResolver.setDefaultLocale(Locale.ENGLISH);
	   return sessionLocaleResolver;
   }
   
   @Bean 
   public LocaleChangeInterceptor localeChangeInterceptor() {
	   LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	   localeChangeInterceptor.setParamName("locale");
	   return localeChangeInterceptor;
   }
   
   @Bean
   public ControllerClassNameHandlerMapping controllerClassNameHandlerMapping() {
	   ControllerClassNameHandlerMapping controllerClassNameHandlerMapping = new ControllerClassNameHandlerMapping();
	   Object[] o = {localeChangeInterceptor()};
	   controllerClassNameHandlerMapping.setInterceptors(o);
	   return controllerClassNameHandlerMapping;
   }
   
   @Bean
   public ResourceBundleMessageSource messageSource() {
	   ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
	   resourceBundleMessageSource.setBasename("classpath:global");
	   return resourceBundleMessageSource;
   }
   
    
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/resources/").addResourceLocations("/resources/");
    	registry.addResourceHandler("/static/**").addResourceLocations("WEB-INF/static/");
    }
}