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
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
            <li><a href="https://www.flickr.com/photos/198612506@N04/albums">COMUNIDAD</a></li>
            <li><a href="contacto.php">CONTACTO</a></li>
            <li><a href="indicaciones.php">INDICACIONES</a></li>
            <li><a href="buscar.php">BUSCAR</a></li>
            <li><a href="logout.php">CERRAR</a></li>
        </ul>
    </nav>

    <div class="content-container">
        <div class="percentage-container">
            <canvas id="percentageChart" width="300" height="300"></canvas>
            <div class="percentage-label" style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); font-size: 24px; font-weight: bold;">
                
            </div>
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", function() {
                var ctx = document.getElementById('percentageChart').getContext('2d');
                var percentageValue = 83;  // Puedes cambiar este valor

                var chart = new Chart(ctx, {
                    type: 'doughnut',
                    data: {
                        labels: ["Porcentaje", "Restante"],
                        datasets: [{
                            data: [percentageValue, 100 - percentageValue],
                            backgroundColor: ["#4CAF50", "#ddd"],
                            borderColor: ["#4CAF50", "#ddd"],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        cutout: '80%', // Esto hace que el gráfico sea un anillo en lugar de un pastel completo
                        plugins: {
                            legend: {
                                display: false // Esto oculta la leyenda
                            }
                        }
                    }
                });
            });
        </script>

        <div class="items-grid">
            <a href="destino1.php" class="item" data-img="image1"></a>
            <a href="destino2.php" class="item" data-img="image2"></a>
            <a href="destino3.php" class="item" data-img="image3"></a>
            <a href="destino4.php" class="item" data-img="image4"></a>
            <a href="destino5.php" class="item" data-img="image5"></a>
            <a href="destino6.php" class="item" data-img="image6"></a>
        </div>

    </div>

</body>
</html>