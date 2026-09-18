<?php
    include("session.php");

    if(isset($POST['search']))
    {

            $valueToSearch = $POST[ 'valueToSearch'];
            $query = "SELECT * FROM users WHERE firstname LIKE '%".$valueToSearch."%' OR last_name LIKE '%".$valueToSearch."%'";
            $result = filterRecord($query);
    }
    else
    {
            $query = "SELECT *FROM users";
            $result = filterRecord($query);
    }
    function filterRecord($query)
    {
        include("config.php");
        $filter_result = mysqli_query($mysqli, $query);
        return $filter_result;
    }
?>
<!DOCTYPE html>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT" crossorigin="anonymous">
<link rel="stylesheet" href="style.css">
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE= dge ">
    <meta name= "viewport" content = "width=device-width,initial-scale=1.0">
    <link rel="stylesheet" href="users.css">
    <title >Usuarios</title>
</head>
<body>
 
<hr/>

<div class="form-body">
     <img src="img/logo.png" alt="user-login">
     <p class="text">Usuarios</p>
     <form action="" method="POST">
         <input type="search" name="valueToSearch" placeholder="Búsqueda" >
         <button type="submit" class="signupbtn" name="search">Buscar</button>
    </form>
</div>


<?php

echo "<table border='1'>
<tr>
<th>Nombre</th>
<th>Apellido</th>
<th>Segundo Apellido</th>
<th>»Fecha Nacimiento</th>
<th>»id</th>
<th>»Direccion</th>
<th>»Correo Electronico</th>
<th>»Numero de Celular</th>
<th>Actualizar</th>
<th>Eliminar</th>
</tr>";

while($row = mysqli_fetch_array($result))
{
echo "<tr>";
echo "<td>" . $row['firstname'] . "</td>";
echo "<td>" . $row['middlename'] . "</td>";
echo "<td>" . $row['last_name'] . "</td>";
echo "<td>" . $row['birthday'] . "</td>";
echo "<td>" . $row['idD'] . "</td>";
echo "<td>" . $row['CD'] . "</td>";
echo "<td>" . $row['CE'] . "</td>";
echo "<td>" . $row['NumeroT'] . "</td>";
echo "<td><a href='edit.php?id=".$row['username']."'>Editar</a></td>";
echo "<td><a href='delete.php?id=".$row['username']."'>Eliminar</a></td>";
echo "</tr>";
}
echo "</table>";
?>

</body>
</html>
