<?php
include("config.php");
include("session.php");

date_default_timezone_set('America/Mexico_City');
$fecha_actual = date("Y-m-d H:i:s");

if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_FILES["imageUpload"])) {

    $lugar = $_POST['place_found'];
    $categoria = $_POST['categoria'];
    $descripcion = $_POST['description'];
    $imagen = $_FILES['imagen']['tmp_name']; // Esto deberá ser manejado adecuadamente.
    $fecha = $_POST['upload_time'];

    $sql = "INSERT INTO imagenes (place_found, categoria, description, imagen, upload_time) VALUES ('$lugar', '$categoria', '$descripcion', '$imagen', '$fecha')";

    if (mysqli_query($mysqli, $sql)) {
        echo '<script>
            Swal.fire({
                icon: "success",
                title: "Reporte Guardado Correctamente",
                text: "¡Felicidades el reporte fue guardado exitosamente!",
                showConfirmButton: true,
                confirmButtonText: "Cerrar"
            }).then(function(result){
                if(result.value){                   
                    window.location = "home.php";
                }
            });
        </script>';
    } else {
        echo '<script>
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "¡El reporte no pudo ser guardado!",
                showConfirmButton: true,
                confirmButtonText: "Cerrar"
            }).then(function(result){
                if(result.value){                   
                    window.location = "path-to-your-report-form.php"; // Cambia esto a la ruta donde se encuentra tu formulario de reporte.
                }
            });
        </script>';
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Barra de navegación</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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
    <div class="container mt-5">
        <div class="header bg-success text-white p-4 mb-4 rounded-top">
            <h1>REPORTAR</h1>
        </div>
        <div class="bg-white p-4 rounded-bottom">
            <form action="upload_image.php" method="post" enctype="multipart/form-data">
                <div class="row">
                    <div class="col-md-6 d-flex flex-column justify-content-center align-items-center">
                        <input type="file" name="imagen" id="imageUpload" class="mb-3 d-none">
                        <label for="imageUpload">
                            <img src="path-to-default-image.jpg" alt="Seleccionar imagen" class="img-fluid rounded img-upload">
                        </label>
                        <h2 class="mt-3"></h2>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="location">Encontrado en:</label>
                            <input type="text" name="place_found" class="form-control" id="location" placeholder="Lugar donde se encontró" required>
                        </div>
                        <div class="form-group">
                            <label for="objectCategory">Categoría de objeto:</label>
                            <input type="text" name="categoria" class="form-control" id="objectCategory" placeholder="Categoría del objeto" required>
                        </div>
                        <div class="form-group">
                            <label for="objectFeatures">Características:</label>
                            <input type="text" name="description" class="form-control" id="objectFeatures" placeholder="Descripción" required>
                        </div>
                        <div class="form-group">
                            <label for="foundDate">Encontrado el día:</label>
                            <input type="datetime-local" name="upload_time" class="form-control" id="foundDate" required>
                        </div>
                        <button type="submit" class="btn btn-success">Reportar</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <script>
        document.getElementById('imageUpload').addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    document.querySelector('.img-upload').src = e.target.result;
                }
                reader.readAsDataURL(file);
            }
        });

        document.getElementById('reportForm').addEventListener('submit', function(event) {
            event.preventDefault(); // detiene la acción predeterminada del formulario

            const location = document.getElementById('location').value;
            const objectCategory = document.getElementById('objectCategory').value;
            const objectFeatures = document.getElementById('objectFeatures').value;
            const foundDate = document.getElementById('foundDate').value;

            if (!location || !objectCategory || !objectFeatures || !foundDate) {
                Swal.fire({
                    icon: 'error',
                    title: 'Campos vacíos',
                    text: 'Por favor, completa todos los campos antes de enviar el reporte.',
                });
                return;
            }

            Swal.fire({
                icon: 'success',
                title: 'Producto reportado',
                text: 'Tu reporte ha sido enviado exitosamente.',
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = 'home.php';
                }
            });
        });
    </script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-GgRlCq5rxc4Swm9zmmTmD69ZkBJ8eACo57VfVGL5rEX5dpeT/P9G4+X+Y1xWTVkF" crossorigin="anonymous"></script>
</body>
</html>
