package incidents

import org.yaml.snakeyaml.Yaml
import java.io.InputStream

/** Configuration parsed from application.yaml. */
data class Config(
    val incident: IncidentSection = IncidentSection()
) {
    data class IncidentSection(var sourceUrl: String? = null)

    companion object {
        fun load(stream: InputStream): Config {
            val yaml = Yaml()
            val map = yaml.load<Map<String, Any?>>(stream) ?: emptyMap()
            val incidentMap = map["incident"] as? Map<*, *> ?: emptyMap<Any, Any>()
            val url = incidentMap["sourceUrl"] as? String
            return Config(IncidentSection(url))
        }

        fun loadDefault(): Config {
            val resource = Config::class.java.getResourceAsStream("/application.yaml")
            return if (resource != null) {
                resource.use { load(it) }
            } else Config()
        }
    }
}
