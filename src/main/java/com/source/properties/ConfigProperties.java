package com.source.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/11/16:43
 */
@Data
@SpringBootConfiguration
@ConfigurationProperties(prefix = "qq")
public class ConfigProperties {

    private long app_id;

    private Integer daid;

    private String u1;

    private long pt_3rd_aid;

}
