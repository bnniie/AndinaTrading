'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import PhoneInput from 'react-phone-input-2';
import 'react-phone-input-2/lib/style.css';

import styles from './EditarPerfilModal.module.css';

interface Inversionista {
  usuario: string;
  telefono: string;
}

interface EditarPerfilModalProps {
  inversionista: Inversionista;
  onClose: () => void;
  onSave: (datosActualizados: Inversionista) => void;
}

export default function EditarPerfilModal({
  inversionista,
  onClose,
  onSave
}: EditarPerfilModalProps) {
  const router = useRouter();
  const [formData, setFormData] = useState<Inversionista>({
    usuario: inversionista.usuario,
    telefono: inversionista.telefono
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handlePhoneChange = (phone: string) => {
    setFormData({ ...formData, telefono: phone });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/actualizar-contacto`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        onSave(formData);
        onClose();
        router.push('/perfil');
      } else {
        console.error('Error al actualizar contacto');
      }
    } catch (error) {
      console.error('Error de conexión:', error);
    }
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        <h2 className={styles.title}>Editar Perfil</h2>
        <form onSubmit={handleSubmit} className={styles.formGrid}>
          <div className={styles.column}>
            <input
              type="text"
              name="usuario"
              placeholder="Usuario"
              value={formData.usuario}
              onChange={handleChange}
              required
              minLength={4}
              maxLength={20}
              pattern="[A-Za-z0-9]{4,20}"
              className={styles.input}
            />
            <small className={styles.helper}>Debe tener entre 4 y 20 caracteres alfanuméricos</small>

            <PhoneInput
              country={'co'}
              onlyCountries={['co', 'pe', 've', 'ec']}
              value={formData.telefono}
              onChange={handlePhoneChange}
              inputClass={styles.input}
              placeholder="Teléfono"
            />
            <small className={styles.helper}>Ingresa tu número telefónico</small>
          </div>

          <div className={styles.buttonRow}>
            <button type="submit" className={styles.actionButton}>Guardar</button>
            <button type="button" onClick={onClose} className={styles.actionButton}>Cancelar</button>
          </div>
        </form>
      </div>
    </div>
  );
}