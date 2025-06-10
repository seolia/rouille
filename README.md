# Incident gRPC Server

This project provides a simple Kotlin gRPC server for querying incident data. Incidents are stored in-memory and can later be persisted in a database.

## Building

```bash
./gradlew build
```

## Running

```bash
./gradlew run
```

The server listens on port `9090` and exposes the `IncidentService` gRPC API.
Incidents are indexed by geohash for quick lookup via the built-in `GeocodingService`.

## API

- **GetIncidentsByLocation**: returns incidents for a given latitude and longitude.

The protocol buffers definition is available under `app/src/main/proto/incidents.proto`.
