<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Barra de navegación</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="destino.css">
</head>
<body>
    <nav class="green-nav">
        <div class="logo">
            <img src="img/logo.png" alt="Logo">
        </div>
        <ul>
            <li><a href="home.php">INICIO</a></li>
            <!--<li><a href="users.php">Usuarios</a></li>-->
            <li><a href="servicio.php">REPORTAR</a></li>
            <li><a href="https://www.flickr.com/photos/198612506@N04/albums">COMUNIDAD</a></li>
            <li><a href="servicio.php">CONTACTO</a></li>
            <li><a href="servicio.php">INDICACIONES</a></li>
            <li><a href="logout.php">CERRAR SESIÓN</a></li>
        </ul>
    </nav>
    <div class="top-bar">
    3 PARAGUAS
</div>

<div class="container">
    <div class="content">
        <div class="left-content">
            <div class="image-container">
                <img src="img/image4.jpg" alt="Producto">
            </div>
        </div>
        <div class="right-content">
            <div class="form-group">
                <label for="encontrado-en">Encontrado en:</label>
                <div id="encontrado-en">Entrada de San Jerónimo</div>
            </div>
            <div class="form-group">
                <label for="categoria-producto">Categoría del producto:</label>
                <div id="categoria-producto">Paraguas</div>
            </div>
            <div class="form-group">
                <label for="caracteristicas">Características:</label>
                <div id="caracteristicas">rojo, negro, verde</div>
            </div>
            <div class="form-group">
                <label for="fecha-encontrado">Encontrado el:</label>
                <div id="fecha-encontrado">23/09/2023</div>
            </div>
        </div>
</div>

</body>
</html>
</body>