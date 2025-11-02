'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import styles from '../LoginPage.module.css';

export default function LoginComisionistaPage() {
  const router = useRouter();

  const [credenciales, setCredenciales] = useState({
    usuario: '',
    contrasena: ''
  });

  const [mostrarContrasena, setMostrarContrasena] = useState(false);
  const [mensaje, setMensaje] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCredenciales({ ...credenciales, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/comisionistas/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credenciales),
        credentials: 'include'
      });

      let datos;
      try {
        datos = await response.json();
      } catch {
        const textoPlano = await response.text();
        setMensaje(textoPlano);
        return;
      }

      if (response.ok) {
        localStorage.setItem('usuario', datos.usuario);
        localStorage.setItem('comisionista', JSON.stringify(datos));
        setMensaje('✅ Inicio de sesión exitoso');

        if (datos.dashboard_url) {
          window.location.href = datos.dashboard_url;
        } else {
          router.push('/perfil/comisionista');
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
    <main className={styles.container}>
      <section className={styles.left}>
        <Image
          src="/static/img/icon.png"
          alt="AndinaTrading"
          width={80}
          height={80}
          className={styles.logo}
        />
        <h1 className={styles.title}>Inicio de sesión</h1>
        <p className={styles.description}>
          Accede como comisionista para gestionar tus operaciones
        </p>
      </section>

      <section className={styles.right}>
        <form onSubmit={handleSubmit}>
          <div className={styles.formGroup}>
            <input
              type="text"
              name="usuario"
              placeholder="Usuario"
              value={credenciales.usuario}
              onChange={handleChange}
              required
              className={styles.input}
            />
          </div>

          <div className={styles.formGroup}>
            <div className={styles.passwordWrapper}>
              <input
                type={mostrarContrasena ? 'text' : 'password'}
                name="contrasena"
                placeholder="Contraseña"
                value={credenciales.contrasena}
                onChange={handleChange}
                required
                className={styles.input}
              />
              <span
                className={styles.eyeIcon}
                onClick={() => setMostrarContrasena(!mostrarContrasena)}
              >
                👁
              </span>
            </div>
          </div>

          <div className={styles.buttonRow}>
            <button type="submit" className={styles.actionButton}>Ingresar</button>
            <button type="button" onClick={volverInicio} className={styles.actionButton}>Volver</button>
          </div>

          {mensaje && (
            <p className={styles.mensaje}>{mensaje}</p>
          )}
        </form>
      </section>
    </main>
  );
}