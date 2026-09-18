<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Barra de navegación</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="buscar.css">

</head>
<body>
    <nav class="green-nav">
        <div class="logo">
            <img src="img/logo.png" alt="Logo">
        </div>
        <ul>
            <li><a href="home.php">INICIO</a></li>
            <li><a href="reportar.php">REPORTAR</a></li>
            <li><a href="comunidad.php">COMUNIDAD</a></li>
            <li><a href="contacto.php">CONTACTO</a></li>
            <li><a href="indicaciones.php">INDICACIONES</a></li>
            <li><a href="logout.php">CERRAR SESIÓN</a></li>
        </ul>
    </nav>

    <div class="header">
        <h1>Buscar Objetos</h1>
    </div>

    <div class="container">
        <div class="row justify-content-center search-input">
            <div class="col-md-8">
                <input type="text" id="searchTerm" class="form-control" placeholder="Buscar...">
            </div>
            <div class="col-md-2">
                <button class="btn btn-block search-btn">Buscar</button>
            </div>
        </div>
    
    <!-- Grid de imágenes -->
    <div class="row">
    <!-- Añadimos 4 columnas para 4 imágenes en una fila -->
    <!-- Repite estas 4 columnas para 4 filas en total -->
        <div class="col-md-3">
            <div class="grid-item">
                <a href="destino6.php">
                    <img src="imagenes/airpods.jpg" class="img-fluid" id="airpods">
                </a>
            </div>
            <div class="grid-item">
                <a href="destino1.php">
                    <img src="imagenes/guitarra.jpg" class="img-fluid" id="guitarra">
                </a>
            </div>
            <div class="grid-item">
                <a href="destino1.php">
                    <img src="imagenes/cargador.jpg" class="img-fluid" id="cargador">
                </a>
            </div>
        </div>
        <div class="col-md-3">
            <div class="grid-item">
                <a href="destino1.php">
                    <img src="imagenes/encendedor.jpg" class="img-fluid" id="encendedor">
                </a>
            </div>
            <div class="grid-item">
                <a href="destino1.php">
                    <img src="imagenes/mac.jpg" class="img-fluid" id="mac">
                </a>
            </div>
            <div class="grid-item">
                <a href="destino1.php">
                    <img src="imagenes/celular2.jpg" class="img-fluid" id="celular2">
                </a>
            </div>
        </div>
        <div class="col-md-3">
            <div class="grid-item">
                <a href="destino1.php">
                    <img src="imagenes/celular3.jpg" class="img-fluid" id="celular3">
                </a>
            </div>
            <div class="grid-item">
                <a href="destino1.php">
                    <img src="imagenes/ipad.jpg" class="img-fluid" id="ipad">
                </a>
            </div>
            <div class="grid-item">
                <a href="destino1.php">
                    <img src="imagenes/jbl.jpg" class="img-fluid" id="jbl">
                </a>
            </div>
        </div>
        <div class="col-md-3">
            <div class="grid-item">
                <a href="destino1.php">
                    <img src="imagenes/psp.jpg" class="img-fluid" id="psp">
                </a>
            </div>
            <div class="grid-item">
                <a href="destino1.php">
                    <img src="imagenes/ps4.jpg" class="img-fluid" id="ps4">
                </a>
            </div>
            <div class="grid-item">
                <a href="destino1.php">
                    <img src="imagenes/audi.jpg" class="img-fluid" id="audi">
                </a>
            </div>
        </div>
</div>
        <script>
        document.querySelector('.search-btn').addEventListener('click', function() {
            let term = document.getElementById('searchTerm').value.toLowerCase();
            let images = document.querySelectorAll('.grid-item img');

            images.forEach(img => {
                if (img.id.includes(term)) {
                    img.parentElement.parentElement.style.display = 'block';
                } else {
                    img.parentElement.parentElement.style.display = 'none';
                }
            });
        });
    </script>

    <!-- Enlazamos JS de Bootstrap -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>