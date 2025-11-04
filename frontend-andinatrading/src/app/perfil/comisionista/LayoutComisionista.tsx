'use client';

import { ReactNode } from 'react';
import styles from './LayoutPageComisionista.module.css';
import {
  BanknoteArrowUp,
  Newspaper,
  HandCoins,
  Undo2
} from 'lucide-react';

/**
 * Layout principal para comisionistas.
 * Divide la vista en un sidebar izquierdo con navegación y un panel derecho para contenido dinámico.
 */
export default function Layout({ children }: { children: ReactNode }) {
  /**
   * Cierra la sesión del usuario comisionista.
   * Realiza una petición POST al endpoint de logout y redirige al inicio.
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
      {/* Sidebar izquierdo con navegación vertical */}
      <aside className={styles.div1}>
        {/* Logotipo institucional */}
        <img src="/static/img/icon.png" alt="Logo" className={styles['icon-pic']} />

        {/* Grupo de botones de navegación */}
        <div className={styles['menu-bubbles']}>
          {/* Acceso a módulo de bolsa */}
          <div className={styles.bubble} title="Bolsa">
            <BanknoteArrowUp size={24} />
          </div>

          {/* Acceso a solicitudes pendientes */}
          <div className={styles.bubble} title="Solicitudes pendientes">
            <Newspaper size={24} />
          </div>

          {/* Acceso a órdenes pendientes */}
          <div className={styles.bubble} title="Órdenes pendientes">
            <HandCoins size={24} />
          </div>
        </div>

        {/* Botón de cierre de sesión */}
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