'use client';

import { useEffect, useState } from 'react';
import styles from '../ProfilePage.module.css';
import Layout from './LayoutComisionista';
import { User } from 'lucide-react';
import { Pie, Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  ArcElement,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend
} from 'chart.js';

// Registro de módulos necesarios para los gráficos
ChartJS.register(
  ArcElement,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend
);

/**
 * Página de perfil del comisionista.
 * Muestra datos personales, estadísticas de órdenes y movimientos recientes en formato gráfico.
 */
export default function PerfilComisionistaPage() {
  // Datos del comisionista
  const [comisionista, setComisionista] = useState<any>(null);

  // Mensaje de error o estado
  const [mensaje, setMensaje] = useState('');

  // Estadísticas de órdenes por estado
  const [ordenesPorEstado, setOrdenesPorEstado] = useState<Record<string, number>>({});

  // Lista de movimientos recientes
  const [movimientos, setMovimientos] = useState<{ fechaCreacion: string; precio: number }[]>([]);

  /**
   * Obtiene los datos del perfil del comisionista al cargar la página.
   */
  useEffect(() => {
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/comisionistas/perfil`, {
      credentials: 'include'
    })
      .then((res) => {
        if (!res.ok) throw new Error('No hay sesión activa');
        return res.json();
      })
      .then(setComisionista)
      .catch(() => setMensaje('❌ No hay sesión activa o ocurrió un error'));
  }, []);

  /**
   * Obtiene estadísticas de órdenes por estado.
   */
  useEffect(() => {
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/comisionistas/ordenes/estadisticas`, {
      credentials: 'include'
    })
      .then((res) => {
        if (!res.ok) throw new Error('Error al obtener estadísticas');
        return res.json();
      })
      .then((data) => {
        const normalizado = Object.fromEntries(
          Object.entries(data).map(([k, v]) => [k.toLowerCase().trim(), Number(v)])
        );
        setOrdenesPorEstado(normalizado);
      })
      .catch(() => setOrdenesPorEstado({}));
  }, []);

  /**
   * Obtiene movimientos recientes de órdenes.
   */
  useEffect(() => {
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/comisionistas/ordenes/movimientos`, {
      credentials: 'include'
    })
      .then((res) => {
        if (!res.ok) throw new Error('Error al obtener movimientos');
        return res.json();
      })
      .then((data) => {
        const ordenesFiltradas = data.map((orden: any) => ({
          fechaCreacion: orden.fechaCreacion,
          precio: Number(orden.precio)
        }));
        setMovimientos(ordenesFiltradas);
      })
      .catch(() => setMovimientos([]));
  }, []);

  // Datos para gráfico circular de órdenes
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

  // Datos para gráfico de línea de movimientos
  const lineData = {
    labels: movimientos.map((o) => o.fechaCreacion),
    datasets: [
      {
        label: 'Precio de orden',
        data: movimientos.map((o) => o.precio),
        fill: false,
        borderColor: '#d881a3',
        backgroundColor: '#d881a3',
        tension: 0.3,
        pointRadius: 4,
        pointHoverRadius: 6,
        hitRadius: 20
      }
    ]
  };

  // Total de órdenes sumando todos los estados
  const totalOrdenes = Object.values(ordenesPorEstado).reduce((acc, val) => acc + val, 0);

  return (
    <Layout>
      <div className={styles.pageContainer}>
        <h2 className={styles.titulo}>Perfil Comisionista</h2>
        <hr className={styles['divider-line']} />

        {/* Mensaje de error o estado */}
        {mensaje && <p className={styles.mensaje}>{mensaje}</p>}

        {/* Datos del comisionista */}
        {comisionista && (
          <div className={styles.contenedorPrincipal}>
            <div className={styles.datos}>
              <div className={styles.iconoPerfil}>
                <div className={styles.iconoCirculo}>
                  <User size={32} strokeWidth={1.5} />
                </div>
              </div>

              <p><strong>Nombre:</strong> {comisionista.nombreCompleto}</p>
              <p><strong>Correo:</strong> {comisionista.correo}</p>
              <p><strong>Ciudad:</strong> {comisionista.ciudad}</p>
              <p><strong>País:</strong> {comisionista.pais}</p>
              <p><strong>Usuario:</strong> {comisionista.usuario}</p>
            </div>

            {/* Panel de estadísticas */}
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

              {/* Gráfico de movimientos recientes */}
              <div className={styles.graficoMovimientosFijo}>
                <h3 className={styles.subtituloGrafico}>Movimientos Recientes</h3>
                <div style={{ width: '100%', overflowX: 'auto' }}>
                  <div style={{ minWidth: '600px', height: '220px' }}>
                    <Line
                      data={lineData}
                      options={{
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                          legend: { display: false }
                        },
                        scales: {
                          x: {
                            title: { display: true, text: 'Fecha (DD-MM)' },
                            ticks: {
                              callback: function (value, index) {
                                const fecha = movimientos[index]?.fechaCreacion;
                                if (!fecha) return '';
                                const date = new Date(fecha);
                                const dia = String(date.getDate()).padStart(2, '0');
                                const mes = String(date.getMonth() + 1).padStart(2, '0');
                                return `${dia}/${mes}`;
                              },
                              maxTicksLimit: 6,
                              color: '#000',
                              font: { size: 10 }
                            },
                            grid: { display: false }
                          },
                          y: {
                            title: { display: true, text: 'Precio ($)' },
                            ticks: {
                              callback: (value) => `$${value}`,
                              stepSize: 1000,
                              color: '#000',
                              font: { size: 10 }
                            },
                            beginAtZero: true,
                            suggestedMax: Math.max(...movimientos.map((m) => m.precio)) * 1.1
                          }
                        }
                      }}
                      height={180}
                    />
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