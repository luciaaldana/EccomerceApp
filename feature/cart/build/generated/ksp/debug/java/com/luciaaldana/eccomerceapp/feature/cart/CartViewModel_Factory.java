package com.luciaaldana.eccomerceapp.feature.cart;

import com.luciaaldana.eccomerceapp.domain.cart.CartItemRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class CartViewModel_Factory implements Factory<CartViewModel> {
  private final Provider<CartItemRepository> cartItemRepositoryProvider;

  public CartViewModel_Factory(Provider<CartItemRepository> cartItemRepositoryProvider) {
    this.cartItemRepositoryProvider = cartItemRepositoryProvider;
  }

  @Override
  public CartViewModel get() {
    return newInstance(cartItemRepositoryProvider.get());
  }

  public static CartViewModel_Factory create(
      Provider<CartItemRepository> cartItemRepositoryProvider) {
    return new CartViewModel_Factory(cartItemRepositoryProvider);
  }

  public static CartViewModel newInstance(CartItemRepository cartItemRepository) {
    return new CartViewModel(cartItemRepository);
  }
}
