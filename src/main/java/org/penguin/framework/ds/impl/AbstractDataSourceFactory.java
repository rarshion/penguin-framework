package org.penguin.framework.ds.impl;

import org.penguin.framework.core.ConfigHelper;
import org.penguin.framework.ds.DataSourceFactory;

import javax.sql.DataSource;

/**
 * Author ： Martin
 * Date : 18/2/12
 * Description :
 * Version : 2.0
 */
public abstract class AbstractDataSourceFactory<T extends DataSource> implements DataSourceFactory {

    protected final String driver = ConfigHelper.getString("smart.framework.jdbc.driver");
    protected final String url = ConfigHelper.getString("smart.framework.jdbc.url");
    protected final String username = ConfigHelper.getString("smart.framework.jdbc.username");
    protected final String password = ConfigHelper.getString("smart.framework.jdbc.password");

    @Override
    public final T getDataSource() {
        T ds = createDataSource();
        setDriver(ds, driver);
        setUrl(ds, url);
        setUsername(ds, username);
        setPassword(ds, password);
        setAdvancedConfig(ds);
        return ds;
    }

    public abstract T createDataSource();

    public abstract void setDriver(T ds, String driver);

    public abstract void setUrl(T ds, String url);

    public abstract void setUsername(T ds, String username);

    public abstract void setPassword(T ds, String password);

    public abstract void setAdvancedConfig(T ds);
}
