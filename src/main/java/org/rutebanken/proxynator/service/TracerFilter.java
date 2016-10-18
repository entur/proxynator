package org.rutebanken.proxynator.service;

import com.google.cloud.trace.ManagedTracer;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.impl.ProxyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TracerFilter extends HttpFiltersAdapter {

    private final ManagedTracer managedTracer;

    private HttpRequest originalRequest;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public TracerFilter(TraceService traceService, HttpRequest originalRequest) {
        super(originalRequest);
        if ( traceService.isToBeUsed() ) {
            this.managedTracer = traceService.createManagedTracer();
        } else {
            this.managedTracer = null;
        }
        this.originalRequest = originalRequest;
    }

    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
        if ( httpObject instanceof HttpRequest ) {
            log.info("Initiating http request ("+originalRequest.uri()+")");

            /*
            if (httpObject instanceof HttpMessage) {
                HttpHeaders headers =(((HttpMessage) httpObject).headers());
                for ( Map.Entry<String, String> x : headers ) {
                    log.info("  "+x.getKey()+" = "+x.getValue());
                }
            }*/
        } else if ( httpObject instanceof HttpResponse ) {
            log.info("Initiating http response");
        }

        return null;
    }

    @Override
    public HttpObject serverToProxyResponse(HttpObject httpObject) {
        if ( ProxyUtils.isLastChunk(httpObject)) {
            log.info("Call finished... ("+originalRequest.uri()+")");
        }

        return httpObject;
    }
}
