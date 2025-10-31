# 🎲 Sistema de Bingo de Frases

Este proyecto permite ejecutar el **Bingo de Frases** de forma simple, tanto en **Windows** como en **Linux**, con un solo clic.  
Incluye archivos de inicio automatizados para facilitar el uso sin necesidad de abrir la terminal.

---

## 💻 INICIO DEL JUEGO (Windows)

Este archivo te permite iniciar el sistema de Bingo con un simple **doble clic**, sin usar la terminal.

### 🔧 Requisitos

- **Java:** Debes tener instalado **Java Runtime Environment (JRE)** versión **17 o superior**.  
- **Archivo JAR:** Generá el archivo ejecutable con el comando:
  ```bash
  mvn clean package
(El .jar se generará dentro de la carpeta target del proyecto).

Archivos necesarios: deben estar juntos en la misma carpeta:

Copy code
bingo-game.jar      ← Programa principal
iniciar_juego.bat   ← Archivo de inicio
▶️ Archivo de inicio (iniciar_juego.bat)
bat
Copy code
@echo off
ECHO Iniciando el servidor de Bingo en el puerto 8080...

REM Inicia el servidor Java en segundo plano (sin ventana de consola)
start javaw -jar bingo-game.jar

REM Espera unos segundos para permitir que el servidor se inicie
timeout /t 5 /nobreak

ECHO Abriendo el navegador...
REM Abre Google Chrome con la vista de docente
start chrome "http://localhost:8080/teacher/view"

EXIT
🚀 Pasos para Iniciar
Asegurate de que los archivos .jar y .bat estén en el mismo lugar.

Hacé doble clic en iniciar_juego.bat.

Se abrirá el navegador automáticamente en la vista de docente:

bash
Copy code
http://localhost:8080/teacher/view
🌐 Acceso al Juego
Vista Docente:
👉 http://localhost:8080/teacher/view

Conexión de Jugadores:
Usá el código QR que aparece en pantalla.
El QR contendrá la IP local de tu computadora para que los jugadores puedan conectarse desde otros dispositivos en la misma red.

⛔ Para Detener el Juego
El servidor Java queda corriendo en segundo plano.
Para detenerlo:

Abrí el Administrador de Tareas (Ctrl + Shift + Esc).

Buscá el proceso llamado Java o OpenJDK.

Seleccionalo y hacé clic en Finalizar tarea.

##🐧 INICIO DEL JUEGO (Linux)
En Linux podés crear un lanzador de aplicación (.desktop) con ícono propio, para abrir el juego como si fuera una app nativa.

###🔧 Requisitos
Java: Instalar JRE 17 o superior.

Archivos necesarios:

scss
Copy code
bingo-game.jar         ← Programa principal
bingo_launcher.desktop ← Archivo de lanzador
bingoIcon.png          ← Ícono (opcional, formato PNG)
Generá el .jar con:

  mvn clean package
  
🧩 Ejemplo de archivo bingo_launcher.desktop

[Desktop Entry]
Version=1.0
Type=Application
Name=Bingo de Frases
Exec=java -jar /home/lucia/JuegoBingo/bingo-game.jar
Icon=/home/lucia/JuegoBingo/bingoIcon.png
Terminal=false
Categories=Game;Education;
🚀 Pasos para Instalar y Ejecutar
Verificá las rutas de Exec= e Icon= en el archivo .desktop.
Deben apuntar a la ubicación correcta de tus archivos, por ejemplo:

swift

Dale permisos de ejecución:

bash
Copy code
chmod +x bingo_launcher.desktop
Movelo al escritorio o al menú de aplicaciones:

bash
Copy code
mv bingo_launcher.desktop ~/Desktop/
## o para el menú:
mv bingo_launcher.desktop ~/.local/share/applications/
Hacé doble clic en el ícono para iniciar el servidor y abrir el navegador en:

bash
Copy code
http://localhost:8080/teacher/view

##🛑 Para Detener el Juego
El servidor corre en segundo plano, por lo que necesitás detener el proceso Java manualmente:

bash
Copy code
ps aux | grep bingo-game.jar
Copiá el número PID del proceso y ejecutá:

bash
Copy code
kill [PID_del_proceso]

###🏁 ¡Listo!
