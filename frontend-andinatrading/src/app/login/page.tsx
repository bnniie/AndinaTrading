'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function LoginPage() {
  const router = useRouter();

  const [credenciales, setCredenciales] = useState({
    usuario: '',
    contrasena: ''
  });

  const [mensaje, setMensaje] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCredenciales({ ...credenciales, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credenciales)
      });

      if (response.ok) {
        const datos = await response.json();
        localStorage.setItem('usuario', datos.usuario);
        localStorage.setItem('inversionista', JSON.stringify(datos));
        setMensaje('✅ Inicio de sesión exitoso');
        if (datos.dashboard_url) {
    window.location.href = datos.dashboard_url;
  } else {
    router.push('/perfil');
  }
        } else if (response.status === 401) {
        setMensaje('❌ Credenciales incorrectas');
      } else {
        setMensaje('⚠️ Error inesperado en el servidor');
      }
    } catch (error) {
      console.error(error);
      setMensaje('❌ No se pudo conectar con el servidor');
    }
  };

  const volverInicio = () => {
    router.push('/');
  };

  return (
    <main style={{ padding: '2rem' }}>
      <h2>Inicio de sesión</h2>
      <form onSubmit={handleSubmit}>
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

        <p style={{ marginTop: '1rem' }}>
          ¿Eres comisionista?{' '}
          <a href="/login/comisionista" style={{ color: '#0070f3', textDecoration: 'underline' }}>
          Inicia sesión aquí
          </a>
        </p>


        <div style={{ marginTop: '2rem' }}>
          <button type="button" onClick={() => router.push('/recuperar')}
          style={{ background: 'none', border: 'none', color: '#007bff', textDecoration: 'underline', cursor: 'pointer' }}
          > ¿Olvidaste tu contraseña? 
        </button>
        </div>

        <div style={{ display: 'flex', gap: '1rem', marginTop: '1rem' }}>
          <button type="submit" style={{ padding: '0.5rem 1rem' }}>Ingresar</button>
          <button type="button" onClick={volverInicio} style={{ padding: '0.5rem 1rem', backgroundColor: '#ccc' }}>
            Volver
          </button>
        </div>
      </form>
      <p style={{ marginTop: '1rem' }}>{mensaje}</p>
    </main>
  );
}