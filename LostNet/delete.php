<?php
include ("config.php");
include ("session.php");
include("home.php");
$id= $_GET ['id'];

$sql = "DELETE FROM users WHERE username='$id'";

if (mysqli_query($mysqli,$sql) ) 
{
          echo '<script>
              Swal.fire({
               icon: "success",
               title: "Usuario Eliminado Correctamente",
               text: "¡El Usuario se elimino correctamente!",
               showConfirmButton: true,
               confirmButtonText: "OK"
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
               title: "Oopss...",
               text: "¡El usuario no fue Eliminado!",
               showConfirmButton: true,
               confirmButtonText: "OK"
               }).then(function(result){
                  if(result.value){                   
                   window.location = "registration.php";
            }
         });
        </script>';
}
?>