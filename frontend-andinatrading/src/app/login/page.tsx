'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import styles from './LoginPage.module.css';

/**
 * Componente de inicio de sesión para inversionistas.
 * Permite ingresar credenciales, validar contra el backend y redirigir al perfil o dashboard.
 */
export default function LoginPage() {
  const router = useRouter();

  // Estado local para almacenar usuario y contraseña
  const [credenciales, setCredenciales] = useState({
    usuario: '',
    contrasena: ''
  });

  // Controla visibilidad de la contraseña
  const [mostrarContrasena, setMostrarContrasena] = useState(false);

  // Mensaje de respuesta del servidor (éxito o error)
  const [mensaje, setMensaje] = useState('');

  /**
   * Actualiza el estado de las credenciales al modificar los campos.
   */
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCredenciales({ ...credenciales, [e.target.name]: e.target.value });
  };

  /**
   * Envía las credenciales al backend y gestiona la respuesta.
   * Si es exitoso, guarda datos en localStorage y redirige al perfil.
   */
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/login`, {
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
      setMensaje('❌ No se pudo conectar con el servidor');
      console.error(error);
    }
  };

  /**
   * Redirige al usuario a la página principal.
   */
  const volverInicio = () => {
    router.push('/');
  };

  return (
    <main className={styles.container}>
      {/* Sección izquierda con branding */}
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
          Ingresa tus credenciales para acceder a tu cuenta
        </p>
      </section>

      {/* Sección derecha con formulario */}
      <section className={styles.right}>
        <form onSubmit={handleSubmit}>
          {/* Campo usuario */}
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

          {/* Campo contraseña con ícono de visibilidad */}
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

          {/* Enlace para login de comisionistas */}
          <div className={styles.linkCentered}>
            ¿Eres comisionista?{' '}
            <a href="/login/comisionista" className={styles.linkAccent}>
              Inicia sesión aquí
            </a>
          </div>

          {/* Enlace para recuperación de contraseña */}
          <p className={styles.linkCentered}>
            <a
              onClick={() => router.push('/recuperar')}
              className={styles.linkAccent}
            >
              ¿Olvidaste tu contraseña?
            </a>
          </p>

          {/* Botones de acción */}
          <div className={styles.buttonRow}>
            <button type="submit" className={styles.actionButton}>Ingresar</button>
            <button type="button" onClick={volverInicio} className={styles.actionButton}>Volver</button>
          </div>

          {/* Mensaje de respuesta */}
          {mensaje && (
            <p className={styles.mensaje}>{mensaje}</p>
          )}
        </form>
      </section>
    </main>
  );
}