'use client'; // Este componente se ejecuta en el cliente. Es necesario para acceder a localStorage y usar hooks como useState y useEffect.

import { useEffect, useState } from 'react'; // Hooks para manejar estado local y efectos secundarios.

export default function PerfilComisionistaPage() {
  // Estado que almacena los datos del comisionista obtenidos desde localStorage.
  const [comisionista, setComisionista] = useState<any>(null);

  // Estado para mostrar mensajes de error o advertencia.
  const [mensaje, setMensaje] = useState('');

  // Efecto que se ejecuta una sola vez al montar el componente.
  useEffect(() => {
    // Intenta recuperar los datos del comisionista desde localStorage.
    const datos = localStorage.getItem('comisionista');

    // Si no hay datos, muestra mensaje de sesión inactiva.
    if (!datos) {
      setMensaje('❌ No hay sesión activa');
      return;
    }

    try {
      // Intenta parsear los datos JSON y almacenarlos en el estado.
      const parsed = JSON.parse(datos);
      setComisionista(parsed);
    } catch (error) {
      // Si ocurre un error al parsear, muestra mensaje de error.
      console.error(error);
      setMensaje('⚠️ Error al cargar perfil');
    }
  }, []); // Dependencia vacía: se ejecuta solo una vez al montar.

  return (
    <main style={{ padding: '2rem' }}>
      <h2>Perfil del Comisionista</h2>

      {/* Muestra mensaje si existe (error o advertencia) */}
      {mensaje && <p>{mensaje}</p>}

      {/* Si los datos del comisionista están disponibles, se muestran en pantalla */}
      {comisionista && (
        <div style={{ marginTop: '1rem' }}>
          <p><strong>Nombre:</strong> {comisionista.nombreCompleto}</p>
          <p><strong>Correo:</strong> {comisionista.correo}</p>
          <p><strong>Ciudad:</strong> {comisionista.ciudad}</p>
          <p><strong>País:</strong> {comisionista.pais}</p>
          <p><strong>Usuario:</strong> {comisionista.usuario}</p>
        </div>
      )}
    </main>
  );
}
