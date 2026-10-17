# ACCOMO — Hotel Reservation System

A full-stack hotel reservation web application built to support hotel discovery, room selection, reservation management, and payment workflows. The project modernizes a legacy Java web application with a React and TypeScript frontend while preserving the existing Java Servlet and MySQL backend.

> **Project type:** Academic team project  
> **My contribution:** Full-stack development and database integration  
> **Status:** Core application implemented; deployment and some integration details may require environment-specific configuration.

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Integration](#api-integration)
- [Database](#database)
- [Project Structure](#project-structure)
- [Security and Reliability Notes](#security-and-reliability-notes)
- [Roadmap](#roadmap)
- [Author Contribution](#author-contribution)

## Overview

ACCOMO is a hotel reservation system designed to make the booking journey easier for customers. Users can explore hotels, review hotel details, filter and sort available options, enter stay details, and manage reservation-related workflows.

The application combines a modern React interface with a Java Servlet/JDBC backend and MySQL persistence. The migration approach is intended to introduce JSON API endpoints for the React client while keeping the existing JSP-based workflows available during the transition.

## Key Features

- **Hotel discovery:** Browse hotel listings and view hotel information and images.
- **Search and filtering:** Filter hotels by category and price, with sorting options.
- **Hotel details:** View details before starting a reservation.
- **Reservation flow:** Enter check-in/check-out dates, guest and room details, and special requests.
- **Price calculation:** Display reservation pricing based on selected booking details.
- **Authentication:** Customer sign-up and login workflows.
- **Reservation management:** Reservation confirmation, history, and cancellation workflows.
- **Payment workflow:** Payment flow designed to support UPI, QR-code, and card options.
- **Legacy compatibility:** Preserve existing JSP/form behavior while adding JSON endpoints for the React frontend.

*Feature availability can depend on the current backend integration and local configuration. This README does not claim that the application is publicly deployed.*

## Technology Stack

| Layer | Technologies |
|---|---|
| Frontend | React 18, TypeScript, Vite, Tailwind CSS, React Router |
| Backend | Java, Jakarta/Java EE Servlets, JavaBeans, JDBC |
| Database | MySQL |
| Application server | Apache Tomcat |
| Development tools | Visual Studio Code, Git, GitHub |

## Architecture

```text
React + TypeScript + Tailwind CSS
                |
                | HTTP requests / JSON
                v
       Java Servlet API layer
                |
                v
         DAO / JDBC layer
                |
                v
             MySQL
```

The frontend is intended to communicate with JSON API servlets under `/api/*`. The backend continues to use the existing servlet, DAO, JavaBean, and database layers. This incremental approach aims to reduce disruption to existing JSP functionality while the frontend is modernized.

## Getting Started

### Prerequisites

Install and configure the following:

- Java Development Kit (JDK)
- Apache Tomcat compatible with the project's servlet APIs
- MySQL Server
- Node.js and npm
- Git

Exact versions and configuration may vary with the checked-out project revision.

### 1. Clone the repository

```bash
git clone <YOUR_REPOSITORY_URL>
cd <YOUR_REPOSITORY_DIRECTORY>
```

Replace the placeholders with the actual GitHub repository URL and directory name.

### 2. Configure the database

1. Start MySQL.
2. Create the application database (the development database has been named `hotel`).
3. Import the SQL schema or backup available with your project, if included.
4. Configure the database connection settings used by the Java application.
5. Ensure the database user has only the permissions the application needs.

Do not commit real database passwords or production credentials.

### 3. Start the Java backend

1. Open the Java web application in your IDE.
2. Check the JDBC driver and database connection configuration.
3. Build the web application and deploy the generated WAR or web application directory to the configured Tomcat instance.
4. Start Tomcat and check its logs for deployment or database connection errors.

The exact build and deployment commands depend on the repository's current build configuration.

### 4. Start the React frontend

Open a terminal in the frontend directory:

```bash
npm install
npm run dev
```

Vite prints the local development URL in the terminal. Open that URL in your browser.

If the frontend requires an API base URL, configure it using the environment variable expected by the frontend source. Do not assume a variable name without checking the project configuration.

## Configuration

Before running the application, verify:

- MySQL is running and the configured port matches your local installation.
- The database name, username, and password match your local MySQL configuration.
- Tomcat is using a compatible servlet API version.
- The frontend's API base URL points to the backend.
- Cross-origin requests and session cookies are configured correctly if the frontend and backend run on different origins.

Use local environment files or server-side configuration for secrets. Keep `.env` files containing real credentials out of Git.

## API Integration

The modernization plan uses JSON endpoints under `/api/*` while retaining legacy JSP/form workflows. The intended API areas include:

| API area | Purpose |
|---|---|
| Hotels | Hotel listings, details, filtering, and sorting |
| Authentication | Customer registration and login |
| Reservations | Availability checks, booking submission, confirmation, history, and cancellation |
| Payments | Payment-related workflow |

Endpoint paths, HTTP methods, request schemas, and response formats should be verified against the current servlet implementation before being documented as a stable public API.

## Database

The application uses MySQL for customer, hotel, reservation, and payment-related data. The project has used tables including `customers`, `reservations`, `reservation1`, and `payments`; confirm the schema in the current database scripts before applying migrations.

For local development:

- Use a dedicated development database.
- Keep database credentials out of source control.
- Back up the database before schema changes.
- Use parameterized JDBC statements for user-supplied values.
- Avoid putting real customer information into screenshots, sample data, or commits.

## Project Structure

The repository combines a React frontend with a Java web application. Directory names may differ by branch; a typical logical layout is:

```text
ACCOMO/
├── frontend/          # React + TypeScript client
├── src/               # Java web application source
│   ├── servlets/      # HTTP / API request handling
│   ├── dao/           # Database access
│   └── beans/         # JavaBeans / data models
├── webapp/            # JSP pages and web resources
├── database/          # SQL scripts, if included
└── README.md
```

Treat this as a conceptual layout rather than a guaranteed exact directory tree; adjust it to match the repository's actual files.

## Security and Reliability Notes

- Never commit passwords, database credentials, session secrets, API keys, or private environment files.
- Hash passwords using a suitable password-hashing algorithm; never store plaintext passwords.
- Validate request data on the server, even when the frontend also validates it.
- Use prepared statements for database operations.
- Enforce authorization on reservation history, cancellation, and payment-related endpoints.
- Configure session cookies and CORS carefully when frontend and backend origins differ.
- Test payment flows with sandbox/test credentials only; do not claim real payment processing unless it is implemented and verified.

## Roadmap

- Complete and verify the React-to-Servlet API integration.
- Validate all hotel search, booking, authentication, and reservation-management flows end to end.
- Improve API documentation with exact endpoints and example payloads.
- Add automated tests for core reservation and authentication workflows.
- Add screenshots or a short demo once the application is running reliably.
- Document reproducible build and deployment steps.

## Author Contribution

This was developed as an academic team project. My contribution focused on **full-stack development and database integration**, including work across the React/TypeScript interface and the Java/MySQL application layers.

### Suggested resume entry

**ACCOMO — Hotel Reservation System** | React, TypeScript, Tailwind CSS, Java Servlets, JDBC, MySQL, Tomcat

- Developed a full-stack hotel reservation application with hotel browsing, filtering, hotel details, and reservation workflows.
- Worked across the React/TypeScript frontend and Java Servlet/JDBC backend, integrating application data with MySQL.
- Supported an incremental modernization approach by introducing JSON API endpoints while preserving legacy JSP workflows.

---

## Screenshots and Demo

Add screenshots here after capturing them from a working local build. Only include features that have been tested in the current version.

```text
docs/
└── screenshots/
    ├── home.png
    ├── hotel-listing.png
    └── reservation-flow.png
```

If a public demo is available later, add its verified URL here.
