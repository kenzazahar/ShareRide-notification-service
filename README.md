# ShareRide — Notification Microservice

Part of **ShareRide**, a real-time carpooling platform built as a team project (Group 6) at ENSA Tangier — connecting drivers and passengers with ride matching, live tracking, in-app chat, payments, and instant notifications, on a Spring Boot microservices architecture.

This repository contains the **Notification microservice**, which I designed and built end-to-end as my primary contribution to the project.

## What it does

Listens for events published by the other ShareRide services (new ride matched, driver arriving, message received, payment confirmed) and delivers real-time notifications to users — without any service needing to know who else is listening.

## How it works

- **Event-driven with Apache Kafka** — services publish events to Kafka topics rather than calling each other directly, so the Notification service (and any future service) can react to what happens elsewhere in the platform without tight coupling.
- **Real-time delivery via WebSocket (STOMP)** — once an event is received, the notification is pushed to the relevant connected client immediately, instead of the client polling for updates.
- **Containerized with Docker** — ships with its own `Dockerfile` and a `docker-compose-kafka.yml` to spin up the service alongside a local Kafka broker for development.
- **CI on push** — GitHub Actions workflow runs on every push to catch build issues early.

## Stack

Java · Spring Boot · Apache Kafka · WebSocket (STOMP) · Maven · Docker

## Context

Built between October 2025 and January 2026 as part of a 6-person team project. I owned the Notification service specifically; the wider ShareRide platform (ride matching, chat, payments) was built by teammates as separate services in the same architecture.
