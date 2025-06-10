package incidents

/** In-memory repository of incidents. */
object IncidentRepository {
    private val incidents = mutableListOf<Incident>()

    fun add(incident: Incident) {
        incidents += incident
    }

    fun findByLocation(latitude: Double, longitude: Double): List<Incident> {
        val hash = GeocodingService.encode(latitude, longitude)
        val prefix2 = if (hash.length > 2) hash.dropLast(2) else hash
        val prefix3 = if (hash.length > 3) hash.dropLast(3) else hash

        val geohashMatches = incidents.filter {
            it.address.geohash.startsWith(prefix2) || it.address.geohash.startsWith(prefix3)
        }
        if (geohashMatches.isNotEmpty()) return geohashMatches

        return incidents.filter {
            GeocodingService.distanceKm(latitude, longitude, it.address.latitude, it.address.longitude) <= 5.0
        }
    }

    fun all(): List<Incident> = incidents.toList()
}
