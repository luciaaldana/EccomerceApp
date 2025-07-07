package com.luciaaldana.eccomerceapp.feature.home;

import com.luciaaldana.eccomerceapp.domain.product.ProductRepository;
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
public final class ProductsViewModel_Factory implements Factory<ProductsViewModel> {
  private final Provider<ProductRepository> productRepositoryProvider;

  public ProductsViewModel_Factory(Provider<ProductRepository> productRepositoryProvider) {
    this.productRepositoryProvider = productRepositoryProvider;
  }

  @Override
  public ProductsViewModel get() {
    return newInstance(productRepositoryProvider.get());
  }

  public static ProductsViewModel_Factory create(
      Provider<ProductRepository> productRepositoryProvider) {
    return new ProductsViewModel_Factory(productRepositoryProvider);
  }

  public static ProductsViewModel newInstance(ProductRepository productRepository) {
    return new ProductsViewModel(productRepository);
  }
}
