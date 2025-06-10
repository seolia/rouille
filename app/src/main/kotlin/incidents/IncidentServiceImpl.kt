package incidents

import incidents.grpc.IncidentServiceGrpcKt
import incidents.grpc.IncidentList
import incidents.grpc.Position
import incidents.grpc.Incident
import incidents.grpc.Address
import incidents.grpc.IncidentType
import incidents.grpc.Severity
import incidents.grpc.Damage
import com.google.protobuf.Timestamp
import java.time.Instant

/** gRPC service implementation returning incidents by location. */
class IncidentServiceImpl : IncidentServiceGrpcKt.IncidentServiceCoroutineImplBase() {
    override suspend fun getIncidentsByLocation(request: Position): IncidentList {
        val matches = IncidentRepository.findByLocation(request.latitude, request.longitude)
        val protoIncidents = matches.map { it.toProto() }
        return IncidentList.newBuilder().addAllIncidents(protoIncidents).build()
    }
}

/** Convert domain model to protobuf message. */
private fun incidents.Incident.toProto(): Incident = Incident.newBuilder()
    .setUuid(uuid)
    .setType(IncidentType.valueOf(type.name))
    .setOccurrenceTime(Timestamp.newBuilder().setSeconds(occurrenceTime.epochSecond).setNanos(occurrenceTime.nano).build())
    .setAddress(
        Address.newBuilder()
            .setStreet(address.street)
            .setCity(address.city)
            .setState(address.state)
            .setPostalCode(address.postalCode)
            .setFipsCode(address.fipsCode)
            .setCountry(address.country)
            .setLatitude(address.latitude)
            .setLongitude(address.longitude)
            .setGeohash(address.geohash)
            .build()
    )
    .setSeverity(Severity.valueOf(severity.name))
    .setDamage(Damage.valueOf(damage.name))
    .setName(name)
    .build()
