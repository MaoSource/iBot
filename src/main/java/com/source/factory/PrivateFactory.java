package com.source.factory;

import com.source.bean.bot.Message;
import com.source.service.PrivateHandler;
import com.source.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/23/16:25
 */
@Component
@Slf4j
public class PrivateFactory implements SmartInitializingSingleton {

    final private List<PrivateHandler> messageApis;

    public PrivateFactory() {
        messageApis = new ArrayList<>();
    }

    public void add(PrivateHandler messageApi) {
        messageApis.add(messageApi);
    }

    public PrivateHandler getPrivateHandler(Message msg) {
        PrivateHandler baseMessageApiFound = null;
        for (PrivateHandler baseMessageApi : messageApis) {
            if (baseMessageApi.canAnalyze(msg)) {
                baseMessageApiFound = baseMessageApi;
            }
        }
        return baseMessageApiFound;
    }

    @Override
    public void afterSingletonsInstantiated() {
        log.info("正在装载PrivateHandler...");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) SpringContextUtil.getApplicationContext()).getBeanFactory();
        String[] beanNames = beanFactory.getBeanNamesForType(PrivateHandler.class);
        Arrays.stream(beanNames).forEach(beanName -> {
            this.add(((PrivateHandler) SpringContextUtil.getBean(beanName)));
            log.info(String.format("装载 [%s]", beanName));
        });
        log.info("装载PrivateHandler结束");
    }

}
