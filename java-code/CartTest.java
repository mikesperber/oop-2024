public class CartTest {
    @Test
    void givenAnEmptyCartWhenAddingAFashionArticleThenPaymentMethodCanBeSepaMandate() {
        // given
        var cartUnderTest = new Cart();
        
        // when
        cartUnderTest.put(new FashionArticle("Christian Dior Skirt XS"));
        
        // then
        assertThat(cartUnderTest).isPaymentMethodAllowed(PaymentMethod.SEPA_MANDATE).equals(true);
    }

    @Test
    void givenAnEmptyCartWhenAddingACosmeticsArticleThenPaymentMethodCannotBeSepaMandate() {
        // given
        var cartUnderTest = new Cart();
        
        // when
        cartUnderTest.put(new CosmeticsArticle("Gucci Red and Blue"));
        
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