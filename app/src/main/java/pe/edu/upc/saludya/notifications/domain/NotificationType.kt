package pe.edu.upc.saludya.notifications.domain

/**
 * Kinds of in-app notifications the inbox can render.
 *
 * Only REASSIGNMENT_OFFER is produced today. New kinds (appointments, queue call, check-in)
 * are added here without touching the features that already exist.
 */
enum class NotificationType {
    REASSIGNMENT_OFFER,
    APPOINTMENT_BOOKED,
    APPOINTMENT_CANCELLED,
    PATIENT_CALLED,
    CHECK_IN_COMPLETED,
    GENERAL
}
