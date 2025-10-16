'use client'; // Este componente se ejecuta en el cliente. Es necesario para usar hooks como useState y useRouter, y para acceder a localStorage o realizar navegación programática.

import { useState } from 'react'; // Hook para manejar el estado local de los campos y mensajes.
import { useRouter } from 'next/navigation'; // Hook para redireccionar al usuario tras la acción.

export default function RecuperarPage() {
  const router = useRouter(); // Instancia del router para navegación programática.

  // Estado para almacenar el nombre de usuario ingresado.
  const [usuario, setUsuario] = useState('');

  // Estado para almacenar la nueva contraseña ingresada.
  const [nuevaContrasena, setNuevaContrasena] = useState('');

  // Estado para mostrar mensajes de éxito o error tras el intento de recuperación.
  const [mensaje, setMensaje] = useState('');

  // Maneja el envío del formulario y realiza la solicitud al backend.
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault(); // Previene el comportamiento por defecto del formulario.

    try {
      // Realiza una solicitud PUT al endpoint de cambio de contraseña.
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/cambiar-contrasena`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ usuario, nuevaContrasena })
      });

      // Manejo de respuestas según el estado HTTP.
      if (response.ok) {
        setMensaje('✅ Contraseña actualizada correctamente');
      } else if (response.status === 404) {
        setMensaje('❌ Usuario no encontrado');
      } else {
        setMensaje('⚠️ Error al actualizar la contraseña');
      }
    } catch (error) {
      // Manejo de errores de red o conexión.
      console.error(error);
      setMensaje('❌ No se pudo conectar con el servidor');
    }
  };

  return (
    <main style={{ padding: '2rem' }}>
      <h2>Recuperar contraseña</h2>

      {/* Formulario para ingresar usuario y nueva contraseña */}
      <form onSubmit={handleSubmit}>
        {/* Campo: Usuario */}
        <div style={{ marginBottom: '1rem' }}>
          <label htmlFor="usuario">Usuario</label><br />
          <input
            type="text"
            id="usuario"
            value={usuario}
            onChange={(e) => setUsuario(e.target.value)}
            required
            style={{ width: '300px', padding: '0.5rem' }}
          />
        </div>

        {/* Campo: Nueva contraseña */}
        <div style={{ marginBottom: '1rem' }}>
          <label htmlFor="nuevaContrasena">Nueva contraseña</label><br />
          <input
            type="password"
            id="nuevaContrasena"
            value={nuevaContrasena}
            onChange={(e) => setNuevaContrasena(e.target.value)}
            required
            style={{ width: '300px', padding: '0.5rem' }}
          />
        </div>

        {/* Botón para enviar el formulario */}
        <button type="submit" style={{ padding: '0.5rem 1rem' }}>
          Cambiar contraseña
        </button>
      </form>

      {/* Mensaje de estado tras el intento de recuperación */}
      {mensaje && <p style={{ marginTop: '1rem' }}>{mensaje}</p>}

      {/* Botón para volver al login */}
      <button
        type="button"
        onClick={() => router.push('/login')}
        style={{
          marginTop: '1rem',
          padding: '0.5rem 1rem',
          backgroundColor: '#ccc',
          border: 'none',
          cursor: 'pointer'
        }}
      >
        Volver
      </button>
    </main>
  );
}
