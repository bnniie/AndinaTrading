'use client';

import { useEffect, useState } from 'react';

export default function PerfilComisionistaPage() {
  const [comisionista, setComisionista] = useState<any>(null);
  const [mensaje, setMensaje] = useState('');

  useEffect(() => {
    const datos = localStorage.getItem('comisionista');
    if (!datos) {
      setMensaje('❌ No hay sesión activa');
      return;
    }

    try {
      const parsed = JSON.parse(datos);
      setComisionista(parsed);
    } catch (error) {
      console.error(error);
      setMensaje('⚠️ Error al cargar perfil');
    }
  }, []);

  return (
    <main style={{ padding: '2rem' }}>
      <h2>Perfil del Comisionista</h2>
      {mensaje && <p>{mensaje}</p>}
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