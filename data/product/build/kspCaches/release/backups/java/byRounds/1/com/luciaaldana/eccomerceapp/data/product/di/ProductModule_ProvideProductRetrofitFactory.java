package com.luciaaldana.eccomerceapp.data.product.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
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
public final class ProductModule_ProvideProductRetrofitFactory implements Factory<Retrofit> {
  @Override
  public Retrofit get() {
    return provideProductRetrofit();
  }

  public static ProductModule_ProvideProductRetrofitFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static Retrofit provideProductRetrofit() {
    return Preconditions.checkNotNullFromProvides(ProductModule.INSTANCE.provideProductRetrofit());
  }

  private static final class InstanceHolder {
    private static final ProductModule_ProvideProductRetrofitFactory INSTANCE = new ProductModule_ProvideProductRetrofitFactory();
  }
}
