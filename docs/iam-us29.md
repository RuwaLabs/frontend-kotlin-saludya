# US-29: Aviso de sesión expirada

La consulta de perfil comprueba el vencimiento del token antes de enviar una solicitud. Si ya venció, la app bloquea la acción, retira la sesión y muestra una pantalla que requiere volver a iniciar sesión. También reacciona a una respuesta `401` del endpoint protegido, aunque el vencimiento local todavía no se haya alcanzado.

`PatientSessionStore` centraliza el token vigente y el aviso de expiración. El estado expuesto permite retirar el acceso cuando otra operación invalida la sesión. Los módulos que incorporen nuevas solicitudes protegidas deben usar esta comprobación; actualmente se aplica a las acciones de IAM. Un fallo de conexión al consultar el perfil muestra el error y conserva una sesión todavía válida.

La revisión ejecutó 109 pruebas unitarias y HTTP sin fallos. Se generaron el APK Debug y el APK de veinte pruebas de pantalla; Lint terminó sin errores. Las pruebas de pantalla quedaron compiladas y pendientes de ejecución en un dispositivo. La verificación del dominio de recuperación y las pruebas con el backend desplegado siguen pendientes.
