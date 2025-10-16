'use client'; // Indica que este componente se ejecuta en el cliente. Requerido para usar hooks como useState y useEffect.

// Importación de hooks y librerías necesarias
import { useEffect, useState } from 'react'; // Hooks para manejar estado y efectos secundarios.
import axios from 'axios'; // Cliente HTTP para realizar solicitudes al backend.
import { useSearchParams } from 'next/navigation'; // Hook para acceder a parámetros de búsqueda en la URL.

// Definición de la estructura de un movimiento financiero
interface Movimiento {
  id: number;
  tipo: string;
  empresa: string;
  monto: number;
  fecha: string;
}

// Definición de la estructura del estado de cuenta
interface EstadoCuenta {
  usuario: string;
  saldo: number;
  movimientos: Movimiento[];
}

export default function EstadoCuenta() {
  // Estado que almacena los datos del estado de cuenta
  const [datos, setDatos] = useState<EstadoCuenta | null>(null);

  // Obtiene el parámetro 'usuario' desde la URL
  const searchParams = useSearchParams();
  const usuarioParam = searchParams.get('usuario') || 'desconocido';

  // Estado local para el usuario consultado
  const [usuario, setUsuario] = useState(usuarioParam);

  // Efecto que se ejecuta al montar el componente o cuando cambia el usuario
  useEffect(() => {
    // Solicita los datos del estado de cuenta desde el backend
    axios.get<EstadoCuenta>(`http://localhost:3000/api/inversionistas/estado-cuenta?usuario=${usuario}`)
      .then((res) => setDatos(res.data)) // Actualiza el estado con los datos recibidos
      .catch((err) => console.error('Error al obtener estado de cuenta:', err)); // Manejo de errores
  }, [usuario]); // Dependencia: se vuelve a ejecutar si cambia el usuario

  return (
    <div style={{ padding: '2rem' }}>
      {/* Título con el nombre del usuario */}
      <h2>Estado de cuenta de {datos?.usuario}</h2>

      {/* Si los datos están disponibles, muestra el saldo y los movimientos */}
      {datos ? (
        <>
          <p><strong>Saldo actual:</strong> ${datos.saldo.toFixed(2)}</p>

          <h3>Movimientos recientes:</h3>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr>
                <th>Fecha</th>
                <th>Tipo</th>
                <th>Empresa</th>
                <th>Monto</th>
              </tr>
            </thead>
            <tbody>
              {/* Mapea cada movimiento y lo muestra en una fila */}
              {datos.movimientos.map(m => (
                <tr key={m.id}>
                  <td>{new Date(m.fecha).toLocaleString()}</td>
                  <td>{m.tipo}</td>
                  <td>{m.empresa}</td>
                  <td>${m.monto.toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      ) : (
        // Mensaje de carga mientras se obtienen los datos
        <p>Cargando estado de cuenta...</p>
      )}
    </div>
  );
}
