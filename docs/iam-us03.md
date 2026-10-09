# US-03: Verificación de correo y creación de cuenta

El paciente ingresa el código de seis dígitos que recibe en su correo. Se mantiene el cambio del equipo a correo; esta implementación no utiliza SMS.

La cuenta se crea con `POST /api/v1/user-accounts`, enviando la identidad, las credenciales y el campo `code`. Solo una respuesta `201` con los identificadores del paciente y de la cuenta habilita el acceso al inicio de sesión. El envío del correo de bienvenida corresponde al evento de registro del backend.

Un código incorrecto mantiene el registro pendiente y permite corregirlo. También se muestran los errores de vencimiento y límite de intentos. El reenvío usa `POST /api/v1/user-accounts/send-verification-code` con el mismo correo y borra el código anterior del formulario; su invalidación se realiza en el servidor. Las contraseñas y el código se borran del estado al finalizar el registro.

El contrato corresponde a `develop` del backend, revisado el 7 de octubre de 2026. Las pruebas HTTP usan un servidor de pruebas; falta comprobar la entrega de correos con el backend desplegado.
