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

{-
data TentativeAddress
    = ShippingAddress Address
    | InvalidShippingAddress Address InvalidShippingAddressReason
    | NoShippingAddress

data Cart
    = GreenCart [Article] Address
    | YellowCart [Article] TentativeAddress

putArticleInCart :: Article -> Cart -> Cart
putArticleInCart article (GreenCart articles address) =
    makeCart (articles ++ [article]) 
             (checkShippingAddress address article)
putArticleInCart article (YellowCart articles tentativeAddress) =
    undefined

makeCart :: [Article] -> TentativeAddress -> Cart
makeCart articles _ = undefined

checkShippingAddress :: Address -> Article -> TentativeAddress
checkShippingAddress PackStation Furniture =
    InvalidShippingAddress PackStation NoFurnitureToPackStation
checkShippingAddress address article =
    ShippingAddress address
-}

{-

-- Try #2

data Tentative reason a
  = TentativeGood a
  | TentativeInvalid reason
  | TentativeNothing

instance Functor (Tentative reason) where
  fmap f (TentativeGood a) = TentativeGood (f a)
  fmap f (TentativeInvalid reason) = TentativeInvalid reason
  fmap f TentativeNothing = TentativeNothing

instance Applicative (Tentative reason) where
  (TentativeGood f) <*> (TentativeGood a) = TentativeGood (f a)
  (TentativeInvalid reason) <*> _ = TentativeInvalid reason
  _ <*> (TentativeInvalid reason) = TentativeInvalid reason
  _ <*> _ = TentativeNothing
  pure = TentativeGood

instance Monad (Tentative a) where
    return = pure
    (TentativeGood a) >>= next = next a
    (TentativeInvalid reason) >>= _next = TentativeInvalid reason
    TentativeNothing >>= _next = TentativeNothing

type TentativeAddress = Tentative (Address, InvalidShippingAddressReason) Address

data Cart
  = GreenCart [Article] Address
  | YellowCart [Article] TentativeAddress

putArticleInCart :: Article -> Cart -> Cart
putArticleInCart article (GreenCart articles address) =
  cart
    (articles ++ [article])
    (checkShippingAddress article address)
putArticleInCart article (YellowCart articles tentativeAddress) =
  YellowCart (articles ++ [article]) 
    (tentativeAddress >>= checkShippingAddress article)

-- cart :: [Article] -> TentativeAddress -> Cart
cart articles tentativeAddress =
  case GreenCart articles <$> tentativeAddress of
    TentativeGood cart -> cart
    _ -> YellowCart articles tentativeAddress

-- smell: only generates two possibilities, also does not capture that the address is the same
checkShippingAddress :: Article -> Address -> TentativeAddress
checkShippingAddress Furniture PackStation =
    TentativeInvalid (PackStation, NoFurnitureToPackStation)
checkShippingAddress article address = TentativeGood address

checkShippingAddress' :: Article -> TentativeAddress -> TentativeAddress
checkShippingAddress' article (TentativeGood address) =
    checkShippingAddress article address
checkShippingAddress' article (TentativeInvalid (address, reason)) =
    undefined
-- this suggests one address, several accumulating reasons
-- effectively Option (a, [reason])
checkShippingAddress' article TentativeNothing = TentativeNothing

-}

data Tentative reason a
  = TentativeGood a
  | TentativeInvalid a [reason]

instance Functor (Tentative reason) where
  fmap f (TentativeGood a) = TentativeGood (f a)
  fmap f (TentativeInvalid a reasons) = TentativeInvalid (f a) reasons

instance Applicative (Tentative reason) where
  (TentativeGood f) <*> (TentativeGood a) = TentativeGood (f a)
  (TentativeInvalid f reasons) <*> (TentativeInvalid a reasons') = TentativeInvalid (f a) (reasons ++ reasons')
  (TentativeInvalid f reasons) <*> TentativeGood a = TentativeInvalid (f a) reasons
  (TentativeGood f) <*> (TentativeInvalid a reasons) = TentativeInvalid (f a) reasons
  pure = TentativeGood

instance Monad (Tentative reason) where
    (TentativeGood a) >>= next = next a
    (TentativeInvalid a reasons) >>= next =
        case next a of -- just the Writer monad
            TentativeGood a' -> TentativeInvalid a' reasons
            TentativeInvalid a' reasons' ->
                TentativeInvalid a' (reasons ++ reasons')

type TentativeAddress = Tentative InvalidShippingAddressReason Address

data Cart
  = GreenCart [Article] Address
  | YellowCart [Article] (Maybe TentativeAddress)

checkShippingAddress :: Article -> Address -> TentativeAddress
checkShippingAddress Furniture PackStation =
  TentativeInvalid PackStation [NoFurnitureToPackStation]
checkShippingAddress article address = TentativeGood address

checkShippingAddress' :: Article -> TentativeAddress -> TentativeAddress
checkShippingAddress' article tentativeAddress =
    tentativeAddress >>= checkShippingAddress article

 -- cart :: [Article] -> TentativeAddress -> Cart
cart :: [Article] -> Maybe (Tentative InvalidShippingAddressReason Address) -> Cart
cart articles maybeTentativeAddress =
  case (GreenCart articles <$>) <$> maybeTentativeAddress of
    Just (TentativeGood cart) -> cart
    _ -> YellowCart articles maybeTentativeAddress

putArticleInCart :: Article -> Cart -> Cart
putArticleInCart article (GreenCart articles address) =
  cart
    (articles ++ [article])
    (Just (checkShippingAddress article address))
putArticleInCart article (YellowCart articles tentativeAddress) =
  YellowCart
    (articles ++ [article])
    (checkShippingAddress' article <$> tentativeAddress)
