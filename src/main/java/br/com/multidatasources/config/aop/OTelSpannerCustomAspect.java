package br.com.multidatasources.config.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.trace.Span;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@ConditionalOnProperty(
    value = "open-telemetry.aop.enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class OTelSpannerCustomAspect {

    private final ObjectMapper objectMapper;

    public OTelSpannerCustomAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("@within(OTelSpannerCustom)")
    public Object execute(final ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.currentTimeMillis();

        final Object proceed = joinPoint.proceed();

        final long executionTime = System.currentTimeMillis() - start;
        final String methodName = Objects.isNull(joinPoint.getSignature()) ? "Unrecognized" : joinPoint.getSignature().toShortString();
        final var response = objectMapper.writeValueAsString(proceed);

        final Span span = Span.current();
        span.setAttribute("response", Objects.isNull(response) ? "method called is void" : response);
        span.setAttribute("executionTime", executionTime);
        span.setAttribute("methodCalled", methodName);

        return proceed;
    }

}
