package org.penguin.framework.ds.impl;

import org.apache.commons.dbcp.BasicDataSource;
import org.penguin.framework.ds.impl.AbstractDataSourceFactory;

/**
 * Author ： Martin
 * Date : 18/2/12
 * Description :
 * Version : 2.0
 */
public class DefaultDataSourceFactory extends AbstractDataSourceFactory <BasicDataSource> {

    @Override
    public BasicDataSource createDataSource() {
        return new BasicDataSource();
    }

    @Override
    public void setDriver(BasicDataSource ds, String driver) {
        ds.setDriverClassName(driver);
    }

    @Override
    public void setUrl(BasicDataSource ds, String url) {
        ds.setUrl(url);
    }

    @Override
    public void setUsername(BasicDataSource ds, String username) {
        ds.setUsername(username);
    }

    @Override
    public void setPassword(BasicDataSource ds, String password) {
        ds.setPassword(password);
    }

    @Override
    public void setAdvancedConfig(BasicDataSource ds) {
        ds.setValidationQuery("select 1 from dual");
    }
}
