'use client';

import { useState } from 'react';
import styles from './EditarContratoModal.module.css';

/**
 * Modal para editar los parámetros de un contrato.
 * Permite modificar el porcentaje de comisión y la duración en meses, con validación de rango.
 */
export default function EditarContratoModal({
  porcentajeInicial,
  duracionInicial,
  onClose,
  onSave
}: {
  porcentajeInicial: number; // Valor inicial del porcentaje de comisión
  duracionInicial: number;   // Valor inicial de la duración del contrato
  onClose: () => void;       // Función para cerrar el modal
  onSave: (datos: { porcentaje: number; duracion: number }) => void; // Función para guardar los datos editados
}) {
  // Estados locales para los campos del formulario
  const [porcentaje, setPorcentaje] = useState(porcentajeInicial);
  const [duracion, setDuracion] = useState(duracionInicial);
  const [error, setError] = useState('');

  /**
   * Maneja el envío del formulario.
   * Valida los rangos permitidos y ejecuta la función de guardado si los datos son válidos.
   */
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    // Validación de rangos
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
        {/* Título y descripción del modal */}
        <h2 className={styles.titulo}>Editar contrato</h2>
        <p className={styles.referencia}>Modifica los parámetros del contrato actual</p>

        {/* Formulario de edición */}
        <form onSubmit={handleSubmit}>
          {/* Campo porcentaje */}
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

          {/* Campo duración */}
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

          {/* Mensaje de error si los valores son inválidos */}
          {error && <p className={styles.error}>{error}</p>}

          {/* Botones de acción */}
          <div className={styles.botones}>
            <button type="submit" className={styles.actionButton}>Editar</button>
            <button type="button" onClick={onClose} className={styles.actionButton}>Cancelar</button>
          </div>
        </form>
      </div>
    </div>
  );
}