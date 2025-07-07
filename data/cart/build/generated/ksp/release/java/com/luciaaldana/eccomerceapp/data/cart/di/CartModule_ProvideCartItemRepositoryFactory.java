package com.luciaaldana.eccomerceapp.data.cart.di;

import com.luciaaldana.eccomerceapp.domain.cart.CartItemRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class CartModule_ProvideCartItemRepositoryFactory implements Factory<CartItemRepository> {
  @Override
  public CartItemRepository get() {
    return provideCartItemRepository();
  }

  public static CartModule_ProvideCartItemRepositoryFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CartItemRepository provideCartItemRepository() {
    return Preconditions.checkNotNullFromProvides(CartModule.INSTANCE.provideCartItemRepository());
  }

  private static final class InstanceHolder {
    private static final CartModule_ProvideCartItemRepositoryFactory INSTANCE = new CartModule_ProvideCartItemRepositoryFactory();
  }
}
