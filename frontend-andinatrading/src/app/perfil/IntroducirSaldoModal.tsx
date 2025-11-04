import { useState } from 'react';
import styles from './IntroducirSaldoModal.module.css';

/**
 * Modal para ingresar un nuevo saldo disponible.
 * Muestra el saldo actual y permite al usuario introducir un nuevo valor con validación.
 */
export default function IntroducirSaldoModal({
  saldoActual,
  onClose,
  onSave
}: {
  saldoActual: number; // Saldo actual mostrado como referencia
  onClose: () => void; // Función para cerrar el modal
  onSave: (nuevoSaldo: number) => void; // Función para guardar el nuevo saldo
}) {
  // Estado local para el campo de saldo
  const [saldo, setSaldo] = useState('');

  // Estado para mostrar errores de validación
  const [error, setError] = useState('');

  /**
   * Maneja el envío del formulario.
   * Valida que el saldo ingresado sea un número válido mayor o igual a cero.
   */
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const valor = parseFloat(saldo);

    if (isNaN(valor) || valor < 0) {
      setError('❌ Ingresa un número válido mayor o igual a 0');
      return;
    }

    setError('');
    onSave(valor); // Ejecuta la función de guardado con el nuevo saldo
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        {/* Título del modal */}
        <h2 className={styles.titulo}>Introducir Saldo</h2>

        {/* Saldo actual mostrado como referencia */}
        <p className={styles.referencia}>
          Saldo actual: <strong>${saldoActual.toFixed(2)}</strong>
        </p>

        {/* Formulario de ingreso de saldo */}
        <form onSubmit={handleSubmit}>
          <input
            type="number"
            step="0.01"
            placeholder="Nuevo saldo"
            value={saldo}
            onChange={(e) => setSaldo(e.target.value)}
            className={styles.input}
          />

          {/* Mensaje de error si el valor no es válido */}
          {error && <p className={styles.error}>{error}</p>}

          {/* Botones de acción */}
          <div className={styles.botones}>
            <button type="submit" className={styles.actionButton}>Guardar</button>
            <button type="button" onClick={onClose} className={styles.actionButton}>Cancelar</button>
          </div>
        </form>
      </div>
    </div>
  );
}