# JustEat Single-Page Application (SPA)

## What is this

This project is a migration of the web app that can be found in [JustEat-web-app](https://github.com/xFranMe/JustEat-web-app) repository to a Single-Page Application or SPA. The former one was implemented using servlets and JSPs, which has been migrated to AngularJS. To achieve this, an API REST has been developed by making use of JAX-RS.

The functionality offered to the end-user remains the same:

* **Users**: a profile account is required, so sign-up and log-in functions are available. Users can be created, edited and deleted.
* **Restaurants**: users can create, edit and delete their own restaurant pages. Meanwhile, they can only visit restaurants that are owned by other users. 
* **Dishes**: users can create, edit and delete dishes for the restaurants they own. This way, each restaurant will have a list of dishes attached.
* **Orders**: any user can add any dish from any restaurant to their shopping cart (order). Once done, the order can be submited, being linked to their user profile.
* **Reviews**: retaurants can receive reviews from any user, including a grade and a comment. These reviews can't be edited or deleted.
* **Search**: restaurants can be found by their name, category, location or description.

## Scope

This project has been developed, as well, to work locally by using a local server (Tomcat) and database (SQLite).

## About documentation

:warning: The rest of the documentation is written in Spanish.

External documentation can be found in [Doc_JustEat_SPA.pdf](https://github.com/xFranMe/JustEat-SPA/blob/main/Doc_JustEat_SPA.pdf) (within this repository). This covers every relevant aspect of this SPA's design and migration.
