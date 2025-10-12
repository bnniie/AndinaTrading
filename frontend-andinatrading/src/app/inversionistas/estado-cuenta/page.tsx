"use client";

import { useEffect, useState } from 'react';
import axios from 'axios';

interface Movimiento {
  id: number;
  tipo: string;
  empresa: string;
  monto: number;
  fecha: string;
}

interface EstadoCuenta {
  usuario: string;
  saldo: number;
  movimientos: Movimiento[];
}

export default function EstadoCuenta() {
  const [datos, setDatos] = useState<EstadoCuenta | null>(null);
  const [usuario, setUsuario] = useState('com1');

  useEffect(() => {
    axios.get<EstadoCuenta>(`http://localhost:8000/api/inversionistas/estado-cuenta?usuario=${usuario}`)
        .then((res) => setDatos(res.data))
        .catch((err) => console.error('Error al obtener estado de cuenta:', err));

  }, [usuario]);

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Estado de cuenta de {datos?.usuario}</h2>
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
        <p>Cargando estado de cuenta...</p>
      )}
    </div>
  );
}