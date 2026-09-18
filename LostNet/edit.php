<?php
    include("session.php");
    include("config.php");
    $id=$_GET['id'];
?>
<!DOCTYPE html>
<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
<link rel="stylesheet" href="style.css">
<html>
    <head>
        <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE= dge ">
    <meta name= "viewport" content = "width=device-width,initial-scale=1.0">
    <link rel="stylesheet" href="style.css">
    <title >Usuarios</title>
    </head>
    <body>
        <div class="form-body">
        <img src="img/logo.png" alt="user-login">
        <p class="text">Edite su Informacion</p>
        <form action="update.php" method="POST">
            <?php
            $result=mysqli_query($mysqli, "SELECT * FROM users WHERE username='$id'");
            while($row=mysqli_fetch_array($result))
            {
                echo"<input type='hidden' name='id' value='{$row['username']}' required>";
                echo"<input type='text' name='firstname' value='{$row['firstname']}' required>";
                echo"<input type='text' name='middlename' value='{$row[ 'middlename']}' required>";
                echo"<input type='text' name='lastname' value='{$row['last_name']}' required>";
                echo"<input type='date' name='birthdate' value='{$row[ 'birthday']}' required>";
                echo"<input type='text' name='username' value='{$row[ 'username']}' required>";
                echo"<input type='password' name='password' value='{$row['password']}' required>";
                echo"<input type='number' name='NumeroT' value='{$row['NumeroT']}' required>";
                echo"<input type='text' name='idD' value='{$row['idD']}' required>";
                echo"<input type='text' name='CD' value='{$row['CD']}' required>";
                echo"<input type='text' name='CE' value='{$row['CE']}' required>";
                echo"<button type='submit' onclick= >Actualizar</button>";
            }
            ?>
     </form>
  </body>
</html>
