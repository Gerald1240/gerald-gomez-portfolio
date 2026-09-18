import openai 
import re
from conection import conn  # Importar la conexión desde conection.py
import pandas as pd  # Asegúrate de tener pandas instalado

# Configura tu clave de API
openai.api_key = 'INSERT YOUR API KEY HERE'

# Crear el cursor
cursor = conn.cursor()

# Memoria del chatbot (historial de preguntas y respuestas)
memoria = []
memoria_sql = []

# Tamaño máximo del historial de memoria (5 interacciones)
TAMANO_MAXIMO_MEMORIA = 5

# Variable para controlar el modo actual
modo_actual = None  

# -------------------------------
# Utilitario: impresión de SQL
# -------------------------------
def imprimir_sql_en_consola(titulo, sql_texto):
    """
    Imprime en consola la consulta SQL que se va a ejecutar.
    """
    print("\n" + "=" * 70)
    print(f"{titulo}")
    print("-" * 70)
    print(formatear_sql_visual(sql_texto))
    print("=" * 70 + "\n")

# Función para limpiar consultas SQL generadas
def limpiar_consulta_sql(consulta):
    """
    Limpia la consulta SQL generada por OpenAI.
    - Extrae el contenido entre los bloques ```sql ... ``` o ``` ... ```.
    - Elimina backticks invertidos.
    - Recorta espacios sobrantes.
    - revisa que la consulta este bien hecha y si hay texto adicional despues del ; eliminalo
    """
    if not consulta:
        return ""

    # 1) Bloque con ```sql ... ```
    match = re.search(r"```sql\s*(.*?)\s*```", consulta, re.DOTALL | re.IGNORECASE)
    if match:
        consulta = match.group(1)
    else:
        # 2) Bloque con ``` ... ```
        match2 = re.search(r"```\s*(.*?)\s*```", consulta, re.DOTALL)
        if match2:
            consulta = match2.group(1)

    # Limpiezas adicionales
    consulta = consulta.replace("`", "")  # Backticks
    consulta = consulta.strip()

    # Opcional: remover punto y coma final duplicado
    consulta = re.sub(r";+\s*$", ";", consulta)

    return consulta

def formatear_sql_visual(sql):
    if not sql:
        return sql

    palabras = [
        "SELECT", "FROM", "LEFT JOIN", "INNER JOIN", "JOIN",
        "WHERE", "GROUP BY", "HAVING", "ORDER BY",
        "OFFSET", "FETCH NEXT"
    ]

    sql_formateado = sql

    for palabra in palabras:
        sql_formateado = re.sub(
            r"\s+" + palabra + r"\b",
            "\n" + palabra,
            sql_formateado,
            flags=re.IGNORECASE
        )

    sql_formateado = sql_formateado.replace(", ", ",\n    ")

    return sql_formateado.strip()

# Función para generar consultas dinámicas basadas en la estructura de la base de datos
def generar_consulta(pregunta):
    try:
        # Construir el contexto con la memoria
        messages = [
            {
                "role": "system",
                "content": (
                    "Eres un experto en bases de datos SQL Server. Usa la siguiente estructura de base de datos para responder preguntas:"
                    "\n\n"
                    "Tablas disponibles:\n"
                    "1. dbo.Cliente (ID_Cliente, Nombre_Cliente, Contacto, Dirección, Ciudad, FechaRegistro)\n"
                    "2. dbo.Venta (ID_Producto, ID_Cliente, Cantidad_Venta, Fecha_Hora_Venta, PrecioVenta, ID_Documento_Venta)\n"
                    "3. dbo.Productos (ID_Producto, Nombre_Producto, Precio_Compra_Unidad, Precio_Venta_Unidad, StockActual, StockBase)\n"
                    "4. dbo.Proveedor (ID_Proveedor, Nombre_Proveedor, Contacto, Dirección, Ciudad, FechaRegistro)\n"
                    "5. dbo.Kardex (ID_Producto, Entrada, Salida, StockActual, Tipo_Transaccion, Fecha_Hora, ID_Documento_Compra, ID_Documento_Venta)\n"
                    "6. dbo.Compra (ID_Compra, ID_Producto, ID_Proveedor, Cantidad_Compra, TotalCompra, PrecioCompra, Fecha_Hora_Compra, ID_Documento_Compra)\n"
                    "\n"
                    "Relaciones entre las tablas:\n"
                    "- dbo.Venta está relacionada con dbo.Cliente a través de ID_Cliente.\n"
                    "- dbo.Venta está relacionada con dbo.Productos a través de ID_Producto.\n"
                    "- dbo.Kardex está relacionada con dbo.Productos a través de ID_Producto.\n"
                    "- dbo.Compra está relacionada con dbo.Proveedor a través de ID_Proveedor.\n"
                    "- dbo.Compra está relacionada con dbo.Productos a través de ID_Producto.\n"
                    "- dbo.Kardex registra movimientos de inventario y está relacionada con documentos de compra y venta.\n"
                    "\n"
                    "Eres un asistente especializado en generar consultas SQL para la base de datos de inventario. "
                    "Si la pregunta del usuario no tiene relación con las tablas, campos o relaciones de la base de datos, "
                    "responde cortésmente indicando que no puedes responder porque la pregunta no está relacionada con el sistema de inventario a menos que sea informacion de productos."
                    "Los usuarios pueden preguntar acerca de los productos,clientes,proveedores"
                    "Genera consultas SQL válidas basándote en las relaciones entre estas tablas. revisa el contexto previo de la conversacion previa para contextualizar mas su pregunta"
                    "Cuando necesites devolver el segundo, tercero o cualquier posición específica de un ranking, no uses TOP junto con OFFSET. "
                    "En SQL Server usa ORDER BY ... OFFSET n ROWS FETCH NEXT 1 ROWS ONLY. "
                    "Genera consultas SQL válidas basándote en las relaciones entre estas tablas. "
                    "Revisa el contexto previo de la conversación para contextualizar la pregunta, "
                    "pero si hay varias consultas anteriores, usa principalmente la última interacción realizada por el usuario."
                    "Asegúrate de usar las columnas y relaciones correctas, y de que las consultas estén optimizadas para SQL Server"
                    "Si el usuario utiliza expresiones como 'su precio', "
                    "'su precio unitario', 'su stock', 'su código', "
                    "'ese producto', 'ese artículo', 'el mismo producto', "
                    "'ese cliente' o similares, utiliza la información de ULTIMO_RESULTADO "
                    "proporcionada en el contexto para generar una nueva consulta SQL.\n"
                    "Nunca respondas con explicaciones o texto en lenguaje natural.\n"
                    "Tu respuesta debe ser exclusivamente una consulta SQL válida y siempre incluye cantidades en la consulta\n"
                ),
            }
        ]
        # Añadir historial de memoria al contexto
        messages.extend(memoria_sql)

        # Añadir la nueva pregunta del usuario
        messages.append({"role": "user", "content": f"Pregunta: {pregunta}"})
        respuesta = openai.ChatCompletion.create(
            model="gpt-4o-mini",
            messages=messages
        )
        consulta_sql = respuesta['choices'][0]['message']['content']
        consulta_sql_limpia = limpiar_consulta_sql(consulta_sql)

        # Imprimir SQL generada por el LLM (antes de ejecutar)
        imprimir_sql_en_consola("CONSULTA SQL GENERADA POR LLM (ANTES DE EJECUTAR)", consulta_sql_limpia)

        return consulta_sql_limpia
    except Exception as e:
        imprimir_sql_en_consola("ERROR AL GENERAR CONSULTA", str(e))
        return None

# Función para ejecutar la consulta SQL en la base de datos
def consultar_datos(consulta_sql):
    try:
        consulta_sql = limpiar_consulta_sql(consulta_sql)
        consulta_sql = corregir_consulta_sql_server(consulta_sql)

        # Seguridad: solo permitir consultas SELECT
        if not consulta_sql.strip().upper().startswith("SELECT"):
            raise Exception("Solo se permiten consultas SELECT.")

        # Seguridad: bloquear operaciones peligrosas
        palabras_prohibidas = [
            "INSERT", "UPDATE", "DELETE", "DROP", "ALTER",
            "TRUNCATE", "CREATE", "EXEC", "MERGE"
        ]

        for palabra in palabras_prohibidas:
            if re.search(rf"\b{palabra}\b", consulta_sql, re.IGNORECASE):
                raise Exception(f"Operación no permitida: {palabra}")

        # Imprimir la consulta que REALMENTE se ejecutará
        imprimir_sql_en_consola("CONSULTA SQL EJECUTADA EN SQL SERVER", consulta_sql)

        cursor.execute(consulta_sql)
        resultados = cursor.fetchall()

        if resultados:
            columnas = [desc[0] for desc in cursor.description]
            datos = [dict(zip(columnas, fila)) for fila in resultados]
            return datos
        else:
            return None

    except Exception as e:
        imprimir_sql_en_consola("ERROR AL EJECUTAR SQL", str(e))
        return None
    
def corregir_consulta_sql_server(consulta):
    """
    Corrige incompatibilidades comunes de SQL Server generadas por el LLM.
    """
    if not consulta:
        return consulta

    # SQL Server no permite TOP junto con OFFSET
    if re.search(r"\bTOP\s+\d+\b", consulta, re.IGNORECASE) and re.search(r"\bOFFSET\s+\d+\s+ROWS\b", consulta, re.IGNORECASE):
        consulta = re.sub(r"\bTOP\s+\d+\s+", "", consulta, flags=re.IGNORECASE)

    return consulta

# Función para generar una respuesta final basada en los datos
# Función para generar una respuesta final basada en los datos
def generar_respuesta(pregunta, datos):
    try:
        if datos:
            datos_resumidos = datos[:10]

            contexto = ""
            for i, d in enumerate(datos_resumidos, start=1):
                contexto += f"Registro {i}:\n"
                for key, value in d.items():
                    contexto += f"- {key}: {value}\n"
                contexto += "\n"

            messages = [
                {
                    "role": "system",
                    "content": (
                        "Responde únicamente utilizando los datos proporcionados.\n"
                        "IMPORTANTE:\n"
                        "- Los datos recibidos provienen de una consulta SQL que ya fue ejecutada correctamente.\n"
                        "- Si existe al menos un registro, nunca digas que no hay información.\n"
                        "- No digas que faltan datos ni que se requiere información adicional.\n"
                        "- Asume que el resultado recibido corresponde exactamente a lo que preguntó el usuario.\n"
                        "- Si el usuario pregunta por el primer, segundo, tercer, cuarto o cualquier elemento de un ranking, "
                        "considera que el registro recibido ya corresponde a esa posición.\n"
                        "- No inventes información que no aparezca en los datos.\n"
                        "- Responde de forma natural, clara y directa."
                        "Si el usuario pregunta por el segundo, tercero, cuarto o cualquier posición "
                        "de un ranking, reutiliza ULTIMA_SQL y ajusta únicamente la posición solicitada.\n"
                        "Si el usuario pregunta por precio, stock, código, proveedor u otro dato "
                        "de un producto mencionado anteriormente, utiliza Nombre_Producto de "
                        "ULTIMO_RESULTADO para construir una nueva consulta SQL."
                        "Responde de forma detallada utilizando todos los registros recibidos.\n"
                        "No omitas valores numéricos importantes.\n"
                        "No resumas los resultados cuando existan varios registros.\n"
                        "Menciona explícitamente nombres, cantidades, precios, fechas, stock u otros campos que aparezcan en los datos.\n"
                        "Si existen múltiples registros, enuméralos de forma ordenada.\n"
                        "Si existe un único registro, menciona todos sus atributos relevantes.\n"
                        "Prioriza mostrar los datos antes que describirlos.\n"
                    )
                },
                {
                    "role": "user",
                    "content": (
                        f"Pregunta del usuario:\n{pregunta}\n\n"
                        f"Resultado SQL obtenido:\n{contexto}\n\n"
                        "El resultado SQL ya corresponde exactamente a la pregunta realizada."
                    )
                }
            ]

            respuesta = openai.ChatCompletion.create(
                model="gpt-3.5-turbo",
                messages=messages
            )

            return f"<strong>Querybot responde:</strong><br>{respuesta['choices'][0]['message']['content']}"

        else:
            return "Lo siento, no encontré información en los datos para responder a tu pregunta."

    except Exception as e:
        print(f"Error en generar_respuesta: {e}")
        return "Error al generar la respuesta."

def generar_ejemplos():
    """
    Usa la estructura real de la base de datos (tablas/columnas)
    para pedirle a OpenAI que genere 20 preguntas posibles
    que el usuario podría hacer sobre esos datos.
    y realiza preguntas que ayuden a la correcta ejecucion de las consultas" 
    y como fecha limite usa el 3 de octubre del 2024 todas las preguntas antes de esa fecha, "
    """
    try:
        # Obtener tablas y columnas reales
        cursor = conn.cursor()
        cursor.execute("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE'")
        tablas = [row[0] for row in cursor.fetchall()]

        estructura_bd = []
        for tabla in tablas:
            cursor.execute("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?", tabla)
            columnas = [row[0] for row in cursor.fetchall()]
            estructura_bd.append(f"Tabla {tabla}: {', '.join(columnas)}")

        prompt = (
            "Eres un experto en análisis de bases de datos SQL Server y generación de preguntas de negocio. "
            "Te daré la estructura de una base de datos (nombres de tablas y columnas). "
            "Tablas disponibles:\n"
            "1. dbo.Cliente (ID_Cliente, Nombre_Cliente, Contacto, Dirección, Ciudad, FechaRegistro)\n"
            "2. dbo.Venta (ID_Venta, ID_Producto, ID_Cliente, Cantidad_Venta, Fecha_Hora_Venta, PrecioVenta, ID_Documento_Venta)\n"
            "3. dbo.Productos (ID_Producto, Nombre_Producto, Precio_Compra_Unidad, Precio_Venta_Unidad, StockActual, StockBase)\n"
            "4. dbo.Proveedor (ID_Proveedor, Nombre_Proveedor, Contacto, Dirección, Ciudad, FechaRegistro)\n"
            "5. dbo.Kardex (ID_Producto, Entrada, Salida, StockActual, Tipo_Transaccion, Fecha_Hora, ID_Documento_Compra, ID_Documento_Venta)\n"
            "6. dbo.Compra (ID_Compra, ID_Producto, ID_Proveedor, Cantidad_Compra, TotalCompra, PrecioCompra, Fecha_Hora_Compra, ID_Documento_Compra)\n"
            "\n"
            "Relaciones entre las tablas:\n"
            "- dbo.Venta está relacionada con dbo.Cliente a través de ID_Cliente.\n"
            "- dbo.Venta está relacionada con dbo.Productos a través de ID_Producto.\n"
            "- dbo.Kardex está relacionada con dbo.Productos a través de ID_Producto.\n"
            "- dbo.Compra está relacionada con dbo.Proveedor a través de ID_Proveedor.\n"
            "- dbo.Compra está relacionada con dbo.Productos a través de ID_Producto.\n"
            "- dbo.Kardex registra movimientos de inventario y está relacionada con documentos de compra y venta.\n"
            "Genera 20 preguntas útiles, variadas y realistas que un usuario podría hacer sobre estos datos se especifico"
            "y realiza preguntas que ayuden a la correcta ejecucion de las consultas" 
            "y como fecha limite usa el 3 de octubre del 2024, "
            "como si usara un asistente virtual de inventario. "
            "Devuélvelas como una lista numerada del 1 al 20 sin repetir temas.\n\n"
            f"Estructura de la base de datos:\n{chr(10).join(estructura_bd)}"
        )

        # Pedimos a OpenAI que genere las preguntas
        respuesta = openai.ChatCompletion.create(
            model="gpt-4o-mini",
            messages=[
                {"role": "system", "content": "Eres un generador de ejemplos de consultas para un chatbot de inventario."},
                {"role": "user", "content": prompt}
            ]
        )

        texto = respuesta["choices"][0]["message"]["content"]

        # Formatear resultado para mostrarlo bonito
        texto_html = texto.replace("\n", "<br>")
        return f"💡 <b>Ejemplos de preguntas que puedo responder:</b><br>{texto_html}"

    except Exception as e:
        return f"Error al generar ejemplos dinámicos: {str(e)}"

# Función principal del chatbot
def chatbot(pregunta):
    global modo_actual

    # 🧭 Verificar si el usuario quiere volver al menú inicial
    if pregunta.lower() in {"menu", "0"}:
        modo_actual = None
        return (
            "🔙 Has vuelto al menú principal.\n"
            "Por favor, selecciona una opción:\n"
            "1️⃣ para SQL\n"
            "2️⃣ para generar ejemplos dinámicos\n"
            "0️⃣ para volver al menú"
        )

    # 🚪 Si el modo no ha sido seleccionado, mostrar menú inicial
    if modo_actual is None:
        if pregunta == "1":
            modo_actual = "sql"
            return (
                "✅ Modo SQL seleccionado. Puedes empezar a hacer tus preguntas sobre la base de datos.\n"
                "(Escribe '0' para volver al menú)"
            )

        elif pregunta == "2":
            modo_actual = "ejemplos"
            return (
                "🧠 Has seleccionado la opción para generar ejemplos dinámicos de preguntas.\n"
                "Esta función analizará tu base de datos y generará 20 posibles preguntas relacionadas con ventas, clientes, productos, proveedores, etc.\n\n"
                "¿Deseas continuar? Escribe <b>'ok'</b> para comenzar o <b>'0'</b> para volver al menú."
            )

        else:
            return (
                "Por favor, selecciona una opción válida:\n"
                "1️⃣ para SQL\n"
                "2️⃣ para generar ejemplos dinámicos\n"
                "0️⃣ para volver al menú"
            )

    # ⚙️ Si el modo ejemplos está activo y el usuario confirma con "ok"
    if modo_actual == "ejemplos":
        if pregunta.lower() == "ok":
            try:
                cursor = conn.cursor()
                cursor.execute("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE'")
                tablas = [row[0] for row in cursor.fetchall()]

                estructura_bd = []
                for tabla in tablas:
                    cursor.execute("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?", tabla)
                    columnas = [row[0] for row in cursor.fetchall()]
                    estructura_bd.append(f"Tabla {tabla}: {', '.join(columnas)}")

                prompt = (
                    "Eres un experto en SQL Server y análisis de datos empresariales. "
                    "Te proporcionaré la estructura real de una base de datos con sus tablas y columnas. "
                    "Genera 20 preguntas diferentes que un usuario podría hacer sobre estos datos, "
                    "relacionadas con ventas, productos, clientes, proveedores, inventario o compras. "
                    "Tablas disponibles:\n"
                    "1. dbo.Cliente (ID_Cliente, Nombre_Cliente, Contacto, Dirección, Ciudad, FechaRegistro)\n"
                    "2. dbo.Venta (ID_Venta, ID_Producto, ID_Cliente, Cantidad_Venta, Fecha_Hora_Venta, PrecioVenta, ID_Documento_Venta)\n"
                    "3. dbo.Productos (ID_Producto, Nombre_Producto, Precio_Compra_Unidad, Precio_Venta_Unidad, StockActual, StockBase)\n"
                    "4. dbo.Proveedor (ID_Proveedor, Nombre_Proveedor, Contacto, Dirección, Ciudad, FechaRegistro)\n"
                    "5. dbo.Kardex (ID_Producto, Entrada, Salida, StockActual, Tipo_Transaccion, Fecha_Hora, ID_Documento_Compra, ID_Documento_Venta)\n"
                    "6. dbo.Compra (ID_Compra, ID_Producto, ID_Proveedor, Cantidad_Compra, TotalCompra, PrecioCompra, Fecha_Hora_Compra, ID_Documento_Compra)\n"
                    "\n"
                    "Relaciones entre las tablas:\n"
                    "- dbo.Venta está relacionada con dbo.Cliente a través de ID_Cliente.\n"
                    "- dbo.Venta está relacionada con dbo.Productos a través de ID_Producto.\n"
                    "- dbo.Kardex está relacionada con dbo.Productos a través de ID_Producto.\n"
                    "- dbo.Compra está relacionada con dbo.Proveedor a través de ID_Proveedor.\n"
                    "- dbo.Compra está relacionada con dbo.Productos a través de ID_Producto.\n"
                    "- dbo.Kardex registra movimientos de inventario y está relacionada con documentos de compra y venta.\n"
                    "Genera 20 preguntas útiles, variadas y realistas que un usuario podría hacer sobre estos datos se especifico"
                    "y realiza preguntas que ayuden a la correcta ejecucion de las consultas" 
                    "y como fecha limite usa el 3 de octubre del 2024, "
                    f"Estructura de la base de datos:\n{chr(10).join(estructura_bd)}"
                )

                respuesta = openai.ChatCompletion.create(
                    model="gpt-4o-mini",
                    messages=[
                        {
                            "role": "system",
                            "content": "Eres un generador de ejemplos de preguntas para un chatbot de inventario."
                        },
                        {"role": "user", "content": prompt}
                    ]
                )

                texto = respuesta["choices"][0]["message"]["content"]
                texto_html = texto.replace("\n", "<br>")
                modo_actual = None  # 🔙 vuelve al menú luego de generar
                return (
                    "💡 <b>Ejemplos de preguntas que puedo responder:</b><br>"
                    f"{texto_html}<br><br>"
                    "🔙 Escribe '0' para volver al menú principal."
                )

            except Exception as e:
                modo_actual = None
                return f"❌ Error al generar ejemplos dinámicos: {str(e)}"

        elif pregunta.lower() == "0":
            modo_actual = None
            return (
                "🔙 Has cancelado la generación de ejemplos.\n"
                "Por favor, selecciona una opción:\n"
                "1️⃣ para SQL\n"
                "2️⃣ para generar ejemplos dinámicos\n"
                "0️⃣ para volver al menú"
            )
        else:
            return "Por favor, confirma escribiendo <b>'ok'</b> para comenzar o <b>'0'</b> para volver al menú."

    # ⚙️ Si el modo SQL está activo
    if modo_actual == "sql":
        MAX_INTENTOS = 3
        ultimo_error = None

        for intento in range(1, MAX_INTENTOS + 1):
            print(f"\n>>> Intento {intento} de {MAX_INTENTOS} para generar/ejecutar la consulta (SQL)...")

            # Generar consulta SQL
            consulta_sql = generar_consulta(pregunta)
            if consulta_sql and not consulta_sql.strip().lower().startswith("select"):
                ultimo_error = "El modelo devolvió texto en lugar de SQL."
                continue

            if not consulta_sql:
                ultimo_error = "No se pudo generar una consulta SQL válida."
                continue
            
            #if not any(keyword in consulta_sql.lower() for keyword in ["select", "from", "join", "where"]):
            #    return (
            #   "❌ No se detectó una consulta SQL válida. Asegúrate de preguntar sobre los datos del inventario, "
            #    "como ventas, productos, clientes o proveedores."
            #)

            # Ejecutar consulta
            # Ejecutar consulta
            datos_base = consultar_datos(consulta_sql)
            if datos_base is None:
                ultimo_error = "La consulta no devolvió resultados o falló su ejecución."
                continue

            # Generar respuesta
            respuesta = generar_respuesta(pregunta, datos_base)

            # Guardar en memoria con contexto útil para preguntas posteriores
            resumen_datos = datos_base[:5] if datos_base else []

            # Memoria para respuestas
            memoria.append({"role": "user", "content": pregunta})

            memoria.append({
                "role": "assistant",
                "content": respuesta
            })

            memoria_sql.clear()

            memoria_sql.append({
                "role": "system",
                "content": (
                    f"ULTIMA_PREGUNTA = {pregunta}\n"
                    f"ULTIMA_SQL = {consulta_sql}\n"
                    f"ULTIMO_RESULTADO = {resumen_datos}\n\n"
                    "IMPORTANTE:\n"
                    "ULTIMO_RESULTADO contiene el producto, cliente o venta "
                    "mencionado más recientemente.\n"
                    "Si el usuario pregunta:\n"
                    "- su precio\n"
                    "- su precio unitario\n"
                    "- su stock\n"
                    "- su codigo\n"
                    "- ese producto\n"
                    "- ese articulo\n"
                    "- el mismo producto\n"
                    "- ese cliente\n"
                    "- esa venta\n"
                    "debes usar ULTIMO_RESULTADO como referencia.\n"
                    "Genera una nueva consulta SQL utilizando la información "
                    "contenida en ULTIMO_RESULTADO.\n"
                    "Nunca respondas directamente con texto.\n"
                    "Siempre genera SQL válido."
                )
            })
            # Mantener tamaño máximo de memoria
            while len(memoria) > 2 * TAMANO_MAXIMO_MEMORIA:
                memoria.pop(0)
                memoria.pop(0)

            while len(memoria_sql) > 2 * TAMANO_MAXIMO_MEMORIA:
                memoria_sql.pop(0)
                memoria_sql.pop(0)

            return respuesta
        return (
            f"No pude generar/ejecutar una consulta SQL válida para responder a tu pregunta "
            f"después de {MAX_INTENTOS} intentos. Detalle: {ultimo_error}"
        )

