# INFO4 CPOO project

This is the base project your team **MUST** FORK to implement its version of the instant messaging app.

## Server

The server is a Spring Boot app that must be located in the /server directory.
An embryo allowing to receive an send messages to other domains is provided.

## API and end-to-end tests

The /api/serverapi.yml file must contain the OpenAPI specification of the server API.
The /api/e2etests must contains Java end-to-end test code of the server API (generated using OpenAPI code generator).

## Client

The client is an Angular Web app that must be located directly under the /client directory.

## Mockups

The "static" mockup of the UI (images, HTML files...) must be delivered in the /client/mockups directory.

The "actionable" mockup must be delivered directly under the /client directory as an angular app embryo.

## Router

The message router is a Spring Boot app located in the /router directory.
You server will use it to send and receive messages.
It is deployed at https://cpoo-router.mightycode.tech/.

**This code is provided for information, you don't actually need it to implement your the project.**
