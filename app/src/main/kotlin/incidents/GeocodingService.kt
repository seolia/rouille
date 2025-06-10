package incidents

import kotlin.math.*

/** Utility service for geocoding and geohash operations. */
object GeocodingService {
    private const val BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz"

    /** Encode latitude and longitude into a geohash string. */
    fun encode(latitude: Double, longitude: Double, precision: Int = 8): String {
        var latInterval = doubleArrayOf(-90.0, 90.0)
        var lonInterval = doubleArrayOf(-180.0, 180.0)
        var isEven = true
        var bit = 0
        var ch = 0
        val geohash = StringBuilder()

        while (geohash.length < precision) {
            if (isEven) {
                val mid = (lonInterval[0] + lonInterval[1]) / 2
                if (longitude > mid) {
                    ch = ch or (1 shl (4 - bit))
                    lonInterval[0] = mid
                } else {
                    lonInterval[1] = mid
                }
            } else {
                val mid = (latInterval[0] + latInterval[1]) / 2
                if (latitude > mid) {
                    ch = ch or (1 shl (4 - bit))
                    latInterval[0] = mid
                } else {
                    latInterval[1] = mid
                }
            }
            isEven = !isEven
            if (bit < 4) {
                bit++
            } else {
                geohash.append(BASE32[ch])
                bit = 0
                ch = 0
            }
        }
        return geohash.toString()
    }

    /** Decode a geohash into a latitude/longitude pair. */
    fun decode(hash: String): Pair<Double, Double> {
        var latInterval = doubleArrayOf(-90.0, 90.0)
        var lonInterval = doubleArrayOf(-180.0, 180.0)
        var isEven = true

        hash.forEach { c ->
            val cd = BASE32.indexOf(c)
            for (mask in intArrayOf(16, 8, 4, 2, 1)) {
                if (isEven) {
                    refineInterval(lonInterval, cd, mask)
                } else {
                    refineInterval(latInterval, cd, mask)
                }
                isEven = !isEven
            }
        }
        val lat = (latInterval[0] + latInterval[1]) / 2
        val lon = (lonInterval[0] + lonInterval[1]) / 2
        return lat to lon
    }

    private fun refineInterval(interval: DoubleArray, cd: Int, mask: Int) {
        val mid = (interval[0] + interval[1]) / 2
        if ((cd and mask) != 0) {
            interval[0] = mid
        } else {
            interval[1] = mid
        }
    }

    private const val EARTH_RADIUS_KM = 6371.0

    /** Return distance in kilometers between two points. */
    fun distanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS_KM * c
    }
}
