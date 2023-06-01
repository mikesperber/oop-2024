import static org.assertj.core.api.Assertions.*;

import org.jmolecules.ddd.annotation.ValueObject;
import org.junit.jupiter.api.Test;


@ValueObject
record ArticleID(int id) {
    public static ArticleID of(int id) {
        return new ArticleID(id);
    }
}

enum ArticleCategory{ LIFESTYLE, FURNITURE }

class Article {

    public Article(ArticleID id, String description, ArticleCategory category) {
    }

}

record DeliveryAddress(String address, AddressType type) {
    public static DeliveryAddress of(String address, AddressType type) {
        return new DeliveryAddress(address, type);
    }
}

enum AddressType{ HOME_ADDRESS, PACKSTATION }


class Cart {
    public void put(Article article) {
        throw new RuntimeException("Not implemented yet");
    }

    public boolean isDeliverableTo(DeliveryAddress address) {
        throw new RuntimeException("Not implemented yet");
    }
}


class CartTest {
    @Test
    void givenAnEmptyCartWhenAddingALifestyleArticleThenDeliveryAddressCanBePackstation() {
        // given
        var cartUnderTest = new Cart();
        
        // when
        cartUnderTest.put(new Article(ArticleID.of(100), "White candles", ArticleCategory.LIFESTYLE));
        
        // then
        assertThat(
                cartUnderTest.isDeliverableTo(
                        DeliveryAddress.of("Privet Drive 4", AddressType.HOME_ADDRESS)))
            .isTrue();
    }

    @Test
    void givenAnEmptyCartWhenAddingAFurnitureArticleThenDeliveryAddressCannotBePackstation() {
        // given
        var cartUnderTest = new Cart();
        
        // when
        cartUnderTest.put(new Article(new ArticleID(101), "Leather sofa", ArticleCategory.FURNITURE));
        
        // then
        assertThat(
                cartUnderTest.isDeliverableTo(
                        DeliveryAddress.of("Diagon Alley", AddressType.PACKSTATION)))
                .isTrue();
    }
}
