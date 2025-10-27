'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import styles from './RestorePage.module.css';

export default function RecuperarPage() {
  const router = useRouter();

  const [credenciales, setCredenciales] = useState({
    usuario: '',
    nuevaContrasena: ''
  });

  const [confirmarContrasena, setConfirmarContrasena] = useState('');
  const [mostrarContrasena, setMostrarContrasena] = useState(false);
  const [mostrarConfirmacion, setMostrarConfirmacion] = useState(false);
  const [mensaje, setMensaje] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCredenciales({ ...credenciales, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (credenciales.nuevaContrasena !== confirmarContrasena) {
      setMensaje('❌ Las contraseñas no coinciden');
      return;
    }

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/cambiar-contrasena`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credenciales)
      });

      if (response.ok) {
        setMensaje('✅ Contraseña actualizada');
        setCredenciales({ usuario: '', nuevaContrasena: '' });
        setConfirmarContrasena('');
        router.replace('/login')
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

  const volverLogin = () => {
    router.push('/login');
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
        <h1 className={styles.title}>Restablecer contraseña</h1>
        <p className={styles.description}>
          Ingresa tu usuario y confirma tu nueva contraseña
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
                name="nuevaContrasena"
                placeholder="Nueva contraseña"
                value={credenciales.nuevaContrasena}
                onChange={handleChange}
                required
                minLength={6}
                maxLength={20}
                pattern="^(?=.*[A-Za-z])(?=.*\d)(?=.*[^A-Za-z\d]).{6,20}$"
                className={styles.input}
              />
              <span
                className={styles.eyeIcon}
                onClick={() => setMostrarContrasena(!mostrarContrasena)}
              >
                👁
              </span>
            </div>
            <small className={styles.helper}>
              Debe tener entre 6 y 20 caracteres con letra, número y símbolo
            </small>
          </div>

          <div className={styles.formGroup}>
            <div className={styles.passwordWrapper}>
              <input
                type={mostrarConfirmacion ? 'text' : 'password'}
                name="confirmarContrasena"
                placeholder="Confirmar contraseña"
                value={confirmarContrasena}
                onChange={(e) => setConfirmarContrasena(e.target.value)}
                required
                className={styles.input}
              />
              <span
                className={styles.eyeIcon}
                onClick={() => setMostrarConfirmacion(!mostrarConfirmacion)}
              >
                👁
              </span>
            </div>
            <small className={styles.helper}>
              Debe coincidir con la nueva contraseña
            </small>
          </div>

          <div className={styles.buttonRow}>
            <button type="submit" className={styles.actionButton}>Actualizar</button>
            <button type="button" onClick={volverLogin} className={styles.actionButton}>Volver</button>
          </div>

          {mensaje && (
            <p className={styles.mensaje}>{mensaje}</p>
          )}
        </form>
      </section>
    </main>
  );
}