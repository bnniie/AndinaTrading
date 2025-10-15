'use client';

import { useRouter } from 'next/navigation';
import Image from 'next/image';
import styles from './HomePage.module.css';

export default function HomePage() {
  const router = useRouter();

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
        <h1 className={styles.title}>Andina Trading</h1>
        <p className={styles.description}>
          Donde las oportunidades se convierten en decisiones financieras :3
        </p>
      </section>

      <section className={styles.right}>
        <div className={styles.buttonGroup}>
          <button className={styles.actionButton} onClick={() => router.push('/login')}>
            Iniciar sesión
          </button>
          <button className={styles.actionButton} onClick={() => router.push('/inversionistas/registro')}>
            Registrarse
          </button>
        </div>
      </section>
    </main>
  );
}
