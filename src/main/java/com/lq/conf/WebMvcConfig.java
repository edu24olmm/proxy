package com.lq.conf;

import com.lq.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
//@Order(value = 3)
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	private LoginInterceptor loginInterceptor;

	@Autowired
	@Qualifier("loginInterceptor")
	public void setAuthInterceptor(LoginInterceptor authInterceptor) {
		this.loginInterceptor = authInterceptor;
	}
	
	/**
	 * 注册自定义 类型转换器 [String --> Number] 当类型转换失败返回null
	 * @param defaultConversionService
	 */
	/*@Autowired
	@Qualifier("mvcConversionService")
	public void setDefaultConversionService(ConfigurableConversionService defaultConversionService){
		defaultConversionService.removeConvertible(String.class, Number.class);
		defaultConversionService.addConverterFactory(new HrStringToNumberConverterFactory());
		defaultConversionService.addConverterFactory(new StringToDateConverterFactory());
	}*/

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		super.addInterceptors(registry);
		registry.addInterceptor(loginInterceptor);
		loginInterceptor = null;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		converters.add(new MappingJackson2HttpMessageConverter());
		converters.add(new StringHttpMessageConverter());
	}

}
