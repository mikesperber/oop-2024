{-# LANGUAGE InstanceSigs #-}
{-# LANGUAGE TypeSynonymInstances #-}
{-# OPTIONS_GHC -fwarn-incomplete-patterns #-}
module Cart where

import qualified Data.Set as Set
import Data.Set (Set)
import Data.List (foldl')

data Article 
    = Lifestyle
    | Furniture

data ShippingAddress
    = HomeAddress
    | PackStation

data ReasonForInvalidShippingAddress =
    NoFurnitureToPackStation
    deriving (Show, Eq, Ord)

data GreenCart =
    MkGreenCart [Article] ShippingAddress

-- this is where yellow vs. green resides

data Tentative reason a =
    TentativeGood a 
  | TentativeNoGood a (Set reason)
  
-- aren't the reasons attached to the articles?
-- no - domain says it's a problem with the shipping address

tentativeValue :: Tentative reason a -> a
tentativeValue (TentativeGood a) = a
tentativeValue (TentativeNoGood a _) = a 

type TentativeShippingAddress = Tentative ReasonForInvalidShippingAddress ShippingAddress

data YellowCart =
    MkYellowCart [Article] (Maybe TentativeShippingAddress)



checkArticleShippingAddress :: Article -> TentativeShippingAddress -> TentativeShippingAddress
checkArticleShippingAddress article t@(TentativeGood HomeAddress) = t
checkArticleShippingAddress Lifestyle t@(TentativeGood PackStation) = t
checkArticleShippingAddress Furniture (TentativeGood PackStation) =
    TentativeNoGood PackStation (Set.singleton NoFurnitureToPackStation)
checkArticleShippingAddress Furniture (TentativeNoGood PackStation reasons) =
    TentativeNoGood PackStation (Set.insert NoFurnitureToPackStation reasons)
checkArticleShippingAddress article t@(TentativeNoGood shippingAddress reasons) = t

mkYellowCart :: [Article] -> Maybe ShippingAddress -> YellowCart
mkYellowCart articles Nothing = MkYellowCart articles Nothing
mkYellowCart articles (Just shippingAddress) =
    let tentativeShippingAddress = 
          foldl' (flip checkArticleShippingAddress) (TentativeGood shippingAddress) articles
    in MkYellowCart articles (Just tentativeShippingAddress)

cartSingleton :: Article -> YellowCart
cartSingleton article = MkYellowCart [article] Nothing

combineCarts :: YellowCart -> YellowCart -> YellowCart
combineCarts (MkYellowCart articles1 Nothing)
             (MkYellowCart articles2 Nothing) =
    mkYellowCart (articles1 ++ articles2) Nothing
combineCarts (MkYellowCart articles1 (Just tentativeShippingAddress1))
             (MkYellowCart articles2 Nothing) =
    mkYellowCart (articles1 ++ articles2) (Just (tentativeValue tentativeShippingAddress1))
combineCarts (MkYellowCart articles1 Nothing)
             (MkYellowCart articles2 (Just tentativeShippingAddress2)) =
    mkYellowCart (articles1 ++ articles2) (Just (tentativeValue tentativeShippingAddress2))
combineCarts (MkYellowCart articles1 (Just tentativeShippingAddress1))
             (MkYellowCart articles2 (Just tentativeShippingAddress2)) =
    mkYellowCart (articles1 ++ articles2) (Just (tentativeValue tentativeShippingAddress1))

addArticleToCart :: Article -> YellowCart -> YellowCart
addArticleToCart article cart = combineCarts (cartSingleton article) cart


-- sum type? just do one cart type

