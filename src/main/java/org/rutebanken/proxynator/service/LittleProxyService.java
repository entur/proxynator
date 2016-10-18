package org.rutebanken.proxynator.service;

import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
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

    @Value("${proxy.port:9097}")
    @NotNull
    private Integer port;

    private HttpProxyServer server;

    @PostConstruct
    public void createProxy()  {
        log.info("Listening on proxy port: " + port);

        server = DefaultHttpProxyServer.bootstrap()
                .withAllowLocalOnly(false)
                .withPort(port.intValue())
                .start();

    }

    @PreDestroy
    public void closeProxy() {
        log.info("Stopping proxy server" );
        server.stop();
        server = null;
        log.info("Proxy server stopped" );
    }

    public boolean isAlive() {
        return server != null;
    }

}
