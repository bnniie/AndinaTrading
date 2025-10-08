'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function RecuperarPage() {
  const router = useRouter();

  const [usuario, setUsuario] = useState('');
  const [nuevaContrasena, setNuevaContrasena] = useState('');
  const [mensaje, setMensaje] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/cambiar-contrasena`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ usuario, nuevaContrasena })
      });

      if (response.ok) {
        setMensaje('✅ Contraseña actualizada correctamente');
      } else if (response.status === 404) {
        setMensaje('❌ Usuario no encontrado');
      } else {
        setMensaje('⚠️ Error al actualizar la contraseña');
      }
    } catch (error) {
      console.error(error);
      setMensaje('❌ No se pudo conectar con el servidor');
    }
  };

  return (
    <main style={{ padding: '2rem' }}>
      <h2>Recuperar contraseña</h2>
      <form onSubmit={handleSubmit}>
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
        <button type="submit" style={{ padding: '0.5rem 1rem' }}>
          Cambiar contraseña
        </button>
      </form>

      {mensaje && <p style={{ marginTop: '1rem' }}>{mensaje}</p>}

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
      > Volver
      </button>
    </main>
  );
}
