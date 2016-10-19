package org.rutebanken.proxynator.service;

import io.netty.handler.codec.http.HttpRequest;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TraceService traceService;

    @Value("${proxy.port:9077}")
    @NotNull
    private Integer port;

    private HttpProxyServer server;

    @PostConstruct
    public void createProxy()  {
        log.info("Listening on proxy port: " + port);

        server = DefaultHttpProxyServer.bootstrap()
                .withAllowLocalOnly(false)
                .withPort(port.intValue())
                .withFiltersSource( createFilterSource())
                .withTransparent(true)
                .start();
    }

    private HttpFiltersSource createFilterSource() {
        return new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest) {
                return new TracerFilter(traceService, originalRequest);
            }

            /*
            @Override
            public int getMaximumRequestBufferSizeInBytes() {
                return 10 * 1024 * 1024;
            }

            @Override
            public int getMaximumResponseBufferSizeInBytes() {
                return 10 * 1024 * 1024;
            }
            */
        };
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
