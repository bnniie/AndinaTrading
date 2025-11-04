'use client';

import { useEffect, useState } from 'react';
import Layout from '../components/Layout';
import styles from './ProfilePage.module.css';
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

import { useRouter } from 'next/navigation';
import EditarPerfil from './EditarPerfilModal';
import IntroducirSaldoModal from './IntroducirSaldoModal';
import EditarContratoModal from './EditarContratoModal';

/**
 * Página de perfil del inversionista.
 * Muestra datos personales, estadísticas de órdenes, saldo actual, movimientos recientes y permite editar perfil, saldo y contrato.
 */
export default function PerfilPage() {
  const [inversionista, setInversionista] = useState<any>(null);
  const [mensaje, setMensaje] = useState('');
  const [ordenesPorEstado, setOrdenesPorEstado] = useState<Record<string, number>>({});
  const [movimientos, setMovimientos] = useState<{ fechaCreacion: string; precio: number }[]>([]);
  const [mostrarModal, setMostrarModal] = useState(false);
  const [mostrarSaldoModal, setMostrarSaldoModal] = useState(false);
  const [mostrarContratoModal, setMostrarContratoModal] = useState(false);
  const [contrato, setContrato] = useState<{ porcentajeComision: number; duracionMeses: number } | null>(null);
  const router = useRouter();

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
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/ordenes/estadisticas`, {
      method: 'GET',
      credentials: 'include',
    })
      .then((res) => {
        if (!res.ok) throw new Error("Error al obtener estadísticas");
        return res.json();
      })
      .then((data) => {
        const normalizado: Record<string, number> = Object.fromEntries(
          Object.entries(data).map(([k, v]) => [k.toLowerCase().trim(), Number(v)])
        );
        setOrdenesPorEstado(normalizado);
      })
      .catch(() => setOrdenesPorEstado({}));
  }, []);

  // Obtener movimientos de órdenes (fecha + precio)
  useEffect(() => {
  fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/ordenes/movimientos`, {
    method: 'GET',
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

  // Datos para gráfico circular
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

  // Datos para gráfico de línea
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
        pointHoverRadius: 6
      }
    ]
  };

  /**
   * Guarda los datos actualizados del perfil
   */
  const totalOrdenes = Object.values(ordenesPorEstado).reduce((acc, val) => acc + val, 0);

  const handleGuardarPerfil = async (datosActualizados: { usuario: string; telefono: string }) => {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/actualizar-contacto`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(datosActualizados)
    });

    if (response.ok) {
      setInversionista((prev: any) => ({
        ...prev,
        ...datosActualizados
      }));

      setMostrarModal(false);
      router.push('/perfil');
    } else {
      console.error('❌ Error al actualizar contacto');
      setMensaje('❌ No se pudo actualizar el perfil');
    }
  } catch (error) {
    console.error('❌ Error de conexión:', error);
    setMensaje('❌ Error de conexión con el servidor');
  }
};

 /**
   * Guarda el nuevo saldo ingresado
   */
const handleGuardarSaldo = async (montoAdicional: number) => {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/actualizar-saldo`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ saldo: montoAdicional })
    });

    if (response.ok) {
      setInversionista((prev: any) => ({
        ...prev,
        saldo: prev.saldo + montoAdicional
      }));
      setMostrarSaldoModal(false);
    } else {
      setMensaje('❌ No se pudo actualizar el saldo');
    }
  } catch (error) {
    setMensaje('❌ Error de conexión con el servidor');
  }
};

