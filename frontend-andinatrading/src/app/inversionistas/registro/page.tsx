'use client'; // Indica que este componente se ejecuta en el cliente (Client Component), necesario para usar hooks como useState y useRouter.

import { useState } from 'react'; // Hook para manejar el estado local del formulario y mensajes.
import { useRouter } from 'next/navigation'; // Hook para redireccionar al usuario tras el registro.
import styles from './RegistroPage.module.css'; // Módulo CSS para estilizar el formulario de forma encapsulada.

import PhoneInput from 'react-phone-input-2'; // Componente para entrada de teléfono con formato internacional.
import 'react-phone-input-2/lib/style.css'; // Estilos base para el componente PhoneInput.

export default function RegistroPage() {

  const router = useRouter(); // Instancia del router para navegación programática.

  // Estado inicial del formulario con todos los campos requeridos.
  const [form, setForm] = useState({
    nombre: '',
    apellido: '',
    documentoIdentidad: '',
    correo: '',
    telefono: '',
    ciudad: '',
    pais: '',
    usuario: '',
    contrasena: ''
  });

  const [mensaje, setMensaje] = useState(''); // Estado para mostrar mensajes de éxito o error.

  // Maneja cambios en los campos del formulario (input y select).
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // Envía los datos del formulario al backend y gestiona la respuesta.
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/inversionistas/registro`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });

      if (response.ok) {
        setMensaje('✅ Registro exitoso');
        // Reinicia el formulario tras registro exitoso.
        setForm({
          nombre: '',
          apellido: '',
          documentoIdentidad: '',
          correo: '',
          telefono: '',
          ciudad: '',
          pais: '',
          usuario: '',
          contrasena: ''
        });
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

  // Redirige al usuario a la página principal.
  const volverInicio = () => {
    router.push('/');
  };

  return (
    <main className={styles.container}>
      <section className={styles.formBox}>
        <h2 className={styles.title}>Registro de Inversionista</h2>
        <form onSubmit={handleSubmit} className={styles.form}>
          
          {/* Campo: Nombres */}
          <div className={styles.inputGroup}>
            <label htmlFor="nombre">Nombres</label>
            <input
              type="text"
              name="nombre"
              id="nombre"
              value={form.nombre}
              onChange={handleChange}
              required
              className={styles.input}
            />
          </div>

          {/* Campo: Apellidos */}
          <div className={styles.inputGroup}>
            <label htmlFor="apellido">Apellidos</label>
            <input
              type="text"
              name="apellido"
              id="apellido"
              value={form.apellido}
              onChange={handleChange}
              required
              className={styles.input}
            />
          </div>

          {/* Campo: Documento de Identidad con validación de 6 a 12 dígitos numéricos */}
          <div className={styles.inputGroup}>
            <label htmlFor="documentoIdentidad">Documento de Identidad</label>
            <input
              type="text"
              name="documentoIdentidad"
              id="documentoIdentidad"
              value={form.documentoIdentidad}
              onChange={handleChange}
              required
              className={styles.input}
              minLength={6}
              maxLength={12}
              pattern="\d{6,12}"
              placeholder="6–12 dígitos numéricos"
            />
          </div>

          {/* Campo: Correo Electrónico */}
          <div className={styles.inputGroup}>
            <label htmlFor="correo">Correo Electrónico</label>
            <input
              type="email"
              name="correo"
              id="correo"
              value={form.correo}
              onChange={handleChange}
              required
              className={styles.input}
            />
          </div>

          {/* Campo: Teléfono con formato internacional */}
          <div className={styles.inputGroup}>
            <label htmlFor="telefono">Teléfono</label>
            <PhoneInput
              country={'co'}
              onlyCountries={['co', 'pe', 've', 'ec']}
              value={form.telefono}
              onChange={(phone) => setForm({ ...form, telefono: phone })}
              inputClass={styles.input}
            />
          </div>

          {/* Campo: País con opciones limitadas */}
          <div className={styles.inputGroup}>
            <label htmlFor="pais">País</label>
            <select
              name="pais"
              id="pais"
              value={form.pais}
              onChange={handleChange}
              required
              className={styles.input}
            >
              <option value="">Selecciona tu país</option>
              <option value="Colombia">Colombia</option>
              <option value="Ecuador">Ecuador</option>
              <option value="Perú">Perú</option>
              <option value="Venezuela">Venezuela</option>
            </select>
          </div>

          {/* Campo: Ciudad */}
          <div className={styles.inputGroup}>
            <label htmlFor="ciudad">Ciudad</label>
            <input
              type="text"
              name="ciudad"
              id="ciudad"
              value={form.ciudad}
              onChange={handleChange}
              required
              className={styles.input}
            />
          </div>

          {/* Campo: Usuario con validación alfanumérica */}
          <div className={styles.inputGroup}>
            <label htmlFor="usuario">Usuario</label>
            <input
              type="text"
              name="usuario"
              id="usuario"
              value={form.usuario}
              onChange={handleChange}
              required
              className={styles.input}
              minLength={4}
              maxLength={20}
              pattern="[A-Za-z0-9]{4,20}"
              placeholder="4 a 20 caracteres alfanuméricos"
            />
          </div>

          {/* Campo: Contraseña con validación de seguridad */}
          <div className={styles.inputGroup}>
            <label htmlFor="contrasena">Contraseña</label>
            <input
              type="password"
              name="contrasena"
              id="contrasena"
              value={form.contrasena}
              onChange={handleChange}
              required
              className={styles.input}
              minLength={6}
              maxLength={20}
              pattern="^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,20}$"
              placeholder="Incluir una letra, número y símbolo"
            />
          </div>

          {/* Botones de acción: Registrar y Volver */}
          <div className={styles.buttonGroup}>
            <button type="submit" className={styles.submitButton}>Registrar</button>
            <button type="button" onClick={volverInicio} className={styles.backButton}>Volver</button>
          </div>
        </form>

        {/* Mensaje de estado tras el envío */}
        <p className={styles.message}>{mensaje}</p>
      </section>
    </main>
  );
}
