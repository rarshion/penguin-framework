package org.penguin.framework.plugin;

import javax.servlet.ServletContext;

/**
 * Author ： Martin
 * Date : 18/2/14
 * Description :
 * Version : 2.0
 */
public abstract class WebPlugin  implements Plugin {

    public abstract void register(ServletContext servletContext);

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}
