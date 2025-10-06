'use client';

import { useState } from 'react';

export default function RegistroPage() {
  const [form, setForm] = useState({
    nombreCompleto: '',
    documentoIdentidad: '',
    correo: '',
    telefono: '',
    ciudad: '',
    pais: '',
    usuario: '',
    contrasena: ''
  });

  const [mensaje, setMensaje] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/registro`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });

      if (response.ok) {
        setMensaje('✅ Registro exitoso');
        setForm({
          nombreCompleto: '',
          documentoIdentidad: '',
          correo: '',
          telefono: '',
          ciudad: '',
          pais: '',
          usuario: '',
          contrasena: ''
        });
      } else if (response.status === 400) {
        setMensaje('❌ Datos inválidos o usuario ya registrado');
      } else {
        setMensaje('⚠️ Error inesperado en el servidor');
      }
    } catch (error) {
      setMensaje('❌ No se pudo conectar con el servidor');
      console.error(error);
    }
  };

  return (
    <main style={{ padding: '2rem' }}>
      <h2>Registro de Inversionista</h2>
      <form onSubmit={handleSubmit}>
        {Object.entries(form).map(([key, value]) => (
          <div key={key} style={{ marginBottom: '1rem' }}>
            <label htmlFor={key}>{key}</label><br />
            <input
              type={key === 'contrasena' ? 'password' : 'text'}
              name={key}
              id={key}
              value={value}
              onChange={handleChange}
              required
              style={{ width: '300px', padding: '0.5rem' }}
            />
          </div>
        ))}
        <button type="submit" style={{ padding: '0.5rem 1rem' }}>Registrar</button>
      </form>
      <p style={{ marginTop: '1rem' }}>{mensaje}</p>
    </main>
  );
}
