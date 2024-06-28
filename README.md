# Filters Task

## Modal dialog functional requirements

- [x] (+) button creates new line of filtering criteria
- [x] (-) button deletes criteria
- [x] Filter should contain at least one criteria
- [x] There can be 3 types of criteria:
  - [x] Amount with number comparing conditions
  - [x] Title with text comparing conditions
  - [x] Date with date comparing conditions, date choice can be represented by date picker or 3 combo boxes
- [x] Each added criteria default as “Amount” type
- [ ] Comparing conditions should correspond to selected criteria type
- [x] Filter dialog should have fixed size and be configurable to operate in modal/non-modal mode
- [x] Let the user resize only the height
- [x] If there are more rows than dialog can fit, scrolling should appear
- [x] In non-modal mode user by clicking “Add Filter” button sees filter dialog as part of pag

## General Rundown of the Stack
- Java 21
- Spring Boot 3
- Node 22
- Angular 18
- H2

## How to run the project
- Install docker and docker-compose if you haven't already
- Run `docker-compose build` in the root directory
- Run `docker-compose up` in the root directory
- Open your browser and go to `localhost:4200`

## BE Endpoint
`localhost:8080/api/v1/filters`

## Swagger

#### The swagger documentation can be found at `http://localhost:8080/api/v1/swagger-ui/index.html`
