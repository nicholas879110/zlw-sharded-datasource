package com.zlw.datasource.starter;

import com.zlw.datasource.shard.DruidDataSourceExt;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2017/3/24
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DatasourcesClusterProperties {

    List<DruidDataSourceExt> nodes;

    public List<DruidDataSourceExt> getNodes() {
        return nodes;
    }

    public void setNodes(List<DruidDataSourceExt> nodes) {
        this.nodes = nodes;
    }
}
