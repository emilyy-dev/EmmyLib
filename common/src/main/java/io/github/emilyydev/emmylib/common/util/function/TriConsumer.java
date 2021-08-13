package io.github.emilyydev.emmylib.common.util.function;

@FunctionalInterface
public interface TriConsumer<T, U, V> {

  void accept(T t, U u, V v);
}
