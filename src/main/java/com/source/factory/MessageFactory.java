package com.source.factory;

import com.source.bean.bot.Message;
import com.source.service.MessageHandler;
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
 * @date 2022/11/23/15:10
 */
@Component
@Slf4j
public class MessageFactory implements SmartInitializingSingleton {

    final private List<MessageHandler> messageApis;

    public MessageFactory() {
        messageApis = new ArrayList<>();
    }

    public void add(MessageHandler messageApi) {
        messageApis.add(messageApi);
    }

    public MessageHandler getMessageApi(Message msg) {
        MessageHandler baseMessageApiFound = null;
        for (MessageHandler baseMessageApi : messageApis) {
            if (baseMessageApi.canMessage(msg)) {
                baseMessageApiFound = baseMessageApi;
            }
        }
        return baseMessageApiFound;
    }

    @Override
    public void afterSingletonsInstantiated() {
        log.info("正在装载MessageApiFactory...");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) SpringContextUtil.getApplicationContext()).getBeanFactory();
        String[] beanNames = beanFactory.getBeanNamesForType(MessageHandler.class);
        Arrays.stream(beanNames).forEach(beanName -> {
            this.add(((MessageHandler) SpringContextUtil.getBean(beanName)));
            log.info(String.format("装载 [%s]", beanName));
        });
        log.info("装载MessageApiFactory结束");
    }
}
