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
    YellowCart (articles ++ [article]) tentativeAddress

checkShippingAddress :: Address -> Article -> TentativeAddress
checkShippingAddress PackStation Furniture =
  InvalidShippingAddress PackStation NoFurnitureToPackStation
checkShippingAddress address article =
  ShippingAddress address

makeCart :: [Article] -> TentativeAddress -> Cart
makeCart articles tentativeAddress = case tentativeAddress of
  ShippingAddress address -> GreenCart articles address
  _ -> YellowCart articles tentativeAddress

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

putArticleInCart :: Article -> Cart -> Cart
putArticleInCart article (GreenCart articles address) =
  cart
    (articles ++ [article])
    (Just (checkShippingAddress article address))
putArticleInCart article (YellowCart articles tentativeAddress) =
  YellowCart
    (articles ++ [article])
    (fmap (>>= checkShippingAddress article) tentativeAddress)
    
checkShippingAddress :: Article -> Address -> TentativeAddress
checkShippingAddress Furniture PackStation =
  TentativeInvalid PackStation [NoFurnitureToPackStation]
checkShippingAddress article address = TentativeGood address

 -- cart :: [Article] -> TentativeAddress -> Cart
cart :: [Article] -> Maybe (Tentative InvalidShippingAddressReason Address) -> Cart
cart articles maybeTentativeAddress =
  case fmap (fmap (GreenCart articles)) maybeTentativeAddress of
    Just (TentativeGood cart) -> cart
    _ -> YellowCart articles maybeTentativeAddress

