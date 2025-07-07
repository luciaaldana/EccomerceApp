package com.luciaaldana.eccomerceapp.data.product;

import com.luciaaldana.eccomerceapp.data.product.network.ProductApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ProductRepositoryImpl_Factory implements Factory<ProductRepositoryImpl> {
  private final Provider<ProductApi> apiProvider;

  public ProductRepositoryImpl_Factory(Provider<ProductApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public ProductRepositoryImpl get() {
    return newInstance(apiProvider.get());
  }

  public static ProductRepositoryImpl_Factory create(Provider<ProductApi> apiProvider) {
    return new ProductRepositoryImpl_Factory(apiProvider);
  }

  public static ProductRepositoryImpl newInstance(ProductApi api) {
    return new ProductRepositoryImpl(api);
  }
}
