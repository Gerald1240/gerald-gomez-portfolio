import { initializeApp } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-app.js";
import { getAuth, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-auth.js";
import { getFirestore, doc, getDoc } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-firestore.js";

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
const auth = getAuth();
const db = getFirestore(app);

// Asegúrate de que el DOM esté cargado antes de ejecutar el código
document.addEventListener("DOMContentLoaded", () => {
    // Cargar imagen de perfil cuando el usuario esté autenticado
    onAuthStateChanged(auth, async (user) => {
        if (user) {
            const userDoc = await getDoc(doc(db, 'usuarios', user.uid));
            if (userDoc.exists()) {
                const userData = userDoc.data();
                const userAvatar = document.getElementById('user-avatar');
                
                if (userData.profileImageUrl && userAvatar) {
                    userAvatar.src = userData.profileImageUrl; // Cambiar imagen de perfil
                }
            } else {
                console.log("No se encontró el documento del usuario");
            }
        } else {
            console.log("No hay usuario autenticado");
        }
    });

    // Mostrar y ocultar el menú cuando se hace clic en la imagen del usuario
    const userAvatar = document.getElementById('user-avatar');
    const userMenu = document.getElementById('user-menu');

    if (userAvatar && userMenu) {
        // Al hacer clic en el avatar del usuario, mostrar/ocultar el menú
        userAvatar.addEventListener('click', (event) => {
            event.stopPropagation(); // Evitar el cierre inmediato al hacer clic
            userMenu.classList.toggle('show');
        });

        // Cerrar el menú si se hace clic fuera del menú o del avatar
        document.addEventListener('click', (event) => {
            if (!userMenu.contains(event.target) && !userAvatar.contains(event.target)) {
                userMenu.classList.remove('show');
            }
        });
    }
});
