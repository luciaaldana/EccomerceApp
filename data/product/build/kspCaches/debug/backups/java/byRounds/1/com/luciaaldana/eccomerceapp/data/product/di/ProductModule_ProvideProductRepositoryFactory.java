package com.luciaaldana.eccomerceapp.data.product.di;

import com.luciaaldana.eccomerceapp.data.product.network.ProductApi;
import com.luciaaldana.eccomerceapp.domain.product.ProductRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ProductModule_ProvideProductRepositoryFactory implements Factory<ProductRepository> {
  private final Provider<ProductApi> productApiProvider;

  public ProductModule_ProvideProductRepositoryFactory(Provider<ProductApi> productApiProvider) {
    this.productApiProvider = productApiProvider;
  }

  @Override
  public ProductRepository get() {
    return provideProductRepository(productApiProvider.get());
  }

  public static ProductModule_ProvideProductRepositoryFactory create(
      Provider<ProductApi> productApiProvider) {
    return new ProductModule_ProvideProductRepositoryFactory(productApiProvider);
  }

  public static ProductRepository provideProductRepository(ProductApi productApi) {
    return Preconditions.checkNotNullFromProvides(ProductModule.INSTANCE.provideProductRepository(productApi));
  }
}