useEffect(() => {
  const obtenerContrato = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/contrato`, {
        method: 'GET',
        credentials: 'include'
      });

      if (response.ok) {
        const data = await response.json();
        setContrato({
        porcentajeComision: data.porcentajeComision,
        duracionMeses: data.duracionMeses
      });
      }
    } catch (error) {
      console.error('Error al obtener contrato:', error);
    }
  };

  obtenerContrato();
}, []);

 /**
   * Guarda los parámetros editados del contrato
   */
  const handleGuardarContrato = async ({ porcentaje, duracion }: { porcentaje: number; duracion: number }) => {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/editar-contrato`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ porcentaje, duracion })
    });

    if (response.ok) {
      setContrato({ porcentajeComision: porcentaje, duracionMeses: duracion });
      setMostrarContratoModal(false);
    } else {
      setMensaje('❌ No se pudo actualizar el contrato');
    }
  } catch (error) {
    console.error('❌ Error al actualizar contrato:', error);
    setMensaje('❌ Error de conexión con el servidor');
  }
};

  return (
    <Layout>
      <div className={styles.pageContainer}>
        <h2 className={styles.titulo}>Perfil Inversionista</h2>
        <hr className={styles['divider-line']} />

        {mensaje && <p className={styles.mensaje}>{mensaje}</p>}

        {inversionista && (
          <div className={styles.contenedorPrincipal}>
            {/* Datos personales */}
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
                <button className={styles.actionButton} onClick={() => setMostrarModal(true)}>
                  Editar
                </button>
              </div>
            </div>

            <div className={styles.panelSecundario}>
              <div className={styles.ordenes}>
                <h3 className={styles.tituloOrdenes}>Órdenes</h3>
                <div className={styles.ordenesContenido}>
                  <div className={styles.graficoOrdenes}>
                    {/* Diagrama de torta */}
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

                  {/* Ordenes por estado */}
                  <div className={styles.listaOrdenes}>
                    <ul>
                      <li><span className={styles.porAprobar}>Por aprobar:</span><span className={styles.valor}> {ordenesPorEstado.por_aprobar ?? 0}</span></li>
                      <li><span className={styles.aprobadas}>Aprobadas:</span><span className={styles.valor}> {ordenesPorEstado.aprobada ?? 0}</span></li>
                      <li><span className={styles.finalizadas}>Finalizadas:</span><span className={styles.valor}> {ordenesPorEstado.finalizada ?? 0}</span></li>
                    </ul>
                  </div>
                </div>
              </div>

              <div className={styles.movimientos}>
                <div className={styles.saldoGrafico}>
                  <div className={styles.saldoYBotones}>
                    <h3 className={styles.saldoTitulo}>
                      {/* Sección de saldo */}
                      <span className={styles.saldoLabel}>Saldo:</span>{' '}
                      <span className={styles.saldoValor}>${inversionista.saldo}</span>
                    </h3>
                    <div className={styles.botonesSaldo}>
                      <button className={styles.actionButton} onClick={() => setMostrarSaldoModal(true)}>Introducir Saldo</button>
                      <button onClick={() => setMostrarContratoModal(true)} className={styles.actionButton}>Editar Contrato</button>
                    </div>
                  </div>
                  <div className={styles.graficoMovimientos}>
                    <h3 className={styles.subtituloGrafico}>Movimientos Recientes</h3>
                    {/* Diagrama de linea */}
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
                    color: '#000',
                    font: { size: 10 }
                  },
                          grid: { display: false }
                        },
                        y: {
                          title: { display: true, text: 'Precio ($)' },
                          ticks: {
                            callback: (value) => `$${value}`,
                            color: '#000',
                            font: { size: 10 }
                          }
                        }
                      }
                    }}
                    height={180}
                    />
                  </div>

                  {/* Mostrar Editar Perfil */}
                  {mostrarModal && inversionista && (
                    <EditarPerfil
                      inversionista={inversionista}
                      onClose={() => setMostrarModal(false)}
                      onSave={handleGuardarPerfil}
                    />
                  )}

                  {/* Mostrar Saldo */}
                  {mostrarSaldoModal && (
                    <IntroducirSaldoModal
                      saldoActual={inversionista.saldo}
                      onClose={() => setMostrarSaldoModal(false)}
                      onSave={handleGuardarSaldo}
                    />
                  )}

                  {/* Mostrar Contrato */}
                  {mostrarContratoModal && (
                  <EditarContratoModal
                    porcentajeInicial={contrato?.porcentajeComision ?? 0}
                    duracionInicial={contrato?.duracionMeses ?? 12}
                    onClose={() => setMostrarContratoModal(false)}
                    onSave={handleGuardarContrato}
                  />
                  )}
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </Layout>
  );
}
