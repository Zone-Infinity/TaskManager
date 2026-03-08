# Task Manager (Hibernate ORM)

Backend persistence layer migrated from raw JDBC to **Hibernate ORM**.

## What it does

Same functionality as the CLI version:

- Register users
- Login / Logout
- Create tasks
- List tasks
- Update task status
- Delete tasks

## Stack

- Java
- Hibernate ORM
- PostgreSQL
- Maven
- SLF4J + Logback

## Key Concepts

- Entity mapping (`@Entity`)
- Relationships (`@ManyToOne`, `@OneToMany`)
- HQL queries
- Hibernate sessions and transactions
- Automatic SQL generation

## Architecture

CLI → Service → DAO (Hibernate) → PostgreSQL