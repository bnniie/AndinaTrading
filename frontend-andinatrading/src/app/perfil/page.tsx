'use client'; // Este componente se ejecuta en el cliente. Es necesario para acceder a localStorage y usar hooks como useState y useEffect.

import { useEffect, useState } from 'react'; // Hooks para manejar estado local y efectos secundarios.

export default function PerfilPage() {
  // Estado que almacena los datos del inversionista obtenidos desde localStorage.
  const [inversionista, setInversionista] = useState<any>(null);

  // Estado para mostrar mensajes de error o advertencia.
  const [mensaje, setMensaje] = useState('');

  // Efecto que se ejecuta una sola vez al montar el componente.
  useEffect(() => {
    // Intenta recuperar los datos del inversionista desde localStorage.
    const datosGuardados = localStorage.getItem('inversionista');

    // Si no hay datos, muestra mensaje de sesión inactiva.
    if (!datosGuardados) {
      setMensaje('❌ No hay sesión activa');
      return;
    }

    try {
      // Intenta parsear los datos JSON y almacenarlos en el estado.
      const parsed = JSON.parse(datosGuardados);
      setInversionista(parsed);
    } catch (error) {
      // Si ocurre un error al parsear, muestra mensaje de error.
      console.error('Error al leer datos del perfil:', error);
      setMensaje('⚠️ Error al cargar perfil');
    }
  }, []); // Dependencia vacía: se ejecuta solo una vez al montar.

  return (
    <main style={{ padding: '2rem' }}>
      <h2>Perfil del Inversionista</h2>

      {/* Muestra mensaje si existe (error o advertencia) */}
      {mensaje && <p>{mensaje}</p>}

      {/* Si los datos del inversionista están disponibles, se muestran en pantalla */}
      {inversionista && (
        <div style={{ marginTop: '1rem' }}>
          <p><strong>Nombre:</strong> {inversionista.nombreCompleto}</p>
          <p><strong>Correo:</strong> {inversionista.correo}</p>
          <p><strong>Ciudad:</strong> {inversionista.ciudad}</p>
          <p><strong>País:</strong> {inversionista.pais}</p>
          <p><strong>Usuario:</strong> {inversionista.usuario}</p>
        </div>
      )}

      {/* Botón para cerrar sesión: elimina datos del localStorage y redirige al inicio */}
      <button
        onClick={() => {
          localStorage.removeItem('usuario');
          localStorage.removeItem('inversionista');
          window.location.href = '/';
        }}
        style={{
          marginTop: '2rem',
          padding: '0.5rem 1rem',
          backgroundColor: '#f44336',
          color: 'white',
          border: 'none',
          cursor: 'pointer'
        }}
      >
        Cerrar sesión
      </button>
    </main>
  );
}
