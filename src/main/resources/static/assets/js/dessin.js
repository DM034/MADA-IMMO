// Accéder au canvas et à son contexte de dessin
const canvas = document.getElementById('myCanvas');
const ctx = canvas.getContext('2d');

// Dessiner un rectangle
ctx.fillStyle = '#FF0000';  // Couleur de remplissage
ctx.fillRect(50, 50, 150, 100);  // x, y, largeur, hauteur

// Dessiner un cercle
ctx.beginPath();
ctx.arc(300, 150, 50, 0, Math.PI * 2, true);  // x, y, rayon, début angle, fin angle, sens horaire
ctx.fillStyle = '#00FF00';  // Couleur de remplissage
ctx.fill();
ctx.closePath();

// Dessiner une ligne
ctx.beginPath();
ctx.moveTo(100, 300);  // Début de la ligne
ctx.lineTo(400, 400);  // Fin de la ligne
ctx.strokeStyle = '#0000FF';  // Couleur de la ligne
ctx.lineWidth = 5;  // Largeur de la ligne
ctx.stroke();
ctx.closePath();
