package com.luciaaldana.eccomerceapp.data.product.di;

import com.luciaaldana.eccomerceapp.data.product.network.ProductApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("javax.inject.Named")
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
public final class ProductModule_ProvideProductApiFactory implements Factory<ProductApi> {
  private final Provider<Retrofit> retrofitProvider;

  public ProductModule_ProvideProductApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public ProductApi get() {
    return provideProductApi(retrofitProvider.get());
  }

  public static ProductModule_ProvideProductApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new ProductModule_ProvideProductApiFactory(retrofitProvider);
  }

  public static ProductApi provideProductApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(ProductModule.INSTANCE.provideProductApi(retrofit));
  }
}
