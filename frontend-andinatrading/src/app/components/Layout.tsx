'use client';

import { ReactNode } from 'react';
import styles from './LayoutPage.module.css';
import {
  Axis3d,
  FilePen,
  UsersRound,
  HandHeart,
  Undo2
} from 'lucide-react';

/**
 * Componente de layout principal para páginas protegidas del sistema.
 * Incluye un sidebar con navegación icónica y un panel principal para renderizar contenido dinámico.
 *
 * @param children contenido dinámico que se renderiza en el panel derecho.
 * @returns estructura visual con menú lateral y área de contenido.
 */
export default function Layout({ children }: { children: ReactNode }) {
  /**
   * Función para cerrar sesión del usuario.
   * Realiza una solicitud POST al endpoint de logout y redirige al home.
   */
  const cerrarSesion = () => {
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/logout`, {
      method: 'POST',
      credentials: 'include'
    }).finally(() => {
      window.location.href = '/';
    });
  };

  return (
    <div className={styles.layout}>
      {/* Sidebar izquierdo con íconos de navegación */}
      <aside className={styles.div1}>
        {/* Logo principal */}
        <img src="/static/img/icon.png" alt="Logo" className={styles['icon-pic']} />

        {/* Menú de navegación con burbujas */}
        <div className={styles['menu-bubbles']}>
          <div className={styles.bubble} title="Dashboard">
            <Axis3d size={24} />
          </div>
          <div className={styles.bubble} title="Órdenes">
            <FilePen size={24} />
          </div>
          <div className={styles.bubble} title="Comisionistas">
            <UsersRound size={24} />
          </div>
          <div className={styles.bubble} title="Favoritas">
            <HandHeart size={24} />
          </div>
        </div>

        {/* Botón para cerrar sesión */}
        <div className={styles['logout-bubble']}>
          <div
            className={`${styles.bubble} ${styles.logout}`}
            title="Cerrar sesión"
            onClick={cerrarSesion}
          >
            <Undo2 size={24} />
          </div>
        </div>
      </aside>

      {/* Panel derecho donde se renderiza el contenido dinámico */}
      <main className={styles.perfilContainer}>
        {children}
      </main>
    </div>
  );
}