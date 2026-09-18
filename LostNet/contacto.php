<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Barra de navegación</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="home.css">
</head>
<body>
    <nav class="green-nav">
        <div class="logo">
            <img src="img/logo.png" alt="Logo">
        </div>
        <ul>
            <li><a href="home.php">INICIO</a></li>
            <!--<li><a href="users.php">Usuarios</a></li>-->
            <li><a href="reportar.php">REPORTAR</a></li>
            <li><a href="comunidad.php">COMUNIDAD</a></li>
            <li><a href="contacto.php">CONTACTO</a></li>
            <li><a href="indicaciones.php">INDICACIONES</a></li>
            <li><a href="logout.php">CERRAR SESIÓN</a></li>
        </ul>
    </nav>
    <header class="header-color">
    <div class="container">
    </div>
</header>

<div class="container mt-5">
    <h2 class="header-color text-center mb-4">Contáctanos</h2>

    <form>
        <div class="form-group">
            <label for="name">Nombre:</label>
            <input type="text" class="form-control" id="name" placeholder="Ingresa tu nombre">
        </div>
        <div class="form-group">
            <label for="email">Correo electrónico:</label>
            <input type="email" class="form-control" id="email" placeholder="nombre@ejemplo.com">
        </div>
        <div class="form-group">
            <label for="message">Mensaje:</label>
            <textarea class="form-control" id="message" rows="4" placeholder="Escribe tu mensaje..."></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Enviar</button>
    </form>
</div>

<!-- Scripts de Bootstrap -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>