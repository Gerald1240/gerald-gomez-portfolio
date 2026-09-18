// Obtener el id del trabajador desde la URL
const urlParams = new URLSearchParams(window.location.search);
const trabajadorId = urlParams.get('trabajadorId'); // Asegurarnos de que coincida con el parámetro de la URL

// Conectar con Firebase para obtener los detalles del trabajador
import { getFirestore, doc, getDoc, addDoc, collection } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-firestore.js";
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-app.js";

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
const db = getFirestore(app);

// Manejar el envío del formulario y guardar el pedido en Firestore
document.getElementById('pedido-form').addEventListener('submit', async function(event) {
  event.preventDefault();

  // Obtener los datos del formulario
  const fechaPedido = document.getElementById('fecha-pedido').value;
  const descripcion = document.getElementById('descripcion').value;
  const ubicacion = document.getElementById('ubicacion').value;
  const precioOfrecido = document.getElementById('precio').value;

  try {
    // Guardar en la colección 'Pedidos' con el trabajadorId
    await addDoc(collection(db, 'Pedidos'), {
      trabajadorId, // Asociamos el pedido al trabajador específico
      fechaPedido,
      descripcion,
      ubicacion,
      precioOfrecido,
      estado: 'pendiente', // Puedes agregar más campos según necesites
      timestamp: new Date() // Agregar fecha y hora de creación
    });

    Swal.fire({
      icon: 'success',
      title: 'Pedido Enviado',
      text: 'Tu pedido ha sido enviado con éxito.'
    });

    modal.style.display = 'none'; // Cerrar el modal al enviar el formulario
  } catch (error) {
    console.error("Error al guardar el pedido: ", error);
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: 'Hubo un problema al enviar tu pedido. Inténtalo de nuevo más tarde.'
    });
  }
});
