package com.hard.config.init;

import com.hard.config.MvcConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
                MvcConfig.class,
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{
                "/",
        };
    }
}
