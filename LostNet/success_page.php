<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Barra de navegación</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
    <link rel="stylesheet" href="reportar.css">
</head>
<body class="bg-light">
    <nav class="green-nav">
        <div class="logo">
            <img src="img/logo.png" alt="Logo">
        </div>
        <ul>
            <li><a href="home.php">INICIO</a></li>
            <li><a href="reportar.php">REPORTAR</a></li>
            <li><a href="https://www.flickr.com/photos/198612506@N04/albums">COMUNIDAD</a></li>
            <li><a href="contacto.php">CONTACTO</a></li>
            <li><a href="indicaciones.php">INDICACIONES</a></li>
            <li><a href="logout.php">CERRAR SESIÓN</a></li>
        </ul>
    </nav>

    <script>
    // Mostrar la alerta SweetAlert
    Swal.fire({
        icon: 'success',
        title: '¡Operación exitosa!',
        text: 'La imagen se subió correctamente.',
        confirmButtonText: 'Listo'
    }).then((result) => {
        // Redireccionar a la página de inicio al hacer clic en el botón OK
        if (result.isConfirmed) {
            window.location.href = 'home.php'; // Esta es la redirección al home.
        }
    });
    </script>

</body>
</html>
