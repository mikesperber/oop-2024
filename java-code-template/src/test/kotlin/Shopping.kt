package dddfpfriends.kotlin

import org.assertj.core.api.Assertions.*

import org.jmolecules.ddd.annotation.Entity
import org.jmolecules.ddd.annotation.ValueObject
import org.junit.jupiter.api.Test


@ValueObject
data class ArticleID(val id: Int)

@ValueObject
enum class ArticleCategory{ LIFESTYLE, FURNITURE }

@Entity
class Article(
    val id: ArticleID,
    val description: String,
    val category: ArticleCategory
) {
}

@ValueObject
data class DeliveryAddress(val address: String, val type: AddressType)

@ValueObject
enum class AddressType{ HOME_ADDRESS, PACKSTATION }


@Entity
class Cart {
    fun put(article: Article) {
        throw RuntimeException("Not implemented yet")
    }

    fun isDeliverableTo(address: DeliveryAddress): Boolean {
        throw RuntimeException("Not implemented yet")
    }
}


class CartTest {
    @Test
    fun givenAnEmptyCartWhenAddingALifestyleArticleThenDeliveryAddressCanBePackstation() {
        // given
        val cartUnderTest = Cart()
        
        // when
        cartUnderTest.put(Article(ArticleID(100), "White candles", ArticleCategory.LIFESTYLE));
        
        // then
        assertThat(
                cartUnderTest.isDeliverableTo(
                        DeliveryAddress("Privet Drive 4", AddressType.HOME_ADDRESS)))
            .isTrue();
    }

    @Test
    fun givenAnEmptyCartWhenAddingAFurnitureArticleThenDeliveryAddressCannotBePackstation() {
        // given
        var cartUnderTest = Cart()
        
        // when
        cartUnderTest.put(Article(ArticleID(101), "Leather sofa", ArticleCategory.FURNITURE))
        
        // then
        assertThat(
                cartUnderTest.isDeliverableTo(
                        DeliveryAddress("Diagon Alley", AddressType.PACKSTATION)))
                .isTrue()
    }
}
