module Cart where

data Article 
    = Lifestyle
    | Furniture

data ShippingAddress
    = HomeAddress
    | PackStation

data TentativeShippingAddress
    = NoShippingAddress
    | ValidShippingAddress ShippingAddress
    | InvalidShippingAddress ShippingAddress ReasonForInvalidShippingAddress

data ReasonForInvalidShippingAddress =
    NoFurnitureToPackStation
