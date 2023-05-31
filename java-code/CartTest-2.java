public class CartTest {
    @Test
    void givenAnEmptyCartWhenAddingACosmeticsArticleThenCartIsNotEmpty() {
        // given
        var cartUnderTest = new Cart();
        
        // when
        cartUnderTest.put(new FashionArticle("Christian Dior Skirt XS"));
        
        // then
        assertThat(cartUnderTest).isEmpty().equals(false);
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