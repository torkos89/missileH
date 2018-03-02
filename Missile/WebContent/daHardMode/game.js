"use strict";
const CVS = document.getElementById("game");
const CTX = CVS.getContext("2d");
const MINI = document.getElementById("mini");
const MAP = MINI.getContext("2d");
const SCORE = document.getElementById("score");
var worldX;
var worldY;
var screen = {x: CVS.width/2 , y: CVS.height/2};
//var socket = new WebSocket("ws://"+location.hostname+(location.port=="80"? "" : (":"+location.port))+"/Missile/serverD");
var name = document.querySelector("input");
const FORM = document.querySelector("form");
const PLAYER = {id:"",score: 0,x:0,y:0};
var input = {
	    up: false,
	    left: false,
	    down: false,
	    right: false,
	    brake: false,
	    shooting: false,
	    rotate: false,
	    mouseX: 0,
	    mouseY: 0,
	    angle: 0
}
FORM.addEventListener("submit", setName);
function setName(event){
	event.preventDefault();
	name = document.querySelector("input").value;
	FORM.style.display = "none";
	var socket = new WebSocket("ws://"+location.hostname+(location.port=="80"? "" : (":"+location.port))+"/Missile/serverE");
	socket.onopen = function(data){
		  var timer = setInterval(function(){input.angle += input.rotate? -.01 : 0 ; 
			  socket.send((input.up? "1":"0")+(input.left? "1":"0")+(input.down? "1":"0")
				  +(input.right? "1":"0")+(input.brake? "1":"0")+(input.shooting? "1":"0")+(input.rotate? "1":"0")+(input.angle+"" ))})
		}
		
		socket.onmessage = function(data){
		  if(data.data[0]=="_"){
			  let info = data.data.split(",")
			PLAYER.id = info[0].substring(1);
			worldX = info[1];
			worldY = info[2];
		    return;
		  }

		  if(data.data.length == 4) return;
		  CTX.fillStyle = "gray";
		  CTX.fillRect(0,0, CVS.width, CVS.height);
		  
		  const ENTITIES = JSON.parse(data.data);
		  for(let i = 0,l = ENTITIES.length;i<l;i++){
		    if(ENTITIES[i].id && ENTITIES[i].id == PLAYER.id){   // set loop
			  PLAYER.x = ENTITIES[i].pos.x
			  PLAYER.y = ENTITIES[i].pos.y
			  PLAYER.score = ENTITIES[i].score
			  SCORE.innerHTML = PLAYER.score
		    }
				  
		    switch(ENTITIES[i].type){
		      case "ship": ENTITIES[i].draw = drawSpaceship;break;
		      case "asteroid": ENTITIES[i].draw = drawAsteroid;break;
		      case "bullet": ENTITIES[i].draw = drawBullet;break;
		      case "explosion": ENTITIES[i].draw = drawExplosion;break;
		    }
		  }
		  MAP.fillStyle = "black";
		  MAP.fillRect(0,0,MINI.width,MINI.height);

		  
		 // CTX.fillStyle = "black";
		  CTX.clearRect(screen.x>PLAYER.x?(CVS.width-screen.x-PLAYER.x) :0,screen.y>PLAYER.y?(CVS.height-screen.y-PLAYER.y):0
			,screen.x>PLAYER.x?worldX:(worldX - (PLAYER.x-screen.x)), screen.y>PLAYER.y?worldY:(worldY-(PLAYER.y-screen.y)));
		  //CTX.fillRect(screen.x>PLAYER.x?(CVS.width-screen.x-PLAYER.x) :0,screen.y>PLAYER.y?(CVS.height-screen.y-PLAYER.y):0
			//	  , screen.x>PLAYER.x?worldX:(worldX - (PLAYER.x-screen.x)), screen.y>PLAYER.y?worldY:(worldY-(PLAYER.y-screen.y)));
		  CVS.style.backgroundPosition = (PLAYER.x/25*-1)+"px "+ (PLAYER.y/25*-1)+"px"
		  
		  
		  for(let i = 0,l = ENTITIES.length;i<l;i++){   // draw loop
		   ENTITIES[i].draw(ENTITIES[i]);
		  }
		}

		socket.onerror = function(data){

		}
		socket.onclose = function(data){
			
		}
}


