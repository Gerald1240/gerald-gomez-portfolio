// Importación de Firebase y configuración
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-analytics.js";
import { getAuth, createUserWithEmailAndPassword, signInWithEmailAndPassword, signOut, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-auth.js";
import { getFirestore, doc, getDoc, setDoc } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-firestore.js";

// Configuración de Firebase
const firebaseConfig = {
    apiKey: "AIzaSyDXSX9dWx9UmRAP_aXeC7GztpBYCVgibk4",
    authDomain: "helpinghandaqp.firebaseapp.com",
    projectId: "helpinghandaqp",
    storageBucket: "helpinghandaqp.appspot.com",
    messagingSenderId: "416600013741",
    appId: "1:416600013741:web:09885f551a1e0f4539ff24",
    measurementId: "G-2FE5FHXFCR"
};

const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
const auth = getAuth();
const db = getFirestore(app);

// Clase para administrar cuentas
export class ManageAccount {
  register(email, password, nombre, accountType) {
    createUserWithEmailAndPassword(auth, email, password)
      .then((userCredential) => {
        const user = userCredential.user;

        // Guardar información adicional del usuario en Firestore
        return setDoc(doc(db, 'usuarios', user.uid), {
          email: user.email,
          nombre: nombre, // Guardamos el nombre del usuario
          accountType: accountType // Usuario o Trabajador
        }).then(() => {
          // Mostrar el Swal.fire por 3 segundos y luego redirigir según el tipo de cuenta
          Swal.fire({
            icon: 'success',
            title: 'Registro Exitoso',
            text: 'Serás redirigido en unos momentos...',
            timer: 3000, // Tiempo de espera de 3 segundos
            timerProgressBar: true,
            showConfirmButton: false
          }).then(() => {
            // Redirigir después de que Swal.fire desaparezca
            if (accountType === 'Trabajador') {
              window.location.href = "index1.html"; // Redirigir a la página de Trabajador
            } else if (accountType === 'Usuario') {
              window.location.href = "index2.html"; // Redirigir a la página de Usuario
            }
          });
        });
      })
      .catch((error) => {
        console.error(error.message);
        Swal.fire({
          icon: 'error',
          title: 'Error al Registrar',
          text: error.message,
        });
      });
  }

  authenticate(email, password) {
    signInWithEmailAndPassword(auth, email, password)
      .then((userCredential) => {
        const user = userCredential.user;

        // Verificar el tipo de cuenta y redirigir
        const docRef = doc(db, 'usuarios', user.uid);
        return getDoc(docRef).then((docSnap) => {
          if (docSnap.exists()) {
            const userData = docSnap.data();
            if (userData.accountType === 'Trabajador') {
              Swal.fire({
                icon: 'success',
                title: 'Inicio de Sesión Exitoso',
                text: 'Redirigiendo a la página de Trabajador...',
                timer: 3000, // Tiempo de espera de 3 segundos
                timerProgressBar: true,
                showConfirmButton: false
              }).then(() => {
                window.location.href = "index1.html"; // Redirigir a la página de Trabajador
              });
            } else if (userData.accountType === 'Usuario') {
              Swal.fire({
                icon: 'success',
                title: 'Inicio de Sesión Exitoso',
                text: 'Redirigiendo a la página de Usuario...',
                timer: 3000, // Tiempo de espera de 3 segundos
                timerProgressBar: true,
                showConfirmButton: false
              }).then(() => {
                window.location.href = "index2.html"; // Redirigir a la página de Usuario
              });
            }
          } else {
            Swal.fire({
              icon: 'error',
              title: 'Error al Iniciar Sesión',
              text: 'No se encontró el tipo de cuenta.',
            });
          }
        });
      })
      .catch((error) => {
        console.error(error.message);
        Swal.fire({
          icon: 'error',
          title: 'Error al Iniciar Sesión',
          text: error.message,
        });
      });
  }

  signOut() {
    signOut(auth)
      .then(() => {
        Swal.fire({
          icon: 'success',
          title: 'Sesión cerrada',
          text: 'Redirigiendo a la página de inicio...',
          timer: 3000, // Tiempo de espera de 3 segundos
          timerProgressBar: true,
          showConfirmButton: false
        }).then(() => {
          window.location.href = "index.html"; // Redirigir a la página de inicio después de cerrar sesión
        });
      })
      .catch((error) => {
        console.error(error.message);
        Swal.fire({
          icon: 'error',
          title: 'Error al Cerrar Sesión',
          text: error.message,
        });
      });
  }
}

// Asegurar que el DOM esté cargado antes de añadir el event listener para registro
document.addEventListener("DOMContentLoaded", function() {
  const registerForm = document.getElementById("register-form");
  const loginForm = document.getElementById("login-form");

  // Manejar el formulario de registro
  if (registerForm) {
    registerForm.addEventListener("submit", (event) => {
      event.preventDefault(); // Evitar el comportamiento por defecto del formulario

      // Obtener los valores de los campos
      const nombre = document.querySelector('input[placeholder="Nombre"]').value;
      const email = document.getElementById("register-email").value;
      const password = document.getElementById("register-password").value;
      const tipoCuenta = document.getElementById("account-type").value; // Usuario o Trabajador

      // Verificar que todos los campos estén completos
      if (!nombre || !email || !password || !tipoCuenta) {
        Swal.fire({
          icon: 'error',
          title: 'Campos incompletos',
          text: 'Por favor, rellena todos los campos.',
        });
        return;
      }

      // Crear una nueva cuenta
      const account = new ManageAccount();
      account.register(email, password, nombre, tipoCuenta);

      console.log('Formulario de Registro enviado');
    });
  }

  // Manejar el formulario de inicio de sesión
  if (loginForm) {
    loginForm.addEventListener("submit", (event) => {
      event.preventDefault(); // Evitar el comportamiento por defecto del formulario

      // Obtener los valores de los campos
      const email = document.getElementById("login-email").value;
      const password = document.getElementById("login-password").value;

      // Verificar que ambos campos estén completos
      if (!email || !password) {
        Swal.fire({
          icon: 'error',
          title: 'Campos incompletos',
          text: 'Por favor, rellena ambos campos.',
        });
        return;
      }

      // Autenticar al usuario
      const account = new ManageAccount();
      account.authenticate(email, password);

      console.log('Formulario de Inicio de Sesión enviado');
    });
  }
});
