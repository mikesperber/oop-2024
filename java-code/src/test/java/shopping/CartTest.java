package shopping;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import shopping.*;

class CartTest {
    @Test
    void givenACart_whenPuttingALifestyleArticle_thenDeliveryAddressCanBePackstation() {
        // given
        var cartUnderTest = new Cart();

        // when
        cartUnderTest.put(new Article(new ArticleNumber(1),
            new ArticleName("White Candles"), ArticleCategory.LIFESTYLE));

        // then
        assertThat(cartUnderTest.isDeliverableTo(AddressType.HOME_ADDRESS)).isTrue();
    }

    @Test
    void givenACart_whenPuttingAFurnitureArticle_thenDeliveryAddressCannotBePackstation() {
        // given
        var cartUnderTest = new Cart();

        // when
        cartUnderTest.put(new Article(new ArticleNumber(1),
            new ArticleName("Leather sofa"), ArticleCategory.FURNITURE));

        // then
        assertThat(cartUnderTest.isDeliverableTo(AddressType.PACKSTATION)).isFalse();
    }

    @Test
    void givenACart_whenShippingToHomeAddress_thenDeliverable() {
        // given
        var cartUnderTest = new Cart();
        
        // when
        cartUnderTest.put(new Article(new ArticleNumber(1),
            new ArticleName("Leather sofa"), ArticleCategory.FURNITURE));
        
        // then
        assertThat(cartUnderTest.isDeliverableTo(AddressType.HOME_ADDRESS)).isTrue();
    }
    
    @Test
    void givenACart_whenPuttingFurnitureAndLifeStyle_thenDeliveryAddressCannotBePackstation() {
        // given
        var cartUnderTest = new Cart();
        
        // when
        cartUnderTest.put(new Article(new ArticleNumber(1),
            new ArticleName("Leather sofa"), ArticleCategory.FURNITURE));
        cartUnderTest.put(new Article(new ArticleNumber(1),
            new ArticleName("White Candles"), ArticleCategory.LIFESTYLE));
        
        // then
        assertThat(cartUnderTest.isDeliverableTo(AddressType.PACKSTATION)).isFalse();
    }
}
