<?php
include ("config.php");
include ("session.php");
include("script.php");
$id= $_POST ['id'];
$firstname = $_POST ['firstname'] ;
$middlename = $_POST ['middlename'] ;
$lastname = $_POST ['lastname'];
$birthdate=$_POST ['birthdate'] ;
$username = $_POST ['username'] ;
$password = $_POST ['password'] ;
$NumeroT=$_POST ['NumeroT'];
$idD=$_POST ['idD'];
$CD=$_POST ['CD'];
$CE=$_POST['CE'];
$sql = "UPDATE users SET firstname = ' $firstname ' , middlename = '$middlename' , last_name = ' $lastname' , birthday ='$birthdate', username=  '$username' ,password= '$password',NumeroT = '$NumeroT',idD = '$idD', CD = '$CD', CE = '$CE'
WHERE username = '$id' ";
if (mysqli_query($mysqli,$sql) ) 
{
echo '<script>
    Swal.fire({
        icon: "success",
        title: "Usuario Actualizado",
        text: "¡El usuario fue modificado exitosamente!",
        showConfirmButton: true,
        confirmButtonText: "OK"
        }).then(function(result){
           if(result.value){                   
            window.location = "users.php";
    } 
 });
</script>';
} 
 else 
{
echo '<script>
    Swal.fire({
        icon: "error",
        title: "Usuario No Actualizado",
        text: "¡El usuario nop fue modificado exitosamente!",
        showConfirmButton: true,
        confirmButtonText: "OK"
        }).then(function(result){
           if(result.value){                   
            window.location = "users.php";
    }
 });
</script>';
} 
?>
