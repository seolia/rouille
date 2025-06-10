package incidents

import java.time.Instant

/** Address with geolocation coordinates. */
data class Address(
    val street: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val fipsCode: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val geohash: String = ""
)

enum class IncidentType { TRAFFIC, CRIME, EMERGENCY }

enum class Severity { MINOR, MODERATE, MAJOR, CRITICAL }

enum class Damage { INJURY, FATALITY, PROPERTY }

/** Domain model for an incident. */
data class Incident(
    val uuid: String,
    val type: IncidentType,
    val occurrenceTime: Instant,
    val address: Address,
    val severity: Severity,
    val damage: Damage,
    val name: String = ""
)
