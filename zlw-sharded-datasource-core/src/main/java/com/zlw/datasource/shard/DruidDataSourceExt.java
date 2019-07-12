package com.zlw.datasource.shard;

import com.alibaba.druid.pool.DruidDataSource;
import com.zlw.datasource.AESCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangliewei
 * @date 2018/3/21 16:37
 */
public class DruidDataSourceExt extends DruidDataSource {

    private static final long serialVersionUID = -7689153489743510707L;

    private static final String DEFAULT_CODEC_KEY = "0132457689BACDFE";


    private Logger LOG = LoggerFactory.getLogger(getClass());

    /***数据源是否是master,只能有一个master**/
    private Boolean master;

    /*** 权重*/
    private Integer weight = 1;

    /**
     * slave 数据检查数据源是否可用时间间隔
     **/
    private Long interval;


    @Override
    public String getUsername() {
        LOG.info("username : " + super.getUsername());
        return AESCodec.Decrypt(super.getUsername(), DEFAULT_CODEC_KEY);
    }

    @Override
    public String getPassword() {
        LOG.info("password : " + super.getPassword());
        return AESCodec.Decrypt(super.getPassword(), DEFAULT_CODEC_KEY);
    }

    public Boolean getMaster() {
        return master;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }






//	@Override
//	public String getUrl() {
//		LOG.info("url : " + super.getUrl());
//		return AESCodec.Decrypt( super.getUrl()  , DEFAULT_CODEC_KEY  );
//	}


}


