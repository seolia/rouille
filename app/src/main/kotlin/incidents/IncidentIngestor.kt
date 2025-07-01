package incidents

import java.io.File
import java.io.InputStream
import java.net.URL
import java.time.Instant

/** Utility to ingest incidents from CSV files or URLs. */
object IncidentIngestor {
    /** Download a file from the given URL to a temporary file. */
    fun download(url: String): File {
        val tmp = File.createTempFile("incidents", ".csv")
        URL(url).openStream().use { input ->
            tmp.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tmp
    }

    /** Ingest incidents from a CSV input stream. */
    fun ingestCsv(stream: InputStream) {
        stream.bufferedReader().useLines { lines ->
            lines.drop(1).forEach { line ->
                val tokens = line.split(',')
                if (tokens.size < 14) return@forEach
                val incident = Incident(
                    uuid = tokens[0],
                    type = IncidentType.valueOf(tokens[1]),
                    occurrenceTime = Instant.parse(tokens[2]),
                    address = Address(
                        street = tokens[3],
                        city = tokens[4],
                        state = tokens[5],
                        postalCode = tokens[6],
                        fipsCode = tokens[7],
                        country = tokens[8],
                        latitude = tokens[9].toDouble(),
                        longitude = tokens[10].toDouble(),
                        geohash = GeocodingService.encode(tokens[9].toDouble(), tokens[10].toDouble())
                    ),
                    severity = Severity.valueOf(tokens[11]),
                    damage = Damage.valueOf(tokens[12]),
                    name = tokens[13]
                )
                IncidentRepository.add(incident)
            }
        }
    }

    /** Ingest incidents from a CSV file path. */
    fun ingestCsv(path: String) {
        val file = File(path)
        if (!file.exists()) return
        file.inputStream().use { ingestCsv(it) }
    }

    /** Download a CSV from URL and ingest its contents. */
    fun ingestFromUrl(url: String) {
        val file = download(url)
        ingestCsv(file.absolutePath)
        file.delete()
    }
}
