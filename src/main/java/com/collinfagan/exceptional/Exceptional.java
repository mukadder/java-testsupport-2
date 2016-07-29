package com.collinfagan.exceptional;

import java.util.Objects;
import java.util.function.Consumer;

/*
 * 
 */
public class Exceptional<T> {

	private static final Exceptional<?> EMPTY = new Exceptional<>();

	private final T value;
	private final Exception exception;

	private Exceptional() {
		this.value = null;
		this.exception = null;
	}

	public void rethrowRuntime() {
		if (exception != null) {
			throw new RuntimeException(exception);
		}
	}

	public void rethrow() throws Exception {
		if (exception != null) {
			throw exception;
		}
	}

	public Exception getException() {
		return exception;
	}

	public static <T> Exceptional<T> empty() {
		@SuppressWarnings("unchecked")
		Exceptional<T> t = (Exceptional<T>) EMPTY;
		return t;
	}

	private Exceptional(T value) {
		this.value = Objects.requireNonNull(value);
		this.exception = null;
	}

	private Exceptional(Exception exception) {
		this.exception = Objects.requireNonNull(exception);
		value = null;
	}

	public static <T> Exceptional<T> of(T value) {
		return new Exceptional<>(value);
	}

	public static <T> Exceptional<T> of(Exception exception) {
		return new Exceptional<>(exception);
	}

	public static <T> Exceptional<T> ofException(Exception exception) {
		return exception == null ? empty() : of(exception);
	}

	public T get() {
		if (exception != null) {
			throw new RuntimeException(exception);
		}
		return value;
	}

	public boolean isPresent() {
		return exception == null;
	}

	public void ifPresent(Consumer<? super T> consumer) {
		if (exception == null) {
			consumer.accept(value);
		}
	}

	public void ifExceptionPresent(Consumer<? super Exception> consumer) {
		if (exception != null)
			consumer.accept(exception);
	}

	public void ifExceptionPresent(Class<? extends Exception> targetType, Consumer<? super Exception> consumer) {
		if (exception != null && targetType.isAssignableFrom(exception.getClass())) {
			consumer.accept(exception);
		}
	}

	public T orElse(T other) {
		return exception == null ? value : other;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Exceptional)) {
			return false;
		}

		Exceptional<?> other = (Exceptional<?>) obj;
		return Objects.equals(value, other.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return exception == null ? String.format("Exceptional[%s]", value)
				: String.format("Exceptional[%s]", exception);
	}

}
