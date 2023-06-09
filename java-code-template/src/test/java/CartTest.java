import static org.assertj.core.api.Assertions.*;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.ValueObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    private List<Article> articles = new ArrayList<>();
    
    public void put(Article article) {
        articles.add(article);
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








@Entity
private class Cart {
    public void put(Article whiteCandles) {
    }
    public boolean isDeliverableTo(AddressType addressType) {
        return false;
    }
}

@ValueObject
private record ArticleName(String name) {
}

private enum ArticleCategory {
    FURNITURE, LIFESTYLE
}

private class Article {
    public Article(ArticleName name, ArticleCategory category) {
    }
}

private enum AddressType {
    PACKSTATION, HOME_ADDRESS
}