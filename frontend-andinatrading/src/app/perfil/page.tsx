'use client';

import { useEffect, useState } from 'react';
import Layout from '../components/Layout';
import styles from './ProfilePage.module.css';
import { User } from 'lucide-react';

export default function PerfilPage() {
  const [inversionista, setInversionista] = useState<any>(null);
  const [mensaje, setMensaje] = useState('');
  const [ordenesPorEstado, setOrdenesPorEstado] = useState<Record<string, number>>({});

  // Obtener perfil del inversionista
  useEffect(() => {
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/perfil`, {
      credentials: 'include'
    })
      .then((res) => {
        if (!res.ok) throw new Error('No hay sesión activa');
        return res.json();
      })
      .then(setInversionista)
      .catch(() => setMensaje('❌ No hay sesión activa o ocurrió un error'));
  }, []);

  // Obtener estadísticas de órdenes por estado
useEffect(() => {
  fetch("http://localhost:8080/api/inversionistas/ordenes/estadisticas", {
    method: "GET",
    credentials: "include",
  })
    .then((res) => {
      if (!res.ok) throw new Error("Error al obtener estadísticas");
      return res.json();
    })
    .then((data) => {
  console.log("Estadisticas recibidas:", data);

  const normalizado: Record<string, number> = Object.fromEntries(
    Object.entries(data).map(([k, v]) => [k.toLowerCase().trim(), Number(v)])
  );

  setOrdenesPorEstado(normalizado);
})

    .catch((err) => {
      console.error("Error al procesar estadísticas:", err);
      setOrdenesPorEstado({});
    });
}, []);


  return (
    <Layout>
      <div className={styles.pageContainer}>
        <h2 className={styles.titulo}>Perfil del Inversionista</h2>
        <hr className={styles['divider-line']} />

        {mensaje && <p className={styles.mensaje}>{mensaje}</p>}

        {inversionista && (
          <div className={styles.contenedorPrincipal}>
            <div className={styles.datos}>
              <div className={styles.iconoPerfil}>
                <div className={styles.iconoCirculo}>
                  <User size={32} strokeWidth={1.5} />
                </div>
              </div>

              <p><strong>Nombre:</strong> {inversionista.nombre} {inversionista.apellido}</p>
              <p><strong>Correo:</strong> {inversionista.correo}</p>
              <p><strong>Ciudad:</strong> {inversionista.ciudad}</p>
              <p><strong>País:</strong> {inversionista.pais}</p>
              <p><strong>Usuario:</strong> {inversionista.usuario}</p>
              <p><strong>Teléfono:</strong> {inversionista.telefono}</p>
              <div className={styles.botonEditar}>
                <button className={styles.actionButton}>Editar</button>
              </div>
            </div>

            <div className={styles.panelSecundario}>
              <div className={styles.ordenes}>
                <h3>Órdenes por estado</h3>
                <ul className={styles.listaOrdenes}>
                  <li>Por aprobar: {ordenesPorEstado.por_aprobar ?? 0}</li>
                  <li>Aprobadas: {ordenesPorEstado.aprobada ?? 0}</li>
                  <li>Finalizadas: {ordenesPorEstado.finalizada ?? 0}</li>
                </ul>
              </div>

              <div className={styles.movimientos}>
                <div className={styles.saldoGrafico}>
                  <div className={styles.saldoYBotones}>
                    <h3 className={styles.saldoTitulo}>Saldo: ${inversionista.saldo}</h3>
                    <div className={styles.botonesSaldo}>
                      <button className={styles.actionButton}>Introducir Saldo</button>
                      <button className={styles.actionButton}>Editar Contrato</button>
                    </div>
                  </div>

                  <h4 className={styles.subtituloGrafico}>Movimientos en la semana</h4>
                  <div className={styles.graficoPlaceholder}>
                    <p>[grafica]</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </Layout>
  );
}
