package com.luciaaldana.eccomerceapp.data.cart;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class OrderHistoryRepositoryImpl_Factory implements Factory<OrderHistoryRepositoryImpl> {
  @Override
  public OrderHistoryRepositoryImpl get() {
    return newInstance();
  }

  public static OrderHistoryRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OrderHistoryRepositoryImpl newInstance() {
    return new OrderHistoryRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final OrderHistoryRepositoryImpl_Factory INSTANCE = new OrderHistoryRepositoryImpl_Factory();
  }
}
