<?php
include ("config.php");
include ("session.php");
include("home.php");

$dsn = "mysql:host=$host;dbname=$db;charset=$charset";
$options = [
    PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    PDO::ATTR_EMULATE_PREPARES   => false,
];

$pdo = new PDO($dsn, $user, $pass, $options);

$nombreProducto = $_POST['nombreProducto'] ?? '';
$producto = [];

if ($nombreProducto) {
    $stmt = $pdo->prepare("SELECT * FROM productos WHERE nombre = :nombre");
    $stmt->execute(['nombre' => $nombreProducto]);
    $producto = $stmt->fetch();
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
    <body>
    <form action="" method="post">
        <label for="nombreProducto">Nombre del producto:</label>
        <input type="text" id="nombreProducto" name="nombreProducto">
        <input type="submit" value="Buscar">
    </form>

    <?php if($producto): ?>
        <div>
            <h2><?php echo $producto['nombre']; ?></h2>
            <img src="<?php echo $producto['imagen']; ?>" alt="<?php echo $producto['nombre']; ?>">
        </div>
    <?php elseif($nombreProducto): ?>
        <p>No se encontró el producto con el nombre <?php echo htmlspecialchars($nombreProducto); ?>.</p>
    <?php endif; ?>
</body>
</html>