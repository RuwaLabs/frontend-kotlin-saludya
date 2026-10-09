# US-28: Cierre de sesión del paciente

“Cerrar sesión” abre una confirmación. Cancelar o cerrar el diálogo conserva la sesión y no envía ninguna revocación. Al confirmar, la app llama a `POST /api/v1/user-accounts/logout` con el token Bearer y elimina el acceso local.

Una respuesta `204` confirma la revocación; `401` indica que la sesión ya no era válida. Si no se puede confirmar el cierre en el servidor, la app mantiene el cierre local y muestra ese aviso, sin afirmar que el token fue revocado remotamente.

Las pruebas verifican que cancelar conserva el token, que confirmar solicita una sola revocación y que el cierre local funciona sin conexión.
