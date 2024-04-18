package shopping0;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CartTest {
    @Test
    void givenCart_whenPuttingALifestyleArticle_thenDeliveryAddressCanBePackstation() {
        
        var cartUnderTest = new Cart();
        cartUnderTest.put(new Article(new ArticleName("candle diffuser"), ArticleCategory.LIFESTYLE));
        assertThat(cartUnderTest.isDeliverable(AddressType.HOME)).isTrue();
    }
    
    @Test
    void givenCart_whenPuttingAFurnitureArticle_thenDeliveryAddressCannotBePackstation() {
        var cartUnderTest = new Cart();
        cartUnderTest.put(new Article(new ArticleName("couch"), ArticleCategory.FURNITURE));
        assertThat(cartUnderTest.isDeliverable(AddressType.PACKSTATION)).isFalse();
    }
}