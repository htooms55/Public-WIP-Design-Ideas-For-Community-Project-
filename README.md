# Public-WIP-Design-Ideas-For-Community-Project-


A community garden management web application built in Java.  
This is my personal WIP fork of the original CS201 class project, featuring 
a full MVC restructure, a live web dashboard, and custom frontend design work.

---

## Building Upon Coursework Project (Changes)

- Refactored flat file structure into a clean MVC package layout (`model/`, `service/`, `controller/`)
- Converted `User` plant storage from a manual fixed array to `ArrayList<Plant>`
- Fixed request ID collision bug across application restarts
- Built a lightweight Java `HttpServer` web backend (`GardenWebServer.java`)
- Designed and developed an interactive browser-based garden dashboard (`web/index.html`)

---

## Design Direction

- Pixel art garden aesthetic to match with garden theme. WIP (Taking color palette inspirations from Firewatch, Life is Strange, An Average Day at The Cat Cafe, Missed Messages. (Aesthetics)
- Brainstorming theme variations: Water Color, Pastel, Pixel-Hybrid, 3D/2D)

### Title Screen Concept
![Title Screen](design/pixel_title_screen.jpg)

### Garden Menu Concept
![Garden Menu](design/pixel_garden_menu.jpg)

---

## Tech Stack

- **Backend**: Java (plain `HttpServer`, no external frameworks)
- **Frontend**: HTML, CSS, Vanilla JavaScript
- **Persistence**: Java Object Serialization (`garden.dat`)
- **Weather**: Open-Meteo REST API

---

## Original Project

Original class project repository (private):  
[CS201PublicGardenProject](https://github.com/nwyclark/CS201PublicGardenProject) 
