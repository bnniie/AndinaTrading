'use client';

import { ReactNode } from 'react';
import styles from './LayoutPage.module.css';
import {
  Axis3d,
  FilePen,
  UsersRound,
  FileHeart,
  Undo2
} from 'lucide-react';

export default function Layout({ children }: { children: ReactNode }) {
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
      {/* Sidebar izquierdo */}
      <aside className={styles.div1}>
        <img src="/static/img/icon.png" alt="Logo" className={styles['icon-pic']} />

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
            <FileHeart size={24} />
          </div>
        </div>

        <div className={styles['logout-bubble']}>
          <div className={`${styles.bubble} ${styles.logout}`} title="Cerrar sesión" onClick={cerrarSesion}>
            <Undo2 size={24} />
          </div>
        </div>
      </aside>

      {/* Panel derecho */}
      <main className={styles.perfilContainer}>
        {children}
      </main>
    </div>
  );
}