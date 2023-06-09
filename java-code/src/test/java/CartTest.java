import static org.assertj.core.api.Assertions.*;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.ValueObject;
import org.junit.jupiter.api.Test;

@ValueObject
record ArticleID(int id) {
    public static ArticleID of(int id) {
        return new ArticleID(id);
    }
}

@ValueObject
enum ArticleCategory{ LIFESTYLE, FURNITURE }

@Entity
class Article {

    public Article(ArticleID id, String description, ArticleCategory category) {
    }

}

@ValueObject
record DeliveryAddress(String address, AddressType type) {
    public static DeliveryAddress of(String address, AddressType type) {
        return new DeliveryAddress(address, type);
    }
}

@ValueObject
enum AddressType{ HOME_ADDRESS, PACKSTATION }


@Entity
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
    void givenAnEmptyCart_WhenAddingALifestyleArticle_ThenDeliveryAddressCanBePackstation() {
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
    void givenAnEmptyCart_WhenAddingAFurnitureArticle_ThenDeliveryAddressCannotBePackstation() {
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
