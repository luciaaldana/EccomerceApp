-if class com.luciaaldana.eccomerceapp.data.product.dto.ProductDto
-keepnames class com.luciaaldana.eccomerceapp.data.product.dto.ProductDto
-if class com.luciaaldana.eccomerceapp.data.product.dto.ProductDto
-keep class com.luciaaldana.eccomerceapp.data.product.dto.ProductDtoJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.luciaaldana.eccomerceapp.data.product.dto.ProductDto
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.luciaaldana.eccomerceapp.data.product.dto.ProductDto
-keepclassmembers class com.luciaaldana.eccomerceapp.data.product.dto.ProductDto {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.String,java.lang.String,double,java.lang.String,java.lang.Boolean,java.lang.String,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
