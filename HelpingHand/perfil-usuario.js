import { initializeApp } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-app.js";
import { getAuth, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-auth.js";
import { getFirestore, doc, setDoc, getDoc } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-firestore.js";
import { getStorage, ref, uploadBytesResumable, getDownloadURL } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-storage.js";

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

// Inicializa Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getFirestore(app);
const storage = getStorage(app);

// Guardar la información del formulario en Firestore y Firebase Storage
const perfilForm = document.getElementById('perfil-form');
perfilForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Obtener los valores del formulario
    const nombre = document.getElementById('nombre').value;
    const telefono = document.getElementById('telefono').value;
    const direccion = document.getElementById('direccion').value;
    const ciudad = document.getElementById('ciudad').value;
    const distrito = document.getElementById('distrito').value;
    const fechaNacimiento = document.getElementById('fecha-nacimiento').value;
    const genero = document.getElementById('genero').value;
    const preferencias = document.getElementById('preferencias').value;

    // Obtener el usuario autenticado
    const user = auth.currentUser;
    if (!user) {
        console.log("Usuario no autenticado");
        return;
    }

    // Subir la imagen de perfil (si se seleccionó una)
    const fileInput = document.getElementById('foto-perfil');
    let profileImageUrl = '';

    if (fileInput.files[0]) {
        const file = fileInput.files[0];
        const storageRef = ref(storage, `usuarios/${user.uid}/profileImage.jpg`);
        const uploadTask = uploadBytesResumable(storageRef, file);

        await new Promise((resolve, reject) => {
            uploadTask.on('state_changed', 
                () => {}, // Progress bar (opcional)
                (error) => {
                    console.error("Error al subir la imagen: ", error);
                    reject(error);
                }, 
                async () => {
                    profileImageUrl = await getDownloadURL(uploadTask.snapshot.ref);
                    resolve();
                }
            );
        });
    }

    // Guardar los datos en Firestore
    try {
        await setDoc(doc(db, 'usuarios', user.uid), {
            nombre,
            telefono,
            direccion,
            ciudad,
            distrito,
            fechaNacimiento,
            genero,
            preferencias,
            profileImageUrl // URL de la imagen de perfil
        });
        Swal.fire({
            icon: 'success',
            title: 'Perfil guardado',
            text: 'Tu perfil ha sido guardado exitosamente.',
            confirmButtonText: 'OK'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = 'index2.html';
            }
        });
    } catch (error) {
        console.error("Error al guardar el perfil: ", error);
        Swal.fire({
            icon: 'error',
            title: 'Error al guardar',
            text: 'Hubo un error al guardar tu perfil. Intenta nuevamente.',
        });
    }
});

// Cargar datos del usuario autenticado
onAuthStateChanged(auth, async (user) => {
    if (user) {
        const docRef = doc(db, 'usuarios', user.uid);
        try {
            const docSnap = await getDoc(docRef);
            if (docSnap.exists()) {
                const data = docSnap.data();
                document.getElementById('nombre').value = data.nombre;
                document.getElementById('telefono').value = data.telefono;
                document.getElementById('direccion').value = data.direccion;
                document.getElementById('ciudad').value = data.ciudad;
                document.getElementById('distrito').value = data.distrito;
                document.getElementById('fecha-nacimiento').value = data.fechaNacimiento;
                document.getElementById('genero').value = data.genero;
                document.getElementById('preferencias').value = data.preferencias;

                // Mostrar la imagen de perfil si existe
                if (data.profileImageUrl) {
                    document.getElementById('profile-img').src = data.profileImageUrl;
                }
            } else {
                console.log("No se encontraron datos del perfil");
            }
        } catch (error) {
            console.log("Error al obtener el documento: ", error);
        }
    } else {
        console.log("Usuario no autenticado");
    }
});
