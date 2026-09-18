import { initializeApp } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-app.js";
import { getAuth, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-auth.js";
import { getFirestore, doc, setDoc, getDoc } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-firestore.js";
import { getStorage, ref, uploadBytesResumable, getDownloadURL } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-storage.js";

// Configuración de Firebase
const firebaseConfig = {
    apiKey: "INSERT YOUR API KEY HERE",
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

// Asegúrate de que el DOM esté cargado antes de interactuar con los elementos
window.addEventListener('DOMContentLoaded', function () {
    const perfilForm = document.getElementById('perfil-form');

    // Verifica si el formulario existe en el DOM antes de agregar el event listener
    if (perfilForm) {
        perfilForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            // Obtener los valores del formulario
            const nombre = document.getElementById('nombre').value;
            const telefono = document.getElementById('telefono').value;
            const direccion = document.getElementById('direccion').value;
            const biografia = document.getElementById('biografia').value;
            const especialidad = document.getElementById('especialidad').value;
            const experiencia = document.getElementById('experiencia').value;
            const horario = document.getElementById('horario').value;
            const precioMinimo = document.getElementById('precio-minimo').value;

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
                const storageRef = ref(storage, `trabajadores/${user.uid}/profileImage.jpg`);
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

            // Subir el archivo de certificaciones (si se seleccionó uno)
            const certFileInput = document.getElementById('certificaciones');
            let certFileUrl = '';

            if (certFileInput.files[0]) {
                const certFile = certFileInput.files[0];
                const certStorageRef = ref(storage, `trabajadores/${user.uid}/certificacion_${certFile.name}`);
                const certUploadTask = uploadBytesResumable(certStorageRef, certFile);

                await new Promise((resolve, reject) => {
                    certUploadTask.on('state_changed',
                        () => {}, // Progress bar (opcional)
                        (error) => {
                            console.error("Error al subir la certificación: ", error);
                            reject(error);
                        },
                        async () => {
                            certFileUrl = await getDownloadURL(certUploadTask.snapshot.ref);
                            resolve();
                        }
                    );
                });
            }

            try {
                await setDoc(doc(db, 'trabajadores', user.uid), {
                    nombre,
                    telefono,
                    direccion,
                    biografia,
                    especialidad,
                    experiencia,
                    horario,
                    precioMinimo,
                    profileImageUrl, // URL de la imagen de perfil
                    certFileUrl // URL del archivo de certificación
                });
            
                // Esperar 3 segundos antes de mostrar el botón "OK"
                Swal.fire({
                    icon: 'success',
                    title: 'Perfil guardado',
                    text: 'Serás redirigido en breve...',
                    allowOutsideClick: false, // Evitar que se cierre antes
                    timer: 3000, // 3 segundos
                    timerProgressBar: true, // Barra de progreso durante la espera
                    showConfirmButton: false // No mostrar botón "OK"
                }).then(() => {
                    // Redirigir automáticamente después de que el temporizador expire
                    window.location.href = 'index1.html'; // Cambia 'index1.html' por la página deseada
                });
            
            } catch (error) {
                console.error("Error al guardar el perfil: ", error);
            }                  
        });

        // Cargar datos del usuario autenticado
        onAuthStateChanged(auth, async (user) => {
            if (user) {
                const docRef = doc(db, 'trabajadores', user.uid);
                try {
                    const docSnap = await getDoc(docRef);
                    if (docSnap.exists()) {
                        const data = docSnap.data();
                        document.getElementById('nombre').value = data.nombre;
                        document.getElementById('telefono').value = data.telefono;
                        document.getElementById('direccion').value = data.direccion;
                        document.getElementById('biografia').value = data.biografia;
                        document.getElementById('especialidad').value = data.especialidad;
                        document.getElementById('experiencia').value = data.experiencia;
                        document.getElementById('horario').value = data.horario;
                        document.getElementById('precio-minimo').value = data.precioMinimo;

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
    } else {
        console.error("No se encontró el formulario en el DOM.");
    }
});
