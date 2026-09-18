<?php
     include ( "config.php" ) ;
     include ( "session.php" ) ;
     include("script.php");
     $IDservicio=$_POST ['IDservicio'];
     $NombreSER=$_POST ['NombreSER'];
     $Costo=$_POST['Costo'];
     $sql = " INSERT INTO serviciosclientes (IDservicio,NombreSER,Costo) VALUES
     ('$IDservicio','$NombreSER','$Costo')";
     if ( mysqli_query ( $mysqli , $sql ) )
     {
          echo '<script>
              Swal.fire({
               icon: "success",
               title: " Servicio enviado Correctamente",
               text: "¡Felicidades su servicio fue registrado exitosamente!",
               showConfirmButton: true,
               confirmButtonText: "Cerrar"
               }).then(function(result){
                  if(result.value){                   
                   window.location = "home.php";
            }
         });
        </script>';
     }
     else
     {
          echo '<script>
          Swal.fire({
           icon: "error",
           title: "Oops...",
           text: "¡El servicio no pudo ser registrado!",
           showConfirmButton: true,
           confirmButtonText: "Cerrar"
           }).then(function(result){
              if(result.value){                   
               window.location = "servicio.php";
            }
         });
        </script>';
     }            
?>
