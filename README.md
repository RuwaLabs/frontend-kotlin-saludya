# frontend-kotlin-saludya
The Kotlin frontend of SaludYa, designed for patients.

## Identity & Access Management

US-01 implements patient identity verification by DNI, names, last names and birth date against the SaludYa backend. Registration continuation is available only after the backend confirms the identity. Invalid input, identity mismatches and provider outages remain distinct and allow correction or retry.

Implementation and backend configuration: [US-01 — Identity verification](docs/iam-us01.md).
