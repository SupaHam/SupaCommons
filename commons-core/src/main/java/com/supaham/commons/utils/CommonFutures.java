package com.supaham.commons.utils;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

import javax.annotation.Nonnull;

/**
 * Utility methods for working with {@link Future} instances. This class contains methods such as
 * {@link #onSuccess(ListenableFuture, SuccessCallback)},
 * {@link #onFailure(ListenableFuture, FailureCallback)}, and more.
 *
 * @since 0.3.3
 */
public class CommonFutures {

  public static <V> CommonFutureBuilder<V> builder(@Nonnull ListenableFuture<V> future) {
    Preconditions.checkNotNull(future, "future cannot be null.");
    return new CommonFutureBuilder<>(future);
  }

  @Nonnull
  public static <V> ListenableFuture<V> onSuccess(@Nonnull final ListenableFuture<V> future,
                                                  @Nonnull final SuccessCallback<V> callback) {
    Preconditions.checkNotNull(future, "future cannot be null.");
    Preconditions.checkNotNull(callback, "callback cannot be null.");
    Futures.addCallback(future, new FutureCallback<V>() {
      @Override public void onSuccess(V result) {
        callback.success(result);
      }

      @Override public void onFailure(Throwable t) {}
    });
    return future;
  }

  @Nonnull
  public static <V> ListenableFuture<V> onFailure(@Nonnull final ListenableFuture<V> future,
                                                  @Nonnull final FailureCallback callback) {
    Preconditions.checkNotNull(future, "future cannot be null.");
    Preconditions.checkNotNull(callback, "callback cannot be null.");
    Futures.addCallback(future, new FutureCallback<V>() {
      @Override public void onSuccess(V result) {}

      @Override public void onFailure(@Nonnull Throwable t) {
        callback.failure(t);
      }
    });
    return future;
  }

  public interface SuccessCallback<V> {

    void success(V result);
  }

  public interface FailureCallback {

    void failure(Throwable t);
  }

  public static final class CommonFutureBuilder<V> {

    private final ListenableFuture<V> future;

    private CommonFutureBuilder(ListenableFuture<V> future) {
      this.future = future;
    }

    public ListenableFuture<V> get() {
      return this.future;
    }

    public CommonFutureBuilder<V> onSuccess(@Nonnull final SuccessCallback<V> callback) {
      CommonFutures.onSuccess(this.future, callback);
      return this;
    }

    public CommonFutureBuilder<V> onFailure(@Nonnull final FailureCallback callback) {
      CommonFutures.onFailure(this.future, callback);
      return this;
    }
  }
}
