package com.luciaaldana.eccomerceapp.data.cart.di;

import com.luciaaldana.eccomerceapp.domain.cart.OrderHistoryRepository;
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
public final class CartModule_ProvideOrderHistoryRepositoryFactory implements Factory<OrderHistoryRepository> {
  @Override
  public OrderHistoryRepository get() {
    return provideOrderHistoryRepository();
  }

  public static CartModule_ProvideOrderHistoryRepositoryFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OrderHistoryRepository provideOrderHistoryRepository() {
    return Preconditions.checkNotNullFromProvides(CartModule.INSTANCE.provideOrderHistoryRepository());
  }

  private static final class InstanceHolder {
    private static final CartModule_ProvideOrderHistoryRepositoryFactory INSTANCE = new CartModule_ProvideOrderHistoryRepositoryFactory();
  }
}
