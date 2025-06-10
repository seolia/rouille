package incidents

import java.time.Instant
import java.util.UUID

// For generating geohash values
import incidents.GeocodingService

/**
 * Populates the repository with a few example incidents.
 */
fun loadSampleData() {
    val address = Address(
        street = "1 Main St",
        city = "Metropolis",
        state = "NY",
        postalCode = "12345",
        fipsCode = "36061",
        country = "US",
        latitude = 40.0,
        longitude = -74.0,
        geohash = GeocodingService.encode(40.0, -74.0)
    )

    IncidentRepository.add(
        Incident(
            uuid = UUID.randomUUID().toString(),
            type = IncidentType.TRAFFIC,
            occurrenceTime = Instant.now(),
            address = address,
            severity = Severity.MINOR,
            damage = Damage.PROPERTY,
            name = "Traffic at Main St"
        )
    )
}
