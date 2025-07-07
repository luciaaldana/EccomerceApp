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
public final class CartItemRepositoryImpl_Factory implements Factory<CartItemRepositoryImpl> {
  @Override
  public CartItemRepositoryImpl get() {
    return newInstance();
  }

  public static CartItemRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CartItemRepositoryImpl newInstance() {
    return new CartItemRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final CartItemRepositoryImpl_Factory INSTANCE = new CartItemRepositoryImpl_Factory();
  }
}
