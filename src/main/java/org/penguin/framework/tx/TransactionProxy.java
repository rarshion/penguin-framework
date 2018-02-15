package org.penguin.framework.tx;

import org.penguin.framework.aop.proxy.Proxy;
import org.penguin.framework.aop.proxy.ProxyChain;
import org.penguin.framework.dao.DatabaseHelper;
import org.penguin.framework.tx.annotation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Author ： Martin
 * Date : 18/2/14
 * Description :
 * Version : 2.0
 */
public class TransactionProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> flagContainer = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag = flagContainer.get();
        Method method = proxyChain.getTargetMethod();
        if (!flag && method.isAnnotationPresent(Transaction.class)) {
            flagContainer.set(true);
            try {
                DatabaseHelper.beginTransaction();
                logger.debug("[penguin] begin traction");
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                logger.debug("[penguin] commit traction");
            } catch (Exception ex) {
                DatabaseHelper.rollbackTransaction();
                logger.debug("[penguin] rollback traction");
                throw ex;
            } finally {
                flagContainer.remove();
            }
        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
