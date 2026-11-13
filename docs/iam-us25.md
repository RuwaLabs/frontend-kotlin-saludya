# US-25: Ayuda por pérdida de acceso a la cuenta

El paciente puede solicitar ayuda desde la recuperación si no tiene acceso a su correo ni a su celular. El formulario exige un DNI de ocho dígitos y un correo alternativo válido; los errores se muestran en el campo correspondiente sin enviar una solicitud incompleta.

La app envía `dni` y `contactEmail` a `POST /api/v1/account-recovery-requests`. Una respuesta `202` muestra la recepción de la solicitud y las instrucciones de admisión. La solicitud no abre una sesión ni restablece automáticamente las credenciales: el personal autorizado debe verificar la identidad antes de resolverla. Si falla el servicio, se permite reintentar.

Las pruebas comprueban los formatos, el correo de contacto enviado, los fallos del servicio y que no se duplique una solicitud ya recibida.
