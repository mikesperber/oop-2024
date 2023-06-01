# DDD and FP Can’t Be Friends, Yet

## Introduction

Mike: I have customer
Henning: Domain Story EAKI-1
Mike: I see cart, cart, cart -> Lets write code to model the cart
Henning: I would do strategic design first

## Strategic Design

Henning: Draw boundaries EAKI-1
[Mike: Want to write code!]

... Packstation ...

H&M agree to focus on collection, so we learn live
about problems with delivery address.

## Mike writes code

```haskell
module Cart where

data Article =
    Lifestyle
  | Furniture
  deriving Show

data Address =
    HomeAddress
  | PackStation
  deriving Show

data InvalidShippingAddressReason
    = NoFurnitureToPackStation
```

Henning objects: Not discussed with domain expert.

Mike: You write code, Henning.

## OO Modeling

Henning: I would start in a TDD/BDD fashion.

```java
public class CartTest {
    @Test
    void givenAnEmptyCartWhenAddingALifestyleArticleThenDeliveryAddressCanBePackstation() {
        // given
        var cartUnderTest = new Cart();
        
        // when
        cartUnderTest.put(new Article(ArticleID.of(100), "White candles", ArticleCategory.LIFESTYLE));
        
        // then
        assertThat(cartUnderTest).isDeliverableTo(DeliveryAddress.OF("Privet Drive 4", AddressType.HOME_ADDRESS).equals(true);
    }

    @Test
    void givenAnEmptyCartWhenAddingAFurnitureArticleThenDeliveryAddressCannotBePackstation() {
        // given
        var cartUnderTest = new Cart();
        
        // when
        cartUnderTest.put(new Article(ArticleID.of(101), "Leather sofa", ArticleCategory.FURNITURE));
        
        // then
        assertThat(cartUnderTest)
            .isDeliverableTo(DeliveryAddress.OF("Diagon Alley", AddressType.PACKSTATION)
            .equals(true);
    }
}
```

Mike interrupts: OK, Henning, you design behavior - method signatures.  I design data.  Let me show how these two go together.

```haskell
checkShippingAddress :: Address -> Article -> TentativeAddress
checkShippingAddress PackStation Furniture =
  InvalidShippingAddress PackStation NoFurnitureToPackStation
checkShippingAddress address article =
  ShippingAddress address
```

Conclusion: Different approaches, but we can benefit from
each other.

## Shopping cart

Mike: Henning, how would you flesh out the `Cart` class?

... TODO Henning ...

... validation? ...

```haskell
data Cart
    = ReadyCart [Article] Address
    | DraftCart [Article] TentativeAddress
```

Henning: You just made up new words.

H&M: But “ready” and “draft” are not an immediate part of the 
domain concept, and thus the domain vocabulary.  Need to
develop new words.

```haskell
data Cart
    = GreenCart [Article] Address
    | YellowCart [Article] TentativeAddress
```

... validation ...

## Abstraction (optional)

```haskell
data Tentative reason a
  = TentativeGood a
  | TentativeInvalid a [reason]

instance Functor (Tentative reason) where ...

instance Applicative (Tentative reason) where ...
```

Henning: OMG, next thing your’re gonna write is a monad...

Mike: Great idea!

```haskell
instance Monad (Tentative reason) where ...
```

Henning: WHY? Those are all great FP terms, but they are not part of the domain vocabulary and totally disconnected to the domain. This is all about wanking/jerking of on FP terms/math but not about shopping!

Mike: They're not part of the domain vocabulary, they are
*properties* of things in your domain vocabulary.  I'm using
them to learn about the domain.  Also, they are useful
when writing code:

```haskell
type TentativeAddress
  = Tentative InvalidShippingAddressReason Address

data Cart
  = GreenCart [Article] Address
  | YellowCart [Article] (Maybe TentativeAddress)

putArticleInCart article (YellowCart articles tentativeAddress) =
  YellowCart
    (articles ++ [article])
    (fmap (>>= checkShippingAddress article) tentativeAddress)

 -- cart :: [Article] -> TentativeAddress -> Cart
cart :: [Article] -> Maybe (Tentative InvalidShippingAddressReason Address) -> Cart
cart articles maybeTentativeAddress =
  case fmap (fmap (GreenCart articles)) maybeTentativeAddress of
    Just (TentativeGood cart) -> cart
    _ -> YellowCart articles maybeTentativeAddress
```

## Optional

- article -> entity -> ID
