package org.rutebanken.proxynator.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.trace.DefaultTraceContextHandler;
import com.google.cloud.trace.ManagedTracer;
import com.google.cloud.trace.RawTracer;
import com.google.cloud.trace.TraceContextFactoryTracer;
import com.google.cloud.trace.TraceContextHandler;
import com.google.cloud.trace.TraceContextHandlerTracer;
import com.google.cloud.trace.Tracer;
import com.google.cloud.trace.grpc.v1.GrpcTraceSink;
import com.google.cloud.trace.util.ConstantTraceOptionsFactory;
import com.google.cloud.trace.util.JavaTimestampFactory;
import com.google.cloud.trace.util.TimestampFactory;
import com.google.cloud.trace.util.TraceContextFactory;
import com.google.cloud.trace.v1.RawTracerV1;
import com.google.cloud.trace.v1.sink.TraceSink;
import com.google.cloud.trace.v1.source.TraceSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 */
@Service
public class TraceService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Secret defaulting to Erlends installation, which surely will match only him
     */
    @Value("${google.credentials.location:/home/en/Private/Carbon-eb93b021fde2.json}")
    @NotNull
    private String clientSecretsFile;

    @Value("${project.id:carbon-1287}")
    @NotNull
    private String projectId;

    @Value("${trace.is.enabled:true}")
    @NotNull
    private Boolean toBeUsed;

    private Tracer tracer;

    private TraceContextHandler traceContextHandler;

    @PostConstruct
    public void initializeTracer() throws IOException {
        // Create the raw tracer.
        TraceSource traceSource = new TraceSource();
        TraceSink traceSink = new GrpcTraceSink("cloudtrace.googleapis.com",
                GoogleCredentials.fromStream(new FileInputStream(clientSecretsFile)));
        RawTracer rawTracer = new RawTracerV1(projectId, traceSource, traceSink);

        // Create the tracer.
        TraceContextFactory traceContextFactory = new TraceContextFactory(
                new ConstantTraceOptionsFactory(true, false));
        TimestampFactory timestampFactory = new JavaTimestampFactory();
        tracer = new TraceContextFactoryTracer(rawTracer, traceContextFactory, timestampFactory);

        // Create the managed tracer.
        traceContextHandler = new DefaultTraceContextHandler(
                traceContextFactory.rootContext());
    }

    public boolean isAlive() {
        return traceContextHandler != null && tracer != null;
    }

    /**
     *
     * <code>
     # Example: Create some trace data:

     managedTracer.startSpan("rutebanken-helper");
     # Nested trace
     managedTracer.startSpan("junit");

     StackTrace.Builder stackTraceBuilder = ThrowableStackTraceHelper.createBuilder(new Exception());
     managedTracer.setStackTrace(stackTraceBuilder.build());
     managedTracer.endSpan();

     managedTracer.endSpan();
     </code>
     * @return A tracer created with basis in the service
     */
    public ManagedTracer createManagedTracer() {
        return new TraceContextHandlerTracer(tracer, traceContextHandler);
    }

    public boolean isToBeUsed() {
        return toBeUsed.booleanValue();
    }
}
