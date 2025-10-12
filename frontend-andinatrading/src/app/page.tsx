'use client';

import { useRouter } from 'next/navigation';

export default function HomePage() {
  const router = useRouter();

  const irARegistro = () => {
    router.push('/inversionistas/registro');
  };

  const irALogin = () => {
    router.push('/login');
  };

  return (
    <main style={{
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      height: '100vh',
      backgroundColor: '#f5f5f5'
    }}>
      <h1 style={{ marginBottom: '2rem' }}>Bienvenido a AndinaTrading</h1>
      <div style={{ display: 'flex', gap: '2rem' }}>
        <button
          onClick={irALogin}
          style={{
            padding: '1rem 2rem',
            fontSize: '1rem',
            backgroundColor: '#0070f3',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
          }}
        >
          Iniciar sesión
        </button>
        <button
          onClick={irARegistro}
          style={{
            padding: '1rem 2rem',
            fontSize: '1rem',
            backgroundColor: '#00c853',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
          }}
        >
          Registrarse
        </button>
      </div>
    </main>
  );
}