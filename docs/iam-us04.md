# US-04: Inicio de sesión del paciente

El paciente ingresa correo y contraseña. `POST /api/v1/user-accounts/login` valida las credenciales y devuelve un desafío de acceso por correo. La app solicita el código y completa la sesión mediante `POST /api/v1/user-accounts/login/verify`. Este segundo paso forma parte del contrato actual de `develop`.

Las credenciales inválidas muestran un único mensaje. No se indica si falló el correo o la contraseña. La app solo acepta sesiones con rol `PATIENT`, identificadores válidos y token vigente; no ofrece un selector de rol. La contraseña se borra al recibir la respuesta de inicio de sesión y el token se conserva en memoria, sin guardarlo en disco.

La sesión abre una pantalla de cuenta, disponible para integrar después con el inicio del paciente. “Ver mi perfil” consulta los datos del paciente con el token vigente. El cierre requiere confirmación y solicita la revocación al servidor. El vencimiento retira el acceso y muestra el aviso de US-29.

La pantalla incluye el aviso de cuenta inactiva y consulta con admisión cuando el servidor devuelve `IAM_ACCOUNT_INACTIVE` o `IAM_INACTIVE_ACCOUNT`. Actualmente, el backend devuelve `IAM_INVALID_CREDENTIALS` tanto para credenciales incorrectas como para cuentas inactivas; con esa respuesta no es posible distinguir el tercer escenario de la historia. La entrega real del código y ese caso requieren comprobación con el backend del equipo.

La verificación actual de IAM se registra en [US-29](iam-us29.md). Las pruebas de pantalla requieren ejecución en un dispositivo; la entrega real de los correos se comprueba con el backend desplegado.
