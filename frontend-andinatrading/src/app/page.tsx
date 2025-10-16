'use client'; // Este componente se ejecuta en el cliente. Es necesario para usar hooks como useRouter y para manejar navegación programática.

import { useRouter } from 'next/navigation'; // Hook para redireccionar al usuario dentro de la aplicación.
import Image from 'next/image'; // Componente optimizado para mostrar imágenes en Next.js.
import styles from './HomePage.module.css'; // Módulo CSS que contiene los estilos específicos para esta página.

export default function HomePage() {
  const router = useRouter(); // Instancia del router para navegación programática.

  return (
    <main className={styles.container}>
      {/* Sección izquierda: presentación visual y mensaje de bienvenida */}
      <section className={styles.left}>
        {/* Logotipo de la aplicación */}
        <Image
          src="/static/img/icon.png"
          alt="AndinaTrading"
          width={80}
          height={80}
          className={styles.logo}
        />

        {/* Título principal de la página */}
        <h1 className={styles.title}>Andina Trading</h1>

        {/* Descripción o eslogan de la plataforma */}
        <p className={styles.description}>
          Donde las oportunidades se convierten en decisiones financieras :3
        </p>
      </section>

      {/* Sección derecha: botones de acción para login y registro */}
      <section className={styles.right}>
        <div className={styles.buttonGroup}>
          {/* Botón para redirigir al formulario de inicio de sesión */}
          <button
            className={styles.actionButton}
            onClick={() => router.push('/login')}
          >
            Iniciar sesión
          </button>

          {/* Botón para redirigir al formulario de registro de inversionistas */}
          <button
            className={styles.actionButton}
            onClick={() => router.push('/inversionistas/registro')}
          >
            Registrarse
          </button>
        </div>
      </section>
    </main>
  );
}
