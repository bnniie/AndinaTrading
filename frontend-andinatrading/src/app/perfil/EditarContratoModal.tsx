'use client';

import { useState } from 'react';
import styles from './EditarContratoModal.module.css';

export default function EditarContratoModal({
  porcentajeInicial,
  duracionInicial,
  onClose,
  onSave
}: {
  porcentajeInicial: number;
  duracionInicial: number;
  onClose: () => void;
  onSave: (datos: { porcentaje: number; duracion: number }) => void;
}) {
  const [porcentaje, setPorcentaje] = useState(porcentajeInicial);
  const [duracion, setDuracion] = useState(duracionInicial);
  const [error, setError] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (porcentaje < 0 || porcentaje > 100 || duracion < 1 || duracion > 60) {
      setError('Valores fuera de rango. Revisa los campos.');
      return;
    }

    setError('');
    onSave({ porcentaje, duracion });
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        <h2 className={styles.titulo}>Editar contrato</h2>
        <p className={styles.referencia}>Modifica los parámetros del contrato actual</p>

        <form onSubmit={handleSubmit}>
          <div>
            <label htmlFor="porcentaje">Porcentaje de comisión</label><br />
            <input
              id="porcentaje"
              type="number"
              value={porcentaje}
              onChange={(e) => setPorcentaje(Number(e.target.value))}
              className={styles.input}
              min={0}
              max={100}
            />
          </div>

          <div style={{ marginTop: '1rem' }}>
            <label htmlFor="duracion">Duración del contrato (meses)</label><br />
            <input
              id="duracion"
              type="number"
              value={duracion}
              onChange={(e) => setDuracion(Number(e.target.value))}
              className={styles.input}
              min={1}
              max={60}
            />
          </div>

          {error && <p className={styles.error}>{error}</p>}

          <div className={styles.botones}>
            <button type="submit" className={styles.actionButton}>Editar</button>
            <button type="button" onClick={onClose} className={styles.actionButton}>Cancelar</button>
          </div>
        </form>
      </div>
    </div>
  );
}