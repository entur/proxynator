package org.rutebanken.proxynator.service;

import com.google.cloud.trace.ManagedTracer;
import com.sun.org.apache.xerces.internal.util.URI;
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
            String name = originalRequest.uri();
            log.info("Call starting... ("+name+")");
            try {
                URI uri = new URI( name );
                name = uri.getHost()+uri.getPath();
            } catch (URI.MalformedURIException ignored) {}
            managedTracer.startSpan(name);
        }

        return null;
    }

    @Override
    public HttpObject serverToProxyResponse(HttpObject httpObject) {
        if ( ProxyUtils.isLastChunk(httpObject)) {
            log.info("Call finished... ("+originalRequest.uri()+")");
            managedTracer.endSpan();
        }

        return httpObject;
    }
}
