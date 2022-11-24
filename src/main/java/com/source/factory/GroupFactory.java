package com.source.factory;

import com.source.bean.bot.Message;
import com.source.service.GroupHandler;
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
 * @date 2022/11/23/15:51
 */
@Component
@Slf4j
public class GroupFactory implements SmartInitializingSingleton {

    final private List<GroupHandler> messageApis;

    public GroupFactory() {
        messageApis = new ArrayList<>();
    }

    public void add(GroupHandler messageApi) {
        messageApis.add(messageApi);
    }

    public GroupHandler getGroupHandler(Message msg) {
        GroupHandler baseMessageApiFound = null;
        for (GroupHandler baseMessageApi : messageApis) {
            if (baseMessageApi.canAnalyze(msg)) {
                baseMessageApiFound = baseMessageApi;
            }
        }
        return baseMessageApiFound;
    }

    @Override
    public void afterSingletonsInstantiated() {
        log.info("正在装载GroupHandler...");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) SpringContextUtil.getApplicationContext()).getBeanFactory();
        String[] beanNames = beanFactory.getBeanNamesForType(GroupHandler.class);
        Arrays.stream(beanNames).forEach(beanName -> {
            this.add(((GroupHandler) SpringContextUtil.getBean(beanName)));
            log.info(String.format("装载 [%s]", beanName));
        });
        log.info("装载GroupHandler结束");
    }

}
