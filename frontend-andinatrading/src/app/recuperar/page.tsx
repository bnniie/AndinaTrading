'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import styles from './RestorePage.module.css';

/**
 * Página para restablecer la contraseña de un inversionista.
 * Permite ingresar el usuario, nueva contraseña y confirmarla, con validación y retroalimentación visual.
 */
export default function RecuperarPage() {
  const router = useRouter();

  // Estado para los campos del formulario
  const [credenciales, setCredenciales] = useState({
    usuario: '',
    nuevaContrasena: ''
  });

  // Estado para campo de confirmación
  const [confirmarContrasena, setConfirmarContrasena] = useState('');

  // Estados para mostrar/ocultar contraseñas
  const [mostrarContrasena, setMostrarContrasena] = useState(false);
  const [mostrarConfirmacion, setMostrarConfirmacion] = useState(false);

  // Mensaje de respuesta del servidor
  const [mensaje, setMensaje] = useState('');

  /**
   * Actualiza los campos del formulario.
   */
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCredenciales({ ...credenciales, [e.target.name]: e.target.value });
  };

  /**
   * Envía la solicitud de cambio de contraseña al backend.
   * Valida que ambas contraseñas coincidan antes de enviar.
   */
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
        router.replace('/login');
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

  /**
   * Redirige al usuario a la página de login.
   */
  const volverLogin = () => {
    router.push('/login');
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
        <h1 className={styles.title}>Restablecer contraseña</h1>
        <p className={styles.description}>
          Ingresa tu usuario y confirma tu nueva contraseña
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

          {/* Campo nueva contraseña */}
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

          {/* Campo confirmar contraseña */}
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

          {/* Botones de acción */}
          <div className={styles.buttonRow}>
            <button type="submit" className={styles.actionButton}>Actualizar</button>
            <button type="button" onClick={volverLogin} className={styles.actionButton}>Volver</button>
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