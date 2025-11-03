import { useState } from 'react';
import styles from './IntroducirSaldoModal.module.css';

export default function IntroducirSaldoModal({
  saldoActual,
  onClose,
  onSave
}: {
  saldoActual: number;
  onClose: () => void;
  onSave: (nuevoSaldo: number) => void;
}) {
  const [saldo, setSaldo] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const valor = parseFloat(saldo);
    if (isNaN(valor) || valor < 0) {
      setError('❌ Ingresa un número válido mayor o igual a 0');
      return;
    }

    setError('');
    onSave(valor);
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        <h2 className={styles.titulo}>Introducir Saldo</h2>
        <p className={styles.referencia}>Saldo actual: <strong>${saldoActual.toFixed(2)}</strong></p>
        <form onSubmit={handleSubmit}>
          <input
            type="number"
            step="0.01"
            placeholder="Nuevo saldo"
            value={saldo}
            onChange={(e) => setSaldo(e.target.value)}
            className={styles.input}
          />
          {error && <p className={styles.error}>{error}</p>}
          <div className={styles.botones}>
            <button type="submit" className={styles.actionButton}>Guardar</button>
            <button type="button" onClick={onClose} className={styles.actionButton}>Cancelar</button>
          </div>
        </form>
      </div>
    </div>
  );
}