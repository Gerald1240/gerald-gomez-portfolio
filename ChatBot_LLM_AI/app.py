from flask import Flask, render_template, request, jsonify
from chatbot import chatbot  # Importa la función principal del chatbot
import re

app = Flask(__name__)

def format_list_items(text):
    """
    Reemplaza patrones como '1. texto', '2. texto' por líneas separadas con salto <br>
    y negrita en los números. También convierte saltos de línea (\n) en <br>.
    """
    if not text:
        return ""

    # Convierte "1. texto" → "<br><b>1.</b> texto"
    formatted = re.sub(r"(\d+)\.\s", r"<br><b>\1.</b> ", text)
    # Convierte saltos de línea normales a <br>
    formatted = formatted.replace("\n", "<br>")

    return formatted


@app.route("/")
def home():
    return render_template("index.html")  # Página principal

@app.route("/get_response", methods=["POST"])
def get_response():
    # Obtiene la pregunta del usuario desde el cliente
    user_input = request.json.get("user_input")

    if not user_input:
        return jsonify({"error": "No se recibió ninguna entrada del usuario."})

    # Llama a la función principal del chatbot
    bot_response = chatbot(user_input)

    # 🔧 Formatea la respuesta para mostrar listas numeradas y saltos
    formatted_response = format_list_items(bot_response)

    # Devuelve la respuesta ya formateada al frontend
    return jsonify({"response": formatted_response})



if __name__ == "__main__":
    # En producción usa 'debug=False' o servidor WSGI (gunicorn/waitress)
    app.run(debug=True)
