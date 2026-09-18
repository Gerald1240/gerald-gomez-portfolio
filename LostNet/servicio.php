<?php
date_default_timezone_set('America/Mexico_City');
$fecha_actual=date("Y-m-d H:i:s")
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE= dge ">
    <meta name= "viewport" content = "width=device-width,initial-scale=1.0">
    <title>Servicios</title>
    <script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
    <link rel="stylesheet" href="registration.css"> 
</head>
<body1>
    <div class="form-body1">
         <img src="img/logo.png" alt="user-login">
          <p class="text">Servicios</p>  
        <form action="rservicio.php" method="POST">
         <input type="number" placeholder ="ID del Servicio" name = "IDservicio" required>
         <input type="text" placeholder ="Nombre del Servicio" name = "NombreSER" required>
         <input type="number" placeholder ="Costo del Servicio" name = "Costo" required>
         <type="datetime" name="fecha_actual" value="<?=$fecha_actual?>" required>
         <button type="subtmit">Enviar</button>
         </script>
        </form>
    </div>
</body>  
</html>