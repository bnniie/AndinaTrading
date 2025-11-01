'use client';

import { useEffect, useState } from 'react';
import Layout from '../components/Layout';
import styles from './ProfilePage.module.css';
import { User } from 'lucide-react';
import { Pie } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend
} from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);

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
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/inversionistas/ordenes/estadisticas`, {
      method: 'GET',
      credentials: 'include',
    })
      .then((res) => {
        if (!res.ok) throw new Error("Error al obtener estadísticas");
        return res.json();
      })
      .then((data) => {
        console.log("Estadísticas recibidas:", data);

        const normalizado: Record<string, number> = Object.fromEntries(
          Object.entries(data).map(([k, v]) => [k.toLowerCase().trim(), Number(v)])
        );

        console.log("Estado normalizado:", normalizado);
        setOrdenesPorEstado(normalizado);
      })
      .catch((err) => {
        console.error("Error al procesar estadísticas:", err);
        setOrdenesPorEstado({});
      });
  }, []);

  const pieData = {
    labels: ['Por aprobar', 'Aprobadas', 'Finalizadas'],
    datasets: [
      {
        label: 'Órdenes',
        data: [
          ordenesPorEstado.por_aprobar ?? 0,
          ordenesPorEstado.aprobada ?? 0,
          ordenesPorEstado.finalizada ?? 0
        ],
        backgroundColor: ['#facc15', '#4ade80', '#60a5fa'],
        borderColor: ['#fbbf24', '#22c55e', '#3b82f6'],
        borderWidth: 1
      }
    ]
  };

  const totalOrdenes = Object.values(ordenesPorEstado).reduce((acc, val) => acc + val, 0);

  return (
    <Layout>
      <div className={styles.pageContainer}>
        <h2 className={styles.titulo}>Perfil Inversionista</h2>
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
                <h3 className={styles.tituloOrdenes}>Órdenes</h3>
                <div className={styles.ordenesContenido}>
                  <div className={styles.graficoOrdenes}>
                    {totalOrdenes > 0 ? (
                      <Pie
                        data={pieData}
                        options={{
                          maintainAspectRatio: false,
                          plugins: {
                            legend: { display: false }
                          }
                        }}
                      />
                    ) : (
                      <p>No hay datos suficientes para graficar</p>
                    )}
                  </div>

                  <div className={styles.listaOrdenes}>
                    <ul>
                      <li><span className={styles.porAprobar}>Por aprobar:</span><span className={styles.valor}> {ordenesPorEstado.por_aprobar ?? 0}</span></li>
                      <li><span className={styles.aprobadas}>Aprobadas:</span><span className={styles.valor}> {ordenesPorEstado.aprobada ?? 0}</span></li>
                      <li><span className={styles.finalizadas}>Finalizadas:</span><span className={styles.valor}> {ordenesPorEstado.finalizada ?? 0}</span></li>
                    </ul>
                  </div>
                </div>
              </div>

                {/* Bloque de saldo */}
                <div className={styles.movimientos}>
                  <div className={styles.saldoGrafico}>
                    <div className={styles.saldoYBotones}>
                      <h3 className={styles.saldoTitulo}>Saldo: ${inversionista.saldo}</h3>
                      <div className={styles.botonesSaldo}>
                        <button className={styles.actionButton}>Introducir Saldo</button>
                        <button className={styles.actionButton}>Editar Contrato</button>
                      </div>
                    </div>
                    <h4 className={styles.subtituloGrafico}>Movimientos</h4>
                  </div>
                </div>
              </div>
          </div>
        )}
      </div>
    </Layout>
  );
}
