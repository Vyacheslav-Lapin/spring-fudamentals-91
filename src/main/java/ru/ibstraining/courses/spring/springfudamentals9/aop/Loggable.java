package ru.ibstraining.courses.spring.springfudamentals9.aop;

import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.ibstraining.courses.spring.springfudamentals9.commons.AspectUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.BiConsumer;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface Loggable {
  LogLevel value() default LogLevel.INFO;

  enum LogLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR
  }
}

@Slf4j
@Aspect
@Component
@ExtensionMethod(AspectUtils.class)
class LoggableAspect {

  @Pointcut("@annotation(Loggable) || within(@Loggable *)"
            + " || execution(@(@Loggable *) * *(..)) || within (@(@Loggable *) *)"
            + " || execution(@(@(@Loggable *) *) * *(..)) || within (@(@(@Loggable *) *) *)"
            + " || execution(@(@(@(@Loggable *) *) *) * *(..)) || within (@(@(@(@Loggable *) *) *) *)"
            + " || execution(@(@(@(@(@Loggable *) *) *) *) * *(..)) || within (@(@(@(@(@Loggable *) *) *) *) *)"
            + " || execution(@(@(@(@(@(@Loggable *) *) *) *) *) * *(..)) || within (@(@(@(@(@(@Loggable *) *) *) *) *) *)"
            + " || execution(@(@(@(@(@(@(@Loggable *) *) *) *) *) *) * *(..)) || within (@(@(@(@(@(@(@Loggable *) *) *) *) *) *) *)"
            + " || execution(@(@(@(@(@(@(@(@Loggable *) *) *) *) *) *) *) * *(..)) || within (@(@(@(@(@(@(@(@Loggable *) *) *) *) *) *) *) *)")
  void pointcut() {
  }

  @SneakyThrows
  @Around("pointcut()")
  public Object around(ProceedingJoinPoint pjp) {
    val signature = pjp.getSignature();
    val typeName = signature.getDeclaringTypeName();
    val name = signature.getName();
    val logMethod = getLogMethod(pjp);
//    log.info("%s.%s".formatted(typeName, name));
    logMethod.accept("{}.{}", new Object[]{typeName, name});

    val args = pjp.getArgs();

    val result = pjp.proceed(args);

    logMethod.accept("{}.{} вернул {}", new Object[]{typeName, name, result});
//    log.info("%s.%s вернул %s".formatted(typeName, name, result));

    return result;
  }

  private BiConsumer<String, Object[]> getLogMethod(ProceedingJoinPoint pjp) {
    return switch (pjp.getAnnotation(Loggable.class).value()) {
      case INFO -> log::info;
      case DEBUG -> log::debug;
      case WARN -> log::warn;
      case ERROR -> log::error;
      case TRACE -> log::trace;
    };
  }
}
