{-# LANGUAGE InstanceSigs #-}
{-# LANGUAGE TypeSynonymInstances #-}
{-# OPTIONS_GHC -fwarn-incomplete-patterns #-}
module Cart where

import Data.Maybe (isJust)
-- Datenanalyse

-- >>> makeCart [couch, diffuser] Nothing
-- MakeYellowCart [MakeArticle "couch" Furniture,MakeArticle "candle diffuser" Lifestyle] Nothing
-- >>> makeCart [couch] (Just Home)
-- MakeGreenCart [MakeArticle "couch" Furniture] Home

-- Artikeltyp ist eins der folgenden:
-- - Möbel -ODER-
-- - Lifestyle
data ArticleType =
    Furniture | Lifestyle
    deriving Show

data ShippingAddress =
    Home | Packstation
    deriving Show

-- Artikel hat folgende Eigenschaften:
-- - Namen -UND-
-- - Typ
data Article = MakeArticle String ArticleType
  deriving Show

couch :: Article
couch = MakeArticle "couch" Furniture
diffuser :: Article
diffuser = MakeArticle "candle diffuser" Lifestyle

articleName :: Article -> String
articleName (MakeArticle name articleType) = name

data Cart article =
    MakeYellowCart [article] (Maybe ShippingAddress)
  | MakeGreenCart [article] ShippingAddress
  deriving Show

instance Functor Cart where
    fmap :: (a -> b) -> Cart a -> Cart b
    fmap f (MakeYellowCart articles shippingAddress) =
       MakeYellowCart (fmap f articles) shippingAddress
    fmap f (MakeGreenCart articles shippingAddress) =
       MakeGreenCart (fmap f articles) shippingAddress

data ReasonForInvalidCart =
    NoFurnitureToPackstation
    deriving Show
    
checkArticleShippingAddress :: Article -> ShippingAddress -> Maybe ReasonForInvalidCart
checkArticleShippingAddress (MakeArticle name Furniture) Packstation =
    Just NoFurnitureToPackstation
checkArticleShippingAddress (MakeArticle name Furniture) Home = Nothing
checkArticleShippingAddress (MakeArticle name Lifestyle) Packstation = Nothing
checkArticleShippingAddress (MakeArticle name Lifestyle) Home = Nothing


makeCart :: [Article] -> Maybe ShippingAddress -> Cart Article
makeCart articles Nothing = MakeYellowCart articles Nothing
makeCart articles (Just Packstation) =
    if any isJust (map (flip checkArticleShippingAddress Packstation) articles)
    then MakeYellowCart articles (Just Packstation)
    else MakeGreenCart articles Packstation
makeCart articles (Just Home) = MakeGreenCart articles Home
