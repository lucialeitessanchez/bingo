#*INICIO DEL JUEGO (WINDOWS - Archivo .BAT) *💻#
Este archivo te permite iniciar el sistema de Bingo de Frases con un simple doble clic, sin necesidad de usar la terminal.

#Requisitos#
*Java: Debes tener instalado el Java Runtime Environment (JRE) (versión 17 o superior) en tu computadora.

*Generar archivo .jar: mvn clean package (en carpeta target del proyecto)

*Archivos: Debes tener los siguientes archivos juntos en la misma carpeta:

*bingo-game.jar (El programa principal)

*iniciar_juego.bat (El archivo de inicio):

@echo off
ECHO Iniciando el servidor de Bingo en el puerto 8080...

REM Inicia el servidor Java en segundo plano usando javaw (sin ventana de consola)
start javaw -jar bingo-game.jar

REM Espera unos 5 segundos para que el servidor se inicie completamente
timeout /t 5 /nobreak

ECHO Abriendo el navegador...
REM Abre Google Chrome con la URL de la docente
start chrome "http://localhost:8080/teacher/view"

EXIT

##Pasos para Iniciar##
*Asegúrate de que los archivos .jar y .bat estén en el mismo lugar.

*Haz doble clic en el archivo iniciar_juego.bat.

*Nota: El servidor del juego se iniciará en segundo plano y abrirá tu navegador automáticamente en la página de control.

##Acceso al Juego##
Una vez que el navegador se abra, ya estás en la vista de la docente.

Tu Vista (Docente): http://localhost:8080/teacher/view

Conexión de Jugadores (QR): Para que los jugadores se conecten, usa el QR que se muestra en pantalla. El QR contendrá la dirección IP de tu computadora para que la red funcione correctamente. Todos los jugadores deben estar en la misma red

##Para Detener el Juego##
Dado que la aplicación se ejecuta en segundo plano, cerrar la ventana del navegador no detiene el servidor.

Abre el Administrador de Tareas (Ctrl+Shift+Esc).

Busca cualquier proceso llamado "Java" o "OpenJDK".

Selecciona el proceso que esté usando más memoria o CPU y haz clic en "Finalizar tarea".

#*INICIO DEL JUEGO (LINUX - Archivo .desktop)* 🐧#
Este archivo te permite crear un lanzador con un ícono, haciendo que el sistema de Bingo de Frases se sienta como una aplicación nativa.

##Requisitos##
*Java: Debes tener instalado el Java Runtime Environment (JRE) (versión 17 o superior) en tu sistema.

*Archivos: Necesitas:

*bingo-game.jar (El programa principal - Generar archivo .jar: mvn clean package (en carpeta target del proyecto)) 

*bingo_launcher.desktop (El archivo de lanzador)

*bingoIcon.png (O el nombre de tu ícono, preferiblemente un archivo .png)

##Pasos para Instalar y Ejecutar##
1. Preparar el Lanzador
Abre el archivo bingo_launcher.desktop y verifica que la ruta Exec= y Icon= apunten a la ubicación correcta de los archivos en tu sistema.

Ejemplo: Si colocaste los archivos en /home/lucia/JuegoBingo/, la línea Exec= debe apuntar a ese lugar.

Abre la terminal en la carpeta donde tienes los archivos y dale permisos de ejecución:

Bash

chmod +x bingo_launcher.desktop
Mueve el archivo .desktop al escritorio o a la carpeta de aplicaciones local:

Bash

mv bingo_launcher.desktop ~/Desktop/
# O para el menú de aplicaciones:
# mv bingo_launcher.desktop ~/.local/share/applications/
2. Iniciar la Aplicación
Haz doble clic en el nuevo ícono que aparece en tu escritorio o en el menú de aplicaciones.

El servidor se iniciará y automáticamente abrirá tu navegador en: http://localhost:8080/teacher/view.

Para Detener el Juego
Como el servidor se ejecuta en segundo plano, necesitas finalizar el proceso de Java:

Abre la terminal (Ctrl+Alt+T).

Busca el proceso del juego:

Bash

ps aux | grep bingo-game.jar
Copia el número PID (el segundo número de la línea).

Detén el proceso:

Bash

kill [PID_del_proceso]
