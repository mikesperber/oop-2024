public class CartTest {
    @Test
    void givenAnEmptyCartWhenAddingALifestyleArticleThenDeliveryAddressCanBePackstation() {
        // given
        var cartUnderTest = new Cart();
        
        // when
        cartUnderTest.put(new Article(ArticleCategory.LIFESTYLE, "Candles"));
        
        // then
        assertThat(cartUnderTest).isPaymentMethodAllowed(PaymentMethod.SEPA_MANDATE).equals(true);
    }

    @Test
    void givenAnEmptyCartWhenAddingAFurnitureArticleThenDeliveryAddressCannotBePackstation() {
        // given
        var cartUnderTest = new Cart();
        
        // when
        cartUnderTest.put(new Article("Gucci Red and Blue"));
        
        // then
        assertThat(cartUnderTest).isPaymentMethodAllowed(PaymentMethod.SEPA_MANDATE).equals(false);
    }
}

class FashionArticle {
    public FashionArticle(string name) {

    }
}

class Cart {
    void put(FashionArticle article) {

    }
    boolean isEmpty() {
        
    }
}


// TDD = test-driven development
// BDD = behavior-driven development
// DDD = domain-driven design