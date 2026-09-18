import { ManageAccount } from './firebaseconect.js';

// Función para registrar usuarios
document.getElementById("register-form").addEventListener("submit", function(event) {
    event.preventDefault();
    
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const accountType = document.getElementById("account-type").value; // Tipo de cuenta (Usuario o Trabajador)

    createUserWithEmailAndPassword(auth, email, password)
        .then((userCredential) => {
            const user = userCredential.user;

            // Guardar información en Firestore en la colección 'usuarios'
            return setDoc(doc(db, 'usuarios', user.uid), {
                email: user.email,
                accountType: accountType, // Guardamos si es Usuario o Trabajador
            })
            .then(() => {
                if (accountType === 'Trabajador') {
                    // Si es Trabajador, también guarda en la colección 'trabajadores'
                    return setDoc(doc(db, 'trabajadores', user.uid), {
                        email: user.email,
                        // Puedes agregar aquí cualquier otro dato que quieras guardar en la colección 'trabajadores'
                        accountType: accountType,
                    });
                }
            });
        })
        .then(() => {
            Swal.fire({
                icon: 'success',
                title: 'Registro Exitoso',
                text: 'Serás redirigido a la página de inicio de sesión.',
                timer: 2000,
                timerProgressBar: true,
                showConfirmButton: false
            }).then(() => {
                window.location.href = "index1.html"; // Redirigir a la página principal
            });
        })
        .catch((error) => {
            console.error("Error al registrar: ", error);
            Swal.fire({
                icon: 'error',
                title: 'Error al Registrar',
                text: error.message,
            });
        });
});

// Función de autenticación (inicio de sesión)
document.getElementById("login-form").addEventListener("submit", function(event) {
    event.preventDefault();
    
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    signInWithEmailAndPassword(auth, email, password)
        .then((userCredential) => {
            const user = userCredential.user;

            // Obtener los datos del usuario desde Firestore
            const docRef = doc(db, 'usuarios', user.uid);
            getDoc(docRef).then((docSnap) => {
                if (docSnap.exists()) {
                    const userData = docSnap.data();
                    
                    // Verifica el tipo de cuenta y redirige a la página correcta
                    if (userData.accountType === 'Trabajador') {
                        // Redirigir a la página de Trabajador
                        window.location.href = "index1.html";
                    } else if (userData.accountType === 'Usuario') {
                        // Redirigir a la página de Usuario
                        window.location.href = "index2.html";
                    }
                } else {
                    console.log("No se encontró el documento del usuario.");
                }
            }).catch((error) => {
                console.error("Error al obtener datos del usuario: ", error);
            });
        })
        .catch((error) => {
            console.error("Error al iniciar sesión: ", error);
            Swal.fire({
                icon: 'error',
                title: 'Error al Iniciar Sesión',
                text: error.message,
            });
        });
});
