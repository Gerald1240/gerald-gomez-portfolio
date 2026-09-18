<?php
    include("session.php");
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE= dge ">
    <meta name= "viewport" content = "width=device-width,initial-scale=1.0">
    <title>Registracion</title>
    <script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
     <link rel="stylesheet" href="registration.css"> 
</head>
<body1>
    <div class="form-body1">
         <img src="img/logo.png" alt="user-login">
          <p class="text">Registrar Usuario</p>  
        <form action="register.php" method="POST">
         <input type="text" placeholder ="Nombre" name = "firstname" required>
         <input type="text" placeholder ="Segundo nombre" name = "middlename" required>
         <input type="text" placeholder ="Apellido" name = "lastname" required>
         <input type="date" placeholder ="Fecha de Nacimiento"name = "birthdate" required>
         <input type="text" placeholder ="Usuario" name = "username" required>
         <input type="password" placeholder ="Contraseña" name = "password" required>
         <input type="text" placeholder ="idD" name = "idD" required>
         <input type="text" placeholder ="Direccion del Cliente" name = "CD" required>
         <input type="text" placeholder ="Correo Electronico" name = "CE" required>
         <input type="number" placeholder ="Numero de Celular" name = "NumeroT" required>
         <button type="subtmit">Registrarse</button>
         </script>
        </form>
    </div>
</body>
</html>