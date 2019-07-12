package com.zlw.datasource.starter;

import com.zlw.datasource.shard.DruidDataSourceExt;
import com.zlw.datasource.shard.ShardDataSource;
import com.zlw.datasource.shard.ShardDataSourceAspect;
import com.zlw.datasource.shard.SlaveConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhangliewei
 * @date 2018/3/21 10:25
 */
@Configuration
@EnableConfigurationProperties(DatasourcesClusterProperties.class)
@ConditionalOnClass({DruidDataSourceExt.class})
public class DataSourceAutoConfiguration {

    @Autowired
    private DatasourcesClusterProperties clusterProperties;

    @Bean("dataSource")
    public ShardDataSource shardDataSource() {
        List<DruidDataSourceExt> dataSources = clusterProperties.getNodes();
        if (dataSources == null || dataSources.size() == 0) {
            throw new RuntimeException("at leaset one datasource,please check datasource");
        }
        DruidDataSourceExt master = null;
        int masterCounts = 0;
        Set<SlaveConfig> slaveConfigs = new HashSet<>();
        for (DruidDataSourceExt dataSource : dataSources) {
            if (dataSource.getMaster() != null && dataSource.getMaster()) {
                master = dataSource;
                masterCounts++;
            } else {
                SlaveConfig slaveConfig = new SlaveConfig();
                slaveConfig.setWeight(dataSource.getWeight());
                slaveConfig.setDataSources(dataSource);
                slaveConfig.setName(dataSource.getName());
                slaveConfig.setInterval(dataSource.getInterval());
                slaveConfigs.add(slaveConfig);
            }
        }
        if (master == null) {
            throw new RuntimeException("at leaset one master datasource,please check datasource");
        }
        if (masterCounts > 1) {
            throw new RuntimeException("more than one master datasource,please check datasource");
        }
        ShardDataSource shardDataSource = new ShardDataSource(master, master, slaveConfigs);
        return shardDataSource;
    }

    @Bean
    public ShardDataSourceAspect shardDataSourceAspect() {
        ShardDataSourceAspect shardDataSourceAspect = new ShardDataSourceAspect();
        return shardDataSourceAspect;
    }


}
