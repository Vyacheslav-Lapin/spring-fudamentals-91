package ru.ibstraining.courses.spring.springfudamentals9.commons;

import lombok.experimental.UtilityClass;
import org.hibernate.proxy.HibernateProxy;

@UtilityClass
public class HibernateUtils {
  public <T> Class<T> getEffectiveClass(T o) {
    return (Class<T>) (o instanceof HibernateProxy proxy ?
                       proxy.getHibernateLazyInitializer()
                           .getPersistentClass() : o.getClass());
  }
}
