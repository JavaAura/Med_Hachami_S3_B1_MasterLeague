# Master League Tournament Manager

A Java desktop application for managing players, teams, and tournaments in the e-sports domain, developed using Java, Spring Core, and Hibernate.

![MasterLeague-screen1](https://github.com/JavaAura/Med_Hachami_S3_B1_MasterLeague/blob/main/screenshots/screen1.png)
![MasterLeague-screen2](https://github.com/JavaAura/Med_Hachami_S3_B1_MasterLeague/blob/main/screenshots/screen2.png)

## Table of Contents

- [Project Overview](#project-overview)
- [Installation](#installation)
- [Structure](#structure)
- [Features](#features)
- [Technologies](#technologies)




## Project Overview

**Context**:  
Master League is designed for an e-sport organization to help organize and monitor video game tournaments. The application manages players, teams, and tournaments efficiently, providing both basic and advanced tournament duration estimations.

**Objectives**:
- Implement player, team, and tournament management.
- Use Spring Core for Dependency Injection (DI) via XML configuration `applicationContext.xml`.
- Implement CRUD operations for `players`, `teams`, and `tournaments`.
- Apply a layered architecture (MVC) to separate presentation, business, and persistence logic.
- Demonstrate the Open/Closed principle by extending the `TournamentDao` interface.

## Installation

### Prerequisites

- Java 8 or higher

### Setup your database:

1. The application automatically creates the required tables in the H2 database when it runs, as configured in `applicationContext.xml`.


### Steps

1. **Clone the repository:**

   ```sh
   https://github.com/JavaAura/Med_Hachami_S3_B1_MasterLeague.git
   cd Med_Hachami_S3_B1_MasterLeague

2. **Build the application:**
   ```sh
   mvn clean install

3. **Compile the application:**
   ```sh
   mvn compile

4. **Deploy:**

- mvn exec:java



## Structure

- **Model Layer**:  
  Defines entities such as `Player`, `Team`, `Tournament`, and `Game` and their relationships using JPA.
  
- **Controller Layer**:  
  Handles user interactions, receives input from the UI, and communicates with the Service layer to process the requests.
  
- **Repository Layer**:  
  Provides an abstraction for data access. This layer interacts with the database using JDBC and Hibernate ORM to handles CRUD operations.
  
- **Service Layer**:  
  Contains business logic and orchestrates operations between the Controller and Repository layers.
  
- **Presentation Layer**:  
JavaFX is used for the UI, utilizing FXML files to create dynamic and reusable layouts.
## Features

1. **Player Management**:
   - Register, update, delete, and view player details.

2. **Team Management**:
   - Create, update, add/remove players, and view team details.

3. **Tournament Management**:
   - Create, update, add/remove teams, and view tournament details.
   - Calculate the estimated duration of tournaments (basic and advanced).

4. **Game Management**:
   - Create, update, add/remove games.



## Technologies

- **Java 8**: Core language used for development.
- **JavaFX**: A framework for building rich client applications (Desktop,Mobile) with a modern user interface, supporting FXML for UI design and CSS for styling.
- **Spring Core**: For Dependency Injection (DI) via XML configuration.
- **Apache Maven**: For dependency management and project build.
- **H2 Database**: In-memory database for development.
- **Hibernate**: ORM for database access and management.
- **JUnit**: For unit testing.
- **Mockito**: For mocking classes to unit test.
- **JaCoCo**: For code coverage.
- **CSS**: For responsive UI design.
- **JIRA**: For project management using Scrum methodology.
- **Git**: For version control with branches.