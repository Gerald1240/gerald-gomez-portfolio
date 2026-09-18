
<?php
     include ( "config.php" ) ;
     include ( "session.php" ) ;
     include("script.php");
     $firstname=$_POST ['firstname'];
     $middlename=$_POST ['middlename'];
     $lastname=$_POST['lastname'];
     $birthdate=$_POST ['birthdate'] ;
     $username=$_POST ['username'] ;
     $password=$_POST ['password'] ;
     $NumeroT=$_POST ['NumeroT'];
     $idD=$_POST ['idD'];
     $CD=$_POST ['CD'];
     $CE=$_POST['CE'];
         
     $sql = " INSERT INTO users (firstname,middlename,last_name,birthday,username,password,idD,NumeroT,CD,CE) VALUES
     ('$firstname','$middlename','$lastname','$birthdate','$username','$password','$idD','$NumeroT','$CD','$CE')";
     if ( mysqli_query ( $mysqli , $sql ) )
     {
          echo '<script>
              Swal.fire({
               icon: "success",
               title: "Usuario Registrado Correctamente",
               text: "¡Felicidades su Usuario fue registrado exitosamente!",
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
           text: "¡El usuario no pudo ser registrado!",
           showConfirmButton: true,
           confirmButtonText: "Cerrar"
           }).then(function(result){
              if(result.value){                   
               window.location = "registration.php";
            }
         });
        </script>';
     }            
?>
