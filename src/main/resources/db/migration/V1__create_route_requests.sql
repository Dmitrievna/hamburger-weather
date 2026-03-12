-- V1__create_route_requests.sql
-- This migration creates the 'route_requests' table to store user route requests, including start and end locations, coordinates, and a timestamp.
CREATE TABLE route_requests (
    id          BIGSERIAL PRIMARY KEY,
    tag         VARCHAR(255),
    start_street  VARCHAR(255),
    start_city    VARCHAR(255),
    start_country VARCHAR(255),
    end_street    VARCHAR(255),
    end_city      VARCHAR(255),
    end_country   VARCHAR(255),
    coordinates   JSONB,
    requested_at  TIMESTAMP
);