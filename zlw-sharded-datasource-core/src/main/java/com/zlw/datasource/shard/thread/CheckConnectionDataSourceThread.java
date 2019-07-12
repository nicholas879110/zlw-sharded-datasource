package com.zlw.datasource.shard.thread;


import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zlw.datasource.shard.ShardDataSource;
import com.zlw.datasource.shard.ShardDataSourceSupport;
import com.zlw.datasource.shard.SlaveConfig;




public class CheckConnectionDataSourceThread implements Runnable{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ShardDataSource shardDataSource ;
	private SlaveConfig slaveConfig ;
	
	private Boolean isExit = false;
	private Long interval = 5000L;
	
	public CheckConnectionDataSourceThread( ShardDataSource shardDataSource , SlaveConfig slaveConfig ){
		this.shardDataSource = shardDataSource ;
		this.slaveConfig = slaveConfig ;
	}
	
	

	@Override
	public void run() {
		while( ! isExit ) {
			try {
				Connection  conn = shardDataSource.determineTargetDataSource( slaveConfig.getName() ).getConnection() ;
				if( conn != null ){
					//将当前数据源加入
					ShardDataSourceSupport.addSlaveDataSources( slaveConfig );
					isExit = true ;//退出
				}
			} catch (Exception e) {
				try {
					if(slaveConfig.getInterval() !=null ){
						if( interval <= 1000 ){
							interval = 1000L ;
						}else if( slaveConfig.getInterval() >=60000 ){
							interval = 60000L;
						}else{
							interval = slaveConfig.getInterval();
						}
					}
					Thread.sleep( interval );
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		logger.info("数据源：{} 恢复正常 " ,slaveConfig.getName() );
	}
	
	
}
