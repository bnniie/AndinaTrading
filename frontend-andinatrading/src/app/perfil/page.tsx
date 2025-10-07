'use client';

import { useEffect, useState } from 'react';

export default function PerfilPage() {
  const [inversionista, setInversionista] = useState<any>(null);
  const [mensaje, setMensaje] = useState('');

  useEffect(() => {
    const datosGuardados = localStorage.getItem('inversionista');
    if (!datosGuardados) {
      setMensaje('❌ No hay sesión activa');
      return;
    }

    try {
      const parsed = JSON.parse(datosGuardados);
      setInversionista(parsed);
    } catch (error) {
      console.error('Error al leer datos del perfil:', error);
      setMensaje('⚠️ Error al cargar perfil');
    }
  }, []);

  return (
    <main style={{ padding: '2rem' }}>
      <h2>Perfil del Inversionista</h2>
      {mensaje && <p>{mensaje}</p>}
      {inversionista && (
        <div style={{ marginTop: '1rem' }}>
          <p><strong>Nombre:</strong> {inversionista.nombreCompleto}</p>
          <p><strong>Correo:</strong> {inversionista.correo}</p>
          <p><strong>Ciudad:</strong> {inversionista.ciudad}</p>
          <p><strong>País:</strong> {inversionista.pais}</p>
          <p><strong>Usuario:</strong> {inversionista.usuario}</p>
        </div>
      )}
    </main>
  );
}