// Obtener el id del trabajador desde la URL
const urlParams = new URLSearchParams(window.location.search);
const trabajadorId = urlParams.get('trabajadorId'); // Asegurarnos de que coincida con el parámetro de la URL

// Conectar con Firebase para obtener los detalles del trabajador
import { getFirestore, doc, getDoc } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-firestore.js";
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-app.js";

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
const db = getFirestore(app);

// Función para cargar la información del trabajador
async function cargarTrabajador(trabajadorId) {
    const docRef = doc(db, 'trabajadores', trabajadorId);
    const docSnap = await getDoc(docRef);

    if (docSnap.exists()) {
        const data = docSnap.data();
        
        // Rellenar los campos con la información del trabajador
        document.getElementById('nombre-trabajador').innerText = data.nombre || 'Nombre no disponible';
        document.getElementById('presentacion-trabajador').innerText = data.biografia || 'Descripción no disponible';
        document.getElementById('telefono-trabajador').innerText = data.telefono || 'Teléfono no disponible';
        document.getElementById('direccion-trabajador').innerText = data.direccion || 'Dirección no disponible';
        document.getElementById('especialidad-trabajador').innerText = data.especialidad || 'Especialidad no disponible';
        document.getElementById('experiencia-trabajador').innerText = data.experiencia || 'Experiencia no disponible';
        document.getElementById('horario-trabajador').innerText = data.horario || 'Horario no disponible';
        document.getElementById('precio-minimo-trabajador').innerText = data.precioMinimo ? `Desde ${data.precioMinimo} soles/servicio` : 'Precio no disponible';
        
        // Mostrar la imagen de perfil si existe
        document.getElementById('profile-img').src = data.profileImageUrl || 'ruta/a/imagen/default.jpg';

        // Mostrar la certificación si existe
        const certUrl = data.certFileUrl || '#';
        document.getElementById('certificacion-url').href = certUrl;
        document.getElementById('certificacion-url').innerText = certUrl !== '#' ? 'Ver certificación' : 'Certificación no disponible';
        
    } else {
        console.log("No se encontró el trabajador.");
    }
}

// Cargar la información del trabajador al cargar la página
if (trabajadorId) {
    cargarTrabajador(trabajadorId);
} else {
    console.log("No se encontró el ID del trabajador en la URL.");
}

// Obtener elementos del DOM
const modal = document.getElementById('modal-contratar');
const contratarBtn = document.getElementById('contratar-btn'); // Botón de contratar
const closeBtn = document.querySelector('.close-btn'); // Botón para cerrar el modal

// Mostrar el modal al hacer clic en "Contratar"
contratarBtn.addEventListener('click', function(event) {
  event.preventDefault();
  modal.style.display = 'flex'; // Mostrar el modal
});

// Cerrar el modal al hacer clic en la "X"
closeBtn.addEventListener('click', function() {
  modal.style.display = 'none'; // Ocultar el modal
});

// Cerrar el modal si se hace clic fuera del contenido del modal
window.addEventListener('click', function(event) {
  if (event.target === modal) {
    modal.style.display = 'none'; // Ocultar el modal
  }
});

// Manejar el envío del formulario (puedes agregar funcionalidad adicional aquí)
document.getElementById('pedido-form').addEventListener('submit', function(event) {
  event.preventDefault();
  // Aquí puedes agregar la lógica para guardar el pedido
  Swal.fire({
    icon: 'success',
    title: 'Pedido Enviado',
    text: 'Tu pedido ha sido enviado con éxito.'
  });
  modal.style.display = 'none'; // Cerrar el modal al enviar el formulario
});

