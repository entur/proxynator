package org.rutebanken.proxynator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;

/**
 *
 */
@Service
public final class LittleProxyService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${etcd.endpoint:delete}")
    @NotNull
    private String etcdEndpoint;

    @Value("${etcd.prefix:delete}")
    @NotNull
    private String prefix;

    @PostConstruct
    public void createEtcdclient()  {
        log.info("Using the following as etcd endpoint: " + etcdEndpoint);
    }

    @PreDestroy
    public void closeClient() {
    }

    public boolean isAlive() {
        try {
            return true;
        } catch ( Exception e ) {
            log.error("Got exception checking for version. Method isAlive will return false. Masked, the exception was: "+e);
            return false;
        }
    }

}
