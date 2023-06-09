import static org.assertj.core.api.Assertions.*;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.ValueObject;
import org.junit.jupiter.api.Test;

class CartTest {
    @Test
    void givenACart_whenPuttingALifestyleArticle_thenDeliveryAddressCanBePackstation() {
        // given
        var cartUnderTest = new Cart();

        // when
        cartUnderTest.put(new Article(new ArticleName("White Candles"), ArticleCategory.LIFESTYLE));

        // then
        assertThat(cartUnderTest.isDeliverableTo(AddressType.HOME_ADDRESS)).isTrue();
    }

    @Test
    void givenACart_whenPuttingAFurnitureArticle_thenDeliveryAddressCannotBePackstation() {
        // given
        var cartUnderTest = new Cart();

        // when
        cartUnderTest.put(new Article(new ArticleName("Leather sofa"), ArticleCategory.FURNITURE));

        // then
        assertThat(cartUnderTest.isDeliverableTo(AddressType.PACKSTATION)).isFalse();
    }
}
