package com.luciaaldana.eccomerceapp.feature.profile;

import com.luciaaldana.eccomerceapp.domain.cart.CartItemRepository;
import com.luciaaldana.eccomerceapp.domain.cart.OrderHistoryRepository;
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
public final class OrderHistoryViewModel_Factory implements Factory<OrderHistoryViewModel> {
  private final Provider<CartItemRepository> cartRepoProvider;

  private final Provider<OrderHistoryRepository> orderRepoProvider;

  public OrderHistoryViewModel_Factory(Provider<CartItemRepository> cartRepoProvider,
      Provider<OrderHistoryRepository> orderRepoProvider) {
    this.cartRepoProvider = cartRepoProvider;
    this.orderRepoProvider = orderRepoProvider;
  }

  @Override
  public OrderHistoryViewModel get() {
    return newInstance(cartRepoProvider.get(), orderRepoProvider.get());
  }

  public static OrderHistoryViewModel_Factory create(Provider<CartItemRepository> cartRepoProvider,
      Provider<OrderHistoryRepository> orderRepoProvider) {
    return new OrderHistoryViewModel_Factory(cartRepoProvider, orderRepoProvider);
  }

  public static OrderHistoryViewModel newInstance(CartItemRepository cartRepo,
      OrderHistoryRepository orderRepo) {
    return new OrderHistoryViewModel(cartRepo, orderRepo);
  }
}
