package edu.store.vaadin.ui.services;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/** @author Naresh Joshi */
@Service
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public static <T> T createBean(Class<T> beanClass) {
        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
        return beanFactory.createBean(beanClass);
    }

    public static <T> T createComponent(Class<T> componentClass) {
        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
        return beanFactory.createBean(componentClass);
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
