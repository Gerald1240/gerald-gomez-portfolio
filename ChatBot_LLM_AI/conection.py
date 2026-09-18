import pyodbc

# Cadena de conexión
connection_string = (
    "Driver={ODBC Driver 17 for SQL Server};"
    "Server=DESKTOP-PJOO2F5;"  
    "Database=BI_SAVEC;"  
    "Trusted_Connection=yes;"   
)

# Conexión
try:
    conn = pyodbc.connect(connection_string)
    cursor = conn.cursor()
    print("Conexión exitosa a SQL Server")
except Exception as e:
    print("Error al conectar a SQL Server:", e)