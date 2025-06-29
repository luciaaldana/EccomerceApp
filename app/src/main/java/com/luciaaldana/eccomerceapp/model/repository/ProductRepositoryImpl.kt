package com.luciaaldana.eccomerceapp.model.repository

import com.luciaaldana.eccomerceapp.model.data.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(): ProductRepository {

    val imageUrlMock = "https://cdn-3.expansion.mx/dims4/default/5339ade/2147483647/strip/true/crop/8409x5945+0+0/resize/1200x848!/format/webp/quality/60/?url=https%3A%2F%2Fcdn-3.expansion.mx%2Fd8%2F15%2F97756b4a444287dbf850dee43121%2Fistock-1292443598.jpg"
    private val products = listOf(
        Product(1, "Auriculares Bluetooth", "Auriculares inalámbricos con cancelación de ruido.", 15000.0, imageUrlMock, "Tecnología", false),
        Product(2, "Remera estampada", "Remera 100% algodón con diseño exclusivo.", 8000.0, imageUrlMock, "Ropa", false),
        Product(3, "Combo Hamburguesa", "Incluye hamburguesa, papas y bebida.", 4500.0, imageUrlMock, "Comida", true),
        Product(4, "Zapatillas Urbanas", "Zapatillas de cuero sintético, cómodas y livianas.", 25000.0, imageUrlMock, "Calzado", false),
        Product(5, "Smartwatch Deportivo", "Control de pasos, ritmo cardíaco y notificaciones.", 32000.0, imageUrlMock, "Tecnología", false),
        Product(6, "Jeans Clásicos", "Jeans azul oscuro, corte recto.", 12000.0, imageUrlMock, "Ropa", false),
        Product(7, "Papas Fritas Grandes", "Ración grande de papas fritas.", 2000.0, imageUrlMock, "Comida", false),
        Product(8, "Combo Pizza + Gaseosa", "Pizza muzzarella + bebida de 500ml.", 5000.0, imageUrlMock, "Comida", true),
        Product(9, "Sillón Reclinable", "Sillón ideal para ver series con máxima comodidad.", 80000.0, imageUrlMock, "Hogar", false),
        Product(10, "Mesa Ratona", "Mesa de madera para living.", 25000.0, imageUrlMock, "Hogar", false),
        Product(11, "Mouse Gamer", "Mouse con sensor óptico y luces RGB.", 10000.0, imageUrlMock, "Tecnología", false),
        Product(12, "Gorra con visera", "Gorra regulable con diseño moderno.", 4500.0, imageUrlMock, "Ropa", false),
        Product(13, "Botines Fútbol", "Botines con tapones para cancha de césped.", 27000.0, imageUrlMock, "Calzado", false),
        Product(14, "Jugo Natural", "Jugo exprimido de naranja (500ml).", 1800.0, imageUrlMock, "Bebidas", true),
        Product(15, "Notebook 15''", "Notebook con procesador i5 y 8GB RAM.", 250000.0, imageUrlMock, "Tecnología", false),
        Product(16, "Hamburguesa Veggie", "Hamburguesa de vegetales + gaseosa.", 4700.0, imageUrlMock, "Comida", true),
        Product(17, "Camiseta Deportiva", "Camiseta dry-fit para entrenamiento.", 9500.0, imageUrlMock, "Ropa", false),
        Product(18, "Zapatillas Running", "Zapatillas livianas para correr.", 31000.0, imageUrlMock, "Calzado", false),
        Product(19, "Café Helado", "Café frío en botella (250ml).", 2200.0, imageUrlMock, "Bebidas", true),
        Product(20, "Lámpara LED", "Lámpara de escritorio con luz blanca.", 7000.0, imageUrlMock, "Hogar", false)
    )

    override fun getAllProducts(): List<Product> = products
}