# Logic Model obtained by transforming Conceptual Model

* `Person`( <u>email</u>, lastName, firstName );  
* `Route` ( <u>id</u>, emailPerson[FK], description, classification, duration, category, time );  
* `CultureRoute` ( <u>id</u>, emailPerson[FK], description, classification, duration, category, time );  
* `SeaRoute` ( <u>id</u>, emailPerson[FK], description, classification, duration, category, time );  
* `NatureRoute` ( <u>id</u>, emailPerson[FK], description, classification, duration, category, time );  
* `UrbanRoute` ( <u>id</u>, emailPerson[FK], description, classification, duration, category, time );  
* `SportRoute` ( <u>id</u>, emailPerson[FK], description, classification, duration, category, time );  
* `Point` ( <u>id</u>, latitude, longitude, );  
`OCC` *Point: latitude, longitude, id*.  
* `SubPath` ( <u>pointId</u>[FK], <u>routeId</u>[FK], order );  
`OCC` *SubPath: pointId, routeId, order*.