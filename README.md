# Social Routing
The project consists of a system that provides the ability to define and share touristic pedestrian routes. It allows area exploration by using user made routes as virtual tour guides to other users. It has the following functionalities:

* Route creation.

* Ability to search routes.

* Route live tracking.

* Route updates.

* Route characterization.


This information is from a project proposal and as such is incomplete. More accurate information can be found [here](https://github.com/baltasarb/social-routing/wiki).

## Table of Contents
* [Analysis](#analysis)
    * [Use Case](#use-case)
    * [Challenges](#challenges)
* [System Structure](#system-structure)
    * [Client](#client)
    * [Social Routing Service](#social-routing-service)
    * [External APIs](#external-apis)
* [Timeline](#timeline)

## Analysis

### Use case

In the context of the application, a route is a path from point A to point B, that goes through user selected sub paths that might either have relevance or simply provide the fastest way to the next point of interest of that route.

An example of events when using the application might be:

A user at his hotel decides he wants to go sightseeing for an hour and check the surrounding area by foot.

* The user starts the application and searches for a route inserting his location and time available to spend on a route.

* The application suggests the top 3 routes available according to proximity to the user starting point, route evaluation and time necessary to complete the given route.

* The user selects the route and is shown the directions in real time on a map that he has to follow to undergo such route until it is done.

* The user finishes the route and evaluates it, with the possibility of adding a suggestion to it.

### Challenges

Considering the geolocation focused nature of the project there are some challenges to overcome:

* Characterization of a route: the metadata of a route which may contain elements such as travel time, ground elevation, ground type, age range, category, best time of day to transverse amongst others.

* Route creation, storage and visual representation: on the front-end side, how a route is represented and its creation made available to a user and on the back-end side the way that the information regarding the route is saved.

* Route searching: given search parameters each user should have the best possible route suggested to him.

* User authentication: using an external service.

* Live tracking: the user should be able to follow the route in real time, on his device.

* Path to the route and its ordering: given a starting location, a path to the beginning of a route must be generated, taking the route’s ordering into consideration.

* External APIs comprehension and usage.

## System Structure
The project will be developed in two major components and a third exterior one, communicating with each other, separating concerns and business logic.

``FALTA IMAGEM COM SISTEMA``

### Client
The project will have a mobile component which consists of an Android application. This is the component that a user will interact with and is dependent of other internal and external services.

### Social Routing Service

The system database and server are located in this component and together they hold the business logic of the system. This logic is exposed to the outside components through an API.

### External APIs
This component encompasses the external elements of the system, already created by a third party, to provide a faster project development and to allow functionality extension instead of functionality recreation.

## Timeline
`FALTA IMAGEM COM TIMELINE`