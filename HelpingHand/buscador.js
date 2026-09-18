import { initializeApp } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-app.js";
import { getFirestore, collection, getDocs } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-firestore.js";

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

const app = initializeApp(firebaseConfig);
const db = getFirestore(app);

// Función para obtener y mostrar los trabajadores registrados
async function obtenerTrabajadores() {
    const trabajadoresRef = collection(db, "trabajadores");  // Colección de trabajadores en Firestore
    const snapshot = await getDocs(trabajadoresRef);
    
    // Devuelve un array de objetos que incluye los datos del trabajador y el `id` del documento
    return snapshot.docs.map(doc => ({
        id: doc.id,  // Incluimos el ID del documento Firestore
        ...doc.data()  // Espaciamos todos los demás datos
    }));
}

// Función para filtrar y mostrar resultados de búsqueda
function mostrarTrabajadores(trabajadores, filtro = '') {
    // Selecciona el contenedor donde se mostrarán las tarjetas
    const roomGrid = document.querySelector('.room__grid');
    roomGrid.innerHTML = '';  // Limpia cualquier contenido previo

    // Filtrar los trabajadores según el texto del filtro
    const trabajadoresFiltrados = trabajadores.filter(trabajador =>
        trabajador.nombre.toLowerCase().includes(filtro.toLowerCase()) ||
        trabajador.especialidad.toLowerCase().includes(filtro.toLowerCase())
    );

    // Genera las tarjetas para cada trabajador filtrado
    trabajadoresFiltrados.forEach(trabajador => {
        // Aquí usamos `profileImageUrl` en lugar de `fotoURL`
        const imagenPerfil = trabajador.profileImageUrl ? trabajador.profileImageUrl : 'ruta/a/imagen/default.jpg'; // Si no tiene imagen, usar una imagen por defecto

        const card = `
            <div class="room__card">
                <div class="room__card__image">
                    <img src="${imagenPerfil}" alt="${trabajador.nombre}" />
                    <div class="room__card__icons">
                        <span><i class="ri-heart-fill"></i></span>
                        <span><i class="ri-paint-fill"></i></span>
                        <span><i class="ri-shield-star-line"></i></span>
                    </div>
                </div>
                <div class="room__card__details">
                    <h4>${trabajador.nombre}</h4>
                    <p>${trabajador.biografia || 'Sin descripción disponible'}</p>
                    <h5>Desde <span>${trabajador.precioMinimo} soles/servicio</span></h5>
                    <!-- Aquí añadimos el id del trabajador a la URL -->
                    <button class="btn" onclick="window.location.href='contratar.html?trabajadorId=${trabajador.id}'">Agenda Ahora</button>
                </div>
            </div>
        `;
        roomGrid.innerHTML += card;  // Añade la tarjeta al contenedor
    });
}

// Lógica del buscador
document.getElementById('search').addEventListener('input', async (e) => {
    const filtro = e.target.value;
    const trabajadores = await obtenerTrabajadores();  // Obtenemos los trabajadores
    mostrarTrabajadores(trabajadores, filtro);  // Mostramos los trabajadores filtrados
});

// Cargar todos los trabajadores al cargar la página
window.addEventListener('DOMContentLoaded', async () => {
    const trabajadores = await obtenerTrabajadores();  // Obtenemos los trabajadores
    mostrarTrabajadores(trabajadores);  // Mostramos todos los trabajadores al inicio
});
