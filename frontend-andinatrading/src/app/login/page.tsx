'use client'; // Este componente se ejecuta en el cliente. Es necesario para usar hooks como useState y useRouter.

import { useState } from 'react'; // Hook para manejar el estado local de las credenciales y mensajes.
import { useRouter } from 'next/navigation'; // Hook para redireccionar al usuario tras el login.

export default function LoginPage() {
  const router = useRouter(); // Instancia del router para navegación programática.

  // Estado local para almacenar las credenciales ingresadas por el usuario.
  const [credenciales, setCredenciales] = useState({
    usuario: '',
    contrasena: ''
  });

  // Estado para mostrar mensajes de éxito o error tras el intento de login.
  const [mensaje, setMensaje] = useState('');

  // Maneja los cambios en los campos del formulario.
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCredenciales({ ...credenciales, [e.target.name]: e.target.value });
  };

  // Envía las credenciales al backend y gestiona la respuesta.
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault(); // Previene el comportamiento por defecto del formulario.

    try {
      // Realiza una solicitud POST al endpoint de login de inversionistas.
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credenciales)
      });

      if (response.ok) {
        // Si el login es exitoso, guarda los datos en localStorage.
        const datos = await response.json();
        localStorage.setItem('usuario', datos.usuario);
        localStorage.setItem('inversionista', JSON.stringify(datos));
        setMensaje('✅ Inicio de sesión exitoso');

        // Redirige al dashboard si se proporciona una URL personalizada.
        if (datos.dashboard_url) {
          window.location.href = datos.dashboard_url;
        } else {
          router.push('/perfil');
        }
      } else if (response.status === 401) {
        // Si las credenciales son incorrectas.
        setMensaje('❌ Credenciales incorrectas');
      } else {
        // Para otros errores del servidor.
        setMensaje('⚠️ Error inesperado en el servidor');
      }
    } catch (error) {
      // En caso de error de red o conexión.
      console.error(error);
      setMensaje('❌ No se pudo conectar con el servidor');
    }
  };

  // Redirige al usuario a la página principal.
  const volverInicio = () => {
    router.push('/');
  };

  return (
    <main style={{ padding: '2rem' }}>
      <h2>Inicio de sesión</h2>

      {/* Formulario de login */}
      <form onSubmit={handleSubmit}>
        {/* Campo: Usuario */}
        <div style={{ marginBottom: '1rem' }}>
          <label htmlFor="usuario">Usuario</label><br />
          <input
            type="text"
            name="usuario"
            id="usuario"
            value={credenciales.usuario}
            onChange={handleChange}
            required
            style={{ width: '300px', padding: '0.5rem' }}
          />
        </div>

        {/* Campo: Contraseña */}
        <div style={{ marginBottom: '1rem' }}>
          <label htmlFor="contrasena">Contraseña</label><br />
          <input
            type="password"
            name="contrasena"
            id="contrasena"
            value={credenciales.contrasena}
            onChange={handleChange}
            required
            style={{ width: '300px', padding: '0.5rem' }}
          />
        </div>

        {/* Enlace para comisionistas */}
        <p style={{ marginTop: '1rem' }}>
          ¿Eres comisionista?{' '}
          <a href="/login/comisionista" style={{ color: '#0070f3', textDecoration: 'underline' }}>
            Inicia sesión aquí
          </a>
        </p>

        {/* Enlace para recuperación de contraseña */}
        <div style={{ marginTop: '2rem' }}>
          <button
            type="button"
            onClick={() => router.push('/recuperar')}
            style={{
              background: 'none',
              border: 'none',
              color: '#007bff',
              textDecoration: 'underline',
              cursor: 'pointer'
            }}
          >
            ¿Olvidaste tu contraseña?
          </button>
        </div>

        {/* Botones de acción: Ingresar y Volver */}
        <div style={{ display: 'flex', gap: '1rem', marginTop: '1rem' }}>
          <button type="submit" style={{ padding: '0.5rem 1rem' }}>Ingresar</button>
          <button
            type="button"
            onClick={volverInicio}
            style={{ padding: '0.5rem 1rem', backgroundColor: '#ccc' }}
          >
            Volver
          </button>
        </div>
      </form>

      {/* Mensaje de estado tras el intento de login */}
      <p style={{ marginTop: '1rem' }}>{mensaje}</p>
    </main>
  );
}