function keyLetGo(event){
  //if(CONFIG.running){
    switch(event.keyCode){
    	  case 87: input.up = false; break; // W
    	  case 65: input.left = false; break; // A
    	  case 83: input.down = false; break; // S
      case 68: input.right = false; break; // D 
      case 32: input.brake = false; break; //space-bar
    }
 // }
}
function keyPressed(event){
  
    switch(event.keyCode){
      case 87: input.up = true; break; // W
	  case 65: input.left = true; break; // A
	  case 83: input.down = true; break; //S
	  case 68: input.right = true; break; // D 
	  case 32: input.brake = true; break; //space-bar
	  case 69: input.shooting = !input.shooting; break; // E
      case 67: input.rotate = !input.rotate; break; // C
    }
}
function mouseMove(event){
	input.angle = Math.atan2(event.clientX - screen.x ,screen.y - event.clientY );
}
function mouseDown(event){
	input.shooting = true;
}
function mouseUp(event){
	input.shooting = false;
}

document.addEventListener('mousemove', mouseMove);
document.addEventListener('mouseup', mouseUp);
document.addEventListener('mousedown', mouseDown);
document.addEventListener('keyup', keyLetGo);
document.addEventListener('keydown', keyPressed);


function drawSpaceship(ship){

  let playerX = ship.pos.x - PLAYER.x + screen.x;
  let playerY = ship.pos.y - PLAYER.y + screen.y;
  CTX.save();
  CTX.beginPath();
  CTX.translate( playerX, playerY);
  CTX.rotate(ship.angle);
  CTX.rect(-10,-12,20,-20);
  CTX.arc(0,0,20,0,2*Math.PI);
  
 /*
  CTX.save();
  CTX.beginPath();
  CTX.translate( ship.pos.x - PLAYER.x + screen.x, ship.pos.y - PLAYER.y + screen.y);
  CTX.rotate(ship.angle);
  CTX.moveTo(0,-25);
  CTX.lineTo(-8,-5);
  CTX.lineTo(-14,-12);
  CTX.lineTo(-20,-5);
  CTX.lineTo(-20,18);
  CTX.lineTo(-8,18);
  CTX.lineTo(-8,10);
  //right side
  CTX.lineTo(8,10);
  CTX.lineTo(8,18);
  CTX.lineTo(20,18);
  CTX.lineTo(20,-5);
  CTX.lineTo(14,-12);
  CTX.lineTo(8,-5);
  CTX.lineTo(0,-25);
*/
  CTX.fillStyle = ship.color;   //TODO change player color
  CTX.fill();
  
  MAP.fillStyle = "white";
  MAP.fillRect(ship.pos.x/10.5,ship.pos.y/16.5,5,5);  
  
  
  if(ship.engineOn){
    CTX.beginPath();
    CTX.moveTo(-4, 10); //bot L
    CTX.lineTo(0, 19 + Math.random()*5);
    CTX.lineTo(4, 10); // bot R
    //CTX.lineTo(spaceship.width * -0.5, spaceship.height * 0.5);
    CTX.closePath();
    CTX.fillStyle = "orange";
    CTX.fill();
  }
  CTX.restore();
}

function drawAsteroid(asteroid){	
  CTX.beginPath();
  CTX.arc(asteroid.pos.x - PLAYER.x + screen.x, asteroid.pos.y - PLAYER.y + screen.y, asteroid.radius , 0,2*Math.PI)
  CTX.fillStyle = asteroid.color;
  MAP.fillStyle = "pink";
  MAP.fillRect(asteroid.pos.x/10.5,asteroid.pos.y/16.5,5,5);
  CTX.fill(); 
}
function drawBullet(bullet){
  CTX.beginPath();
  CTX.arc(bullet.pos.x - PLAYER.x + screen.x,bullet.pos.y - PLAYER.y + screen.y,bullet.radius,0,2*Math.PI);
  CTX.fillStyle = bullet.color;
  CTX.fill();
}
function drawExplosion(data){
  CTX.beginPath();
  CTX.arc(data.pos.x - PLAYER.x + screen.x, data.pos.y - PLAYER.y + screen.y, 14-data.radius*2, 0, 2 * Math.PI);
  switch(data.radius){
    case 0: CTX.strokeStyle = "purple"; break;
    case 1: CTX.strokeStyle = "red"; break;
    case 2: CTX.strokeStyle = "orange"; break;
    case 3: CTX.strokeStyle = "yellow"; break;
    case 4: CTX.strokeStyle = "white"; break;
  }
  CTX.stroke();
}