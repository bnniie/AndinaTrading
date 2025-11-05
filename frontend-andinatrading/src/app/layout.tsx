// Este componente define la estructura raíz del documento HTML para toda la aplicación.
// Se utiliza en Next.js como layout global, envolviendo todas las páginas con <html>, <head> y <body>.

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    // Define el idioma principal del documento como español.
    <html lang="es">
      <head>
        {/* Título que aparece en la pestaña del navegador */}
        <title>Andina Trading</title>

        {/* Meta descripción para motores de búsqueda y redes sociales */}
        <meta
          name="description"
          content="Tu dinero, bajo control. Tus decisiones, con inteligencia."
        />

        {/* Ícono que se muestra en la pestaña del navegador */}
        <link rel="icon" href="/favicon.ico" />
      </head>

      {/* Cuerpo del documento donde se renderiza el contenido de cada página */}
      <body>{children}</body>
    </html>
  );
}
