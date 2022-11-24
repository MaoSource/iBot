package com.source.factory;

import com.source.bean.bot.Message;
import com.source.service.MessageTypeHandler;
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
 * @date 2022/11/23/16:41
 */
@Component
@Slf4j
public class MessageTypeFactory implements SmartInitializingSingleton {

    final private List<MessageTypeHandler> messageApis;

    public MessageTypeFactory() {
        messageApis = new ArrayList<>();
    }

    public void add(MessageTypeHandler messageApi) {
        messageApis.add(messageApi);
    }

    public MessageTypeHandler getMessageTypeHandler(Message msg) {
        MessageTypeHandler baseMessageApiFound = null;
        for (MessageTypeHandler baseMessageApi : messageApis) {
            if (baseMessageApi.canMessage(msg)) {
                baseMessageApiFound = baseMessageApi;
            }
        }
        return baseMessageApiFound;
    }

    @Override
    public void afterSingletonsInstantiated() {
        log.info("正在装载PrivateHandler...");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) SpringContextUtil.getApplicationContext()).getBeanFactory();
        String[] beanNames = beanFactory.getBeanNamesForType(MessageTypeHandler.class);
        Arrays.stream(beanNames).forEach(beanName -> {
            this.add(((MessageTypeHandler) SpringContextUtil.getBean(beanName)));
            log.info(String.format("装载 [%s]", beanName));
        });
        log.info("装载PrivateHandler结束");
    }
}
