<?php

include("config.php"); // Archivo de configuración de la base de datos.
include("session.php"); // Archivo para manejar sesiones.

if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_FILES["imagen"])) {

    // Verificar si la imagen ha sido cargada correctamente.
    if ($_FILES["imagen"]["error"] == 0) {

        $uploadDirectory = "imagenes_subidas"; // El nombre de la carpeta donde deseas guardar las imágenes.

        // Crear la carpeta si no existe.
        if (!is_dir($uploadDirectory)) {
            mkdir($uploadDirectory, 0777, true); // Crea la carpeta con permisos de escritura.
        }

        $uploadFile = $uploadDirectory . '/' . basename($_FILES["imagen"]["name"]);

        // Mueve el archivo desde el directorio temporal a tu directorio de destino.
        if (move_uploaded_file($_FILES["imagen"]["tmp_name"], $uploadFile)) {

            $lugar = $_POST['place_found'];
            $categoria = $_POST['categoria'];
            $descripcion = $_POST['description'];
            $fecha = $_POST['upload_time'];

            // Almacenar información en la base de datos.
            $sql = "INSERT INTO imagenes (place_found, categoria, description, imagen, upload_time) VALUES ('$lugar', '$categoria', '$descripcion', '$uploadFile', '$fecha')";
            if (mysqli_query($mysqli, $sql)) {
                // Si todo va bien, redirige o muestra un mensaje de éxito.
                header("Location: success_page.php");
            } else {
                // Manejar error al guardar en la base de datos.
                echo "Error al guardar en la base de datos: " . mysqli_error($mysqli);
            }

        } else {
            // Manejar error al mover el archivo.
            echo "Error al subir la imagen.";
        }

    } else {
        // Manejar error en la carga de la imagen.
        echo "Error en la carga de la imagen: " . $_FILES["imagen"]["error"];
    }
}

?>
