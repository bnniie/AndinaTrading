'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import PhoneInput from 'react-phone-input-2';
import 'react-phone-input-2/lib/style.css';

import styles from './RegistroPage.module.css';

export default function RegistroPage() {
  const router = useRouter();

  const paises = ['Colombia', 'Ecuador', 'Perú', 'Venezuela'];

  const ciudadesPorPais: Record<string, string[]> = {
    Colombia: ['Bogotá', 'Medellín', 'Cali', 'Barranquilla', 'Cartagena', 'Bucaramanga', 'Pereira', 'Manizales', 'Santa Marta', 'Cúcuta'],
    Ecuador: ['Quito', 'Guayaquil', 'Cuenca', 'Machala', 'Manta', 'Loja', 'Portoviejo', 'Ambato', 'Esmeraldas', 'Ibarra'],
    Perú: ['Lima', 'Arequipa', 'Trujillo', 'Chiclayo', 'Piura', 'Cusco', 'Iquitos', 'Huancayo', 'Tacna', 'Puno'],
    Venezuela: ['Caracas', 'Maracaibo', 'Valencia', 'Barquisimeto', 'Mérida', 'San Cristóbal', 'Puerto La Cruz', 'Ciudad Guayana', 'Maracay', 'Cumaná']
  };

  const [form, setForm] = useState({
    nombre: '',
    apellido: '',
    documentoIdentidad: '',
    correo: '',
    telefono: '',
    pais: '',
    ciudad: '',
    usuario: '',
    contrasena: ''
  });

  const [confirmarContrasena, setConfirmarContrasena] = useState('');
  const [mostrarContrasena, setMostrarContrasena] = useState(false);
  const [mostrarConfirmacion, setMostrarConfirmacion] = useState(false);
  const [mensaje, setMensaje] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (form.contrasena !== confirmarContrasena) {
      setMensaje('❌ Las contraseñas no coinciden');
      return;
    }

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/registro`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });

      if (response.ok) {
        setMensaje('✅ Registro exitoso');
        setForm({
          nombre: '',
          apellido: '',
          documentoIdentidad: '',
          correo: '',
          telefono: '',
          pais: '',
          ciudad: '',
          usuario: '',
          contrasena: ''
        });
        setConfirmarContrasena('');
        router.push('/login');
      } else if (response.status === 400) {
        setMensaje('❌ Datos inválidos o usuario ya registrado');
      } else {
        setMensaje('⚠️ Error inesperado en el servidor');
      }
    } catch (error) {
      setMensaje('❌ No se pudo conectar con el servidor');
      console.error(error);
    }
  };

  const volverInicio = () => {
    router.push('/');
  };

  return (
    <main className={styles.container}>
      <section className={styles.left}>
        <Image
          src="/static/img/icon.png"
          alt="AndinaTrading"
          width={80}
          height={80}
          className={styles.logo}
        />
        <h1 className={styles.title}>Registro</h1>
        <p className={styles.description}>
          Crea tu cuenta para comenzar a invertir con Andina Trading
        </p>
      </section>

      <section className={styles.right}>
        <form className={styles.formGrid} onSubmit={handleSubmit}>
          <div className={styles.column}>
            <input
              type="text"
              name="nombre"
              placeholder="Nombres"
              value={form.nombre}
              onChange={handleChange}
              required
              className={styles.input}
            />
            <small className={styles.helper}>Ingresa tus nombres completos</small>

            <input
              type="text"
              name="apellido"
              placeholder="Apellidos"
              value={form.apellido}
              onChange={handleChange}
              required
              className={styles.input}
            />
            <small className={styles.helper}>Ingresa tus apellidos completos</small>

            <input
              type="text"
              name="documentoIdentidad"
              placeholder="Documento de Identidad"
              value={form.documentoIdentidad}
              onChange={handleChange}
              required
              minLength={6}
              maxLength={12}
              pattern="\d{6,12}"
              className={styles.input}
            />
            <small className={styles.helper}>Debe contener entre 6 y 12 dígitos numéricos</small>

            <select
              name="pais"
              value={form.pais}
              onChange={(e) => {
                setForm({ ...form, pais: e.target.value, ciudad: '' });
              }}
              required
              className={styles.input}
            >
              <option value="">Selecciona tu país</option>
              {paises.map((pais) => (
                <option key={pais} value={pais}>{pais}</option>
              ))}
            </select>
            <small className={styles.helper}>País de residencia</small>

            <select
              name="ciudad"
              value={form.ciudad}
              onChange={handleChange}
              required
              className={styles.input}
              disabled={!form.pais}
            >
              <option value="">Selecciona tu ciudad</option>
              {form.pais && ciudadesPorPais[form.pais].map((ciudad) => (
                <option key={ciudad} value={ciudad}>{ciudad}</option>
              ))}
            </select>
            <small className={styles.helper}>Ciudad donde resides actualmente</small>
          </div>

          <div className={styles.column}>
            <input
              type="email"
              name="correo"
              placeholder="Correo Electrónico"
              value={form.correo}
              onChange={handleChange}
              required
              className={styles.input}
            />
            <small className={styles.helper}>Ejemplo: usuario@dominio.com</small>

            <PhoneInput
              country={'co'}
              onlyCountries={['co', 'pe', 've', 'ec']}
              value={form.telefono}
              onChange={(phone) => setForm({ ...form, telefono: phone })}
              inputClass={styles.input}
              placeholder="Teléfono"
            />
            <small className={styles.helper}>Ingresa tu número telefónico</small>

            <input
              type="text"
              name="usuario"
              placeholder="Usuario"
              value={form.usuario}
              onChange={handleChange}
              required
              minLength={4}
              maxLength={20}
              pattern="[A-Za-z0-9]{4,20}"
              className={styles.input}
            />
            <small className={styles.helper}>Debe tener entre 4 y 20 caracteres alfanuméricos</small>

            <div className={styles.passwordWrapper}>
              <input
                type={mostrarContrasena ? 'text' : 'password'}
                name="contrasena"
                placeholder="Contraseña"
                value={form.contrasena}
                onChange={handleChange}
                required
                minLength={6}
                maxLength={20}
                pattern="^(?=.*[A-Za-z])(?=.*\d)(?=.*[^A-Za-z\d]).{6,20}$"
                className={styles.input}
              />
              <span onClick={() => setMostrarContrasena(!mostrarContrasena)} className={styles.eyeIcon}>👁</span>
            </div>
            <small className={styles.helper}>Debe tener entre 6 y 20 caracteres con letra, número y símbolo</small>

            <div className={styles.passwordWrapper}>
              <input
                type={mostrarConfirmacion ? 'text' : 'password'}
                name="confirmarContrasena"
                placeholder="Confirmar contraseña"
                value={confirmarContrasena}
                onChange={(e) => setConfirmarContrasena(e.target.value)}
                required
                className={styles.input}
              />
              <span onClick={() => setMostrarConfirmacion(!mostrarConfirmacion)} className={styles.eyeIcon}>👁</span>
            </div>
            <small className={styles.helper}>Debe coincidir con la contraseña ingresada</small>
          </div>

          <div className={styles.buttonRow}>
            <button type="submit" className={styles.actionButton}>Registrar</button>
            <button type="button" onClick={volverInicio} className={styles.actionButton}>Volver</button>
          </div>
        </form>

        <p className={styles.mensaje}>{mensaje}</p>
      </section>
    </main>
  );
}