"use strict";
const VERSION = "K";
const CVS = document.getElementById("game");
const CTX = CVS.getContext("2d");
const MINI = document.getElementById("mini");
const MAP = MINI.getContext("2d");
const SCORE = document.getElementById("score");
var health;
var worldX;
var worldY;
var screen; 
resize();
window.addEventListener("resize",resize);
//var socket = new WebSocket("ws://"+location.hostname+(location.port=="80"? "" : (":"+location.port))+"/Missile/serverD");
var name = document.querySelector("input");
const FORM = document.querySelector("form");
const PLAYER = {id:"",score: 0,x:0,y:0};
function resize(){
	CVS.setAttribute("width",window.innerWidth);
	CVS.setAttribute("height",window.innerHeight);
	screen = {x: CVS.width/2 , y: CVS.height/2};
}

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

var asteroidImg = new Image();
asteroidImg.src = 'http://icons.iconarchive.com/icons/zairaam/bumpy-planets/256/asteroid-icon.png';
var shipImg = new Image();
shipImg.src = 'https://s-media-cache-ak0.pinimg.com/736x/bc/22/81/bc2281162a4776e2a3f7fb91486a51ef.jpg'
FORM.addEventListener("submit", setName);

function setName(event){
  event.preventDefault();
  name = document.querySelector("input").value;
  FORM.style.display = "none";
	var socket = new WebSocket("ws://"+location.hostname+(location.port=="80"? "" : (":"+location.port))+"/Missile/server"+VERSION);
	socket.onopen = function(data){ //TODO: add player name and display it
	/*	  var timer = setInterval(function(){
		//  	input.angle += input.rotate? -.01 : 0 ; //TODO: make serverside
			  socket.send((input.up? "1":"0")+(input.left? "1":"0")+(input.down? "1":"0")
				  +(input.right? "1":"0")+(input.brake? "1":"0")+(input.shooting? "1":"0")
				  +(input.rotate? "1":"0")+(input.angle+"" ))},50) */
		function keyLetGo(event){
		  //if(CONFIG.running){
		    switch(event.keyCode){
		    	  case 87: input.up = false; socket.send("u0"); break; // W
		    	  case 65: input.left = false; socket.send("l0");break; // A
		    	  case 83: input.down = false; socket.send("d0");break; // S
		      case 68: input.right = false; socket.send("r0");break; // D 
		      case 32: input.brake = false; socket.send("b0");break; //space-bar
		      case 70: 										 socket.send("f0");break; // F
		      case 81: 										socket.send("h0");break; // Q
		      case 82: 										socket.send("i0");break; // R
		    }
		 // }
		}
		function keyPressed(event){
		   switch(event.keyCode){
		     case 87: input.up = true; socket.send("u1");break; // W
		     case 65: input.left = true; socket.send("l1");break; // A
		     case 83: input.down = true; socket.send("d1");break; //S
		     case 68: input.right = true; socket.send("r1");break; // D 
		     case 32: input.brake = true; socket.send("b1");break; //space-bar
		     case 69: input.shooting = !input.shooting; socket.send("s1");break; // E
		     case 67: input.rotate = !input.rotate; socket.send("t1");break; // C
		     case 70: 										 socket.send("f1");break; //F
		     case 81: 										socket.send("h1");console.log("Q");break; // Q
		     case 82: 										socket.send("i1");break; // R
		   }
		}
		socket.send("ø"+name);
		function mouseMove(event){
			socket.send("m"+(event.clientX-screen.x)+","+(screen.y-event.clientY))
//			socket.send("m"+ Math.atan2(event.clientX - screen.x ,screen.y - event.clientY));
			//input.angle = Math.atan2(event.clientX - screen.x ,screen.y - event.clientY );
		}
		function mouseDown(event){
			input.shooting = true;
			socket.send("s1");
		}
		function mouseUp(event){
			input.shooting = false;
			socket.send("s0");
		}

		document.addEventListener('mousemove', mouseMove);
		document.addEventListener('mouseup', mouseUp);
		document.addEventListener('mousedown', mouseDown);
		document.addEventListener('keyup', keyLetGo);
		document.addEventListener('keydown', keyPressed);
	}
	
		socket.onmessage = function(data){
		  if(data.data[0]=="_"){
			  let info = data.data.split(",")
			PLAYER.id = info[0].substring(1);
			worldX = parseFloat(info[1]);
			worldY = parseFloat(info[2]);
		    return;
		  }

		  if(data.data.length == 4) return;
		  CTX.fillStyle = "gray";
		  CTX.fillRect(0,0, CVS.width, CVS.height);
		  const ENTITIES = JSON.parse(data.data);
		  console.log(ENTITIES.time) 																					//TODO: remove
		  for(let i = 0, ships = ENTITIES.ships, l = ships.length;i<l;i++){
		    if(ships[i].id && ships[i].id == PLAYER.id){   // set loop
			  PLAYER.x = ships[i].pos.x
			  PLAYER.y = ships[i].pos.y
			  PLAYER.score = ships[i].score
			  //PLAYER.health = ENTITIES[i].health
			  SCORE.innerHTML = PLAYER.score
		    }
				/*  
		    switch(ENTITIES[i].type){
		      case "ship": ENTITIES[i].draw = drawSpaceship;break;
		      case "asteroid": ENTITIES[i].draw = drawAsteroid;break;
		      case "bullet": ENTITIES[i].draw = drawBullet;break;
		      case "explosion": ENTITIES[i].draw = drawExplosion;break;
		    }
		    */
		  }
		  MAP.clearRect(0,0,MINI.width,MINI.height);
		  MAP.fillStyle = "black";
		  MAP.globalAlpha = .7;
		  MAP.fillRect(0,0,MINI.width,MINI.height);
		  MAP.globalAlpha = 1;
		  //console.log(ENTITIES.shields)
		  
		 // CTX.fillStyle = "black";
		  CTX.clearRect(screen.x>PLAYER.x?(CVS.width-screen.x-PLAYER.x) :0,screen.y>PLAYER.y?(CVS.height-screen.y-PLAYER.y):0
			,screen.x>PLAYER.x?worldX:(worldX - (PLAYER.x-screen.x)), screen.y>PLAYER.y?worldY:(worldY-(PLAYER.y-screen.y)));
		  //CTX.fillRect(screen.x>PLAYER.x?(CVS.width-screen.x-PLAYER.x) :0,screen.y>PLAYER.y?(CVS.height-screen.y-PLAYER.y):0
			//	  , screen.x>PLAYER.x?worldX:(worldX - (PLAYER.x-screen.x)), screen.y>PLAYER.y?worldY:(worldY-(PLAYER.y-screen.y)));
		  CVS.style.backgroundPosition = (PLAYER.x/20*-1)+"px "+ (PLAYER.y/20*-1)+"px"
		  	  
		  for(let i = 0, ships = ENTITIES.ships, l = ships.length;i<l;i++) drawSpaceship(ships[i]);
		  
		  for(let i = 0, aiShips = ENTITIES.aiShips, l = aiShips.length;i<l;i++) drawAiSpaceship(aiShips[i]);
		  
		  for(let i = 0, asteroids = ENTITIES.asteroids, l = asteroids.length;i<l;i++) drawAsteroid(asteroids[i]);
			  
		  for(let i = 0, bullets = ENTITIES.bullets, l = bullets.length;i<l;i++) drawBullet(bullets[i]);
		  
		  for(let i = 0, mines = ENTITIES.mines, l = mines.length;i<l;i++) drawMine(mines[i]);
		  
		  for(let i = 0, shields = ENTITIES.shields, l = shields.length;i<l;i++) drawShield(shields[i]);
		  
		  for(let i = 0, mineExplosions = ENTITIES.mineExplosions, l = mineExplosions.length;i<l;i++) drawMineExplosion(mineExplosions[i]);
			  
		  for(let i = 0, explosions = ENTITIES.explosions, l = explosions.length;i<l;i++) drawExplosion(explosions[i]);
		    
		}

		socket.onerror = function(data){

		}
		socket.onclose = function(data){
			
		}
	}
//asteroid[0]- PLAYER.x + screen.x ,asteroid[1]- PLAYER.y + screen.y
function drawAiSpaceship(aiShip){
	CTX.save();
	CTX.beginPath();
	CTX.translate(aiShip[0]- PLAYER.x + screen.x,aiShip[1]- PLAYER.y + screen.y);
	CTX.fillStyle = "#cc6600";
	CTX.arc(0, 0, 21 , 0,2*Math.PI);
	CTX.fill();
	CTX.restore();
}
function drawSpaceship(ship){
	
  let playerX = ship.pos.x - PLAYER.x + screen.x;
  let playerY = ship.pos.y - PLAYER.y + screen.y;
  let playerMapX = (ship.pos.x - PLAYER.x)/20 + MINI.width/2;
  let playerMapY = (ship.pos.y - PLAYER.y)/20 + MINI.height/2;
  
  let left = ship.lAng-ship.shipFacing+Math.PI/2;
  let right = ship.rAng-ship.shipFacing+Math.PI/2;
  CTX.save();
  CTX.beginPath();
  CTX.translate(ship.pos.x - PLAYER.x + screen.x, ship.pos.y - PLAYER.y + screen.y);
  CTX.rotate(ship.shipFacing-Math.PI/2);
  CTX.translate(-20,-10);
  CTX.rotate(left);
  //CTX.rect(-5,-6,10,-10);
  //CTX.arc(0,0,10,0,2*Math.PI);
  CTX.ellipse(0,-5,6,10,0,0,2*Math.PI);
  CTX.clip();
	CTX.drawImage(shipImg,-93,-35,150,150);
  //CTX.fillStyle = ship.color;  
  //CTX.fill();
  CTX.restore();
  
  CTX.save();
  CTX.beginPath();
  CTX.translate(ship.pos.x - PLAYER.x + screen.x, ship.pos.y - PLAYER.y + screen.y);
  CTX.rotate(ship.shipFacing-Math.PI/2);
  CTX.translate(20,-10);
  CTX.rotate(right);
  //CTX.rect(-5,-6,10,-10);
  //CTX.arc(0,0,10,0,2*Math.PI);
  CTX.ellipse(0,-5,6,10,0,0,2*Math.PI);
  CTX.clip();
	CTX.drawImage(shipImg,-93,-35,150,150);
  //CTX.fillStyle = ship.color;  
  //CTX.fill();
	
  CTX.restore();
  
  CTX.save();
  CTX.beginPath();
  CTX.translate(playerX,playerY);
  CTX.rotate(ship.shipFacing-Math.PI/2);
  
  //CTX.save();
  //CTX.beginPath();
  CTX.arc(0, 0, 21 , 0,2*Math.PI)
  CTX.clip();	
	CTX.drawImage(shipImg,-93,-100,150,150);
	CTX.restore();
	if(ship.engineOn){
		CTX.save();
		CTX.translate(playerX,playerY);
	  CTX.rotate(ship.shipFacing-Math.PI/2);
	  let random = Math.random()*5;
    CTX.beginPath();
    CTX.moveTo(-12, 16); 
    CTX.lineTo(-8,random+25);
    CTX.lineTo(-4,18);
    CTX.lineTo(0, random+30);
    CTX.lineTo(4, 18); 
    CTX.lineTo(8, random+25);
    CTX.lineTo(12, 16);
    CTX.lineTo(4, 18); 
    CTX.lineTo(-4, 18);
    CTX.lineTo(-12, 16); 
    //CTX.lineTo(spaceship.width * -0.5, spaceship.height * 0.5);
    CTX.closePath();
    CTX.fillStyle = "orange";
    CTX.fill();
    CTX.restore();  
  }
	
  CTX.save();
  CTX.translate(playerX, playerY);
  CTX.fillStyle = "green"
  CTX.fillRect(-15,-35,5*ship.health,5)
  CTX.beginPath();
  CTX.strokeStyle = "darkgreen"
  CTX.rect(-15,-35,30,5)
  CTX.stroke();
  CTX.textAlign = "center";
  CTX.fillText(ship.name, 0,35);
  CTX.restore();
  
  MAP.save()
  MAP.translate(playerMapX,playerMapY)
  MAP.fillStyle = "white";
  MAP.beginPath()
  MAP.arc(0,0,2,0,Math.PI*2);
  MAP.fill();
  MAP.restore()
  
//  MAP.fillRect(ship.pos.x/10.5,ship.pos.y/16.5,5,5);  

}

function drawAsteroid(asteroid){	
	//let asteroidMapX = (asteroid[0] - PLAYER.x)/20 + MINI.width/2;
	//let asteroidMapY = (asteroid[1] - PLAYER.y)/20 + MINI.height/2;
	/*
	MAP.save()
	  MAP.translate(asteroidMapX,asteroidMapY)
	  MAP.fillStyle = "pink";
	  MAP.beginPath()
	  MAP.arc(0,0,2,0,Math.PI*2);
	  MAP.fill();
	  MAP.restore()
	  let offset = 30;
	*/
  
  if(Math.abs(asteroid[0]-PLAYER.x)>screen.x+15||Math.abs(asteroid[1]-PLAYER.y)>screen.y+15){
	  /*
		CTX.beginPath();
		CTX.arc((asteroid.px>=offset&&asteroid.px<=worldX-offset? asteroid.px:(asteroid.px<offset?worldX +
				  asteroid.px : asteroid.px-worldX))- PLAYER.x + screen.x
				  ,(asteroid.py>=offset&&asteroid.py<=worldY-offset? asteroid.py:(asteroid.py<offset?worldY +
						  asteroid.py : asteroid.py-worldY))- PLAYER.y + screen.y
				  //,asteroid.py - PLAYER.y + screen.y
				  ,asteroid.radius , 0,2*Math.PI)
		
		//CTX.clip();
		/*
		CTX.drawImage(asteroidImg,(asteroid.px>=offset&&asteroid.px<=worldX-offset? asteroid.px:(asteroid.px<offset?worldX +
			  asteroid.px : asteroid.px-worldX))- PLAYER.x + screen.x -30
			  ,(asteroid.py>=offset&&asteroid.py<=worldY-offset? asteroid.py:(asteroid.py<offset?worldY +
					  asteroid.py : asteroid.py-worldY))- PLAYER.y + screen.y -30
					  ,60,60);
					  * /
		//CTX.fillStyle = asteroid.color;
		CTX.fill();
		*/
  }
  else{    // view screen
  	CTX.save();
  	
  	CTX.translate(asteroid[0]- PLAYER.x + screen.x ,asteroid[1]- PLAYER.y + screen.y);
  	CTX.rotate(asteroid[3]);
  CTX.beginPath();
  //CTX.arc(asteroid.px - PLAYER.x + screen.x, asteroid.py - PLAYER.y + screen.y, asteroid.radius , 0,2*Math.PI)
  //CTX.clip();
  CTX.scale(asteroid[2]/80,asteroid[2]/80);
	
	CTX.drawImage(asteroidImg,-128,-128,256,256);
	CTX.restore();
  //CTX.fillStyle = asteroid.color;
  //CTX.fill(); 
  }
  
}
function drawBullet(bullet){
	if(Math.abs(bullet.pos.x-PLAYER.x)>screen.x+15||Math.abs(bullet.pos.y-PLAYER.y)>screen.y+15){
		//console.log("spam")
		return;
	  }
	CTX.save();
  CTX.beginPath();
  //CTX.arc(bullet.pos.x - PLAYER.x + screen.x,bullet.pos.y - PLAYER.y + screen.y,bullet.radius,0,2*Math.PI);
  CTX.ellipse(bullet.pos.x - PLAYER.x + screen.x,bullet.pos.y - PLAYER.y + screen.y,bullet.radius-1.5,bullet.radius+1.5,bullet.angle,0,2*Math.PI)
  CTX.fillStyle = bullet.color;
  CTX.fill();
  CTX.restore();
}
function drawMine(data){
	if(Math.abs(data[0]-PLAYER.x)>screen.x+15||Math.abs(data[1]-PLAYER.y)>screen.y+15){
		return;
	  }
  CTX.beginPath();
  //CTX.arc(bullet.pos.x - PLAYER.x + screen.x,bullet.pos.y - PLAYER.y + screen.y,bullet.radius,0,2*Math.PI);
  CTX.arc(data[0] - PLAYER.x + screen.x,data[1] - PLAYER.y + screen.y,6,0,2*Math.PI)
  CTX.fillStyle = data[3];
  CTX.fill();
}
function drawExplosion(data){
  CTX.beginPath();
  CTX.arc(data[0] - PLAYER.x + screen.x, data[1] - PLAYER.y + screen.y, 14-data[2]*2, 0, 2 * Math.PI);
  switch(data[2]){
    case 0: CTX.strokeStyle = "purple"; break;
    case 1: CTX.strokeStyle = "red"; break;
    case 2: CTX.strokeStyle = "orange"; break;
    case 3: CTX.strokeStyle = "yellow"; break;
    case 4: CTX.strokeStyle = "white"; break;
  }
  CTX.stroke();
}
function drawMineExplosion(data){
  CTX.beginPath();
  CTX.arc(data[0] - PLAYER.x + screen.x, data[1] - PLAYER.y + screen.y, data[2], 0, 2 * Math.PI);
  /*
  switch(data[2]){
    case 0: CTX.strokeStyle = "purple"; break;
    case 1: CTX.strokeStyle = "red"; break;
    case 2: CTX.strokeStyle = "orange"; break;
    case 3: CTX.strokeStyle = "yellow"; break;
    case 4: CTX.strokeStyle = "white"; break;
    case 5: CTX.strokeStyle = "yellow"; break;
    case 6: CTX.strokeStyle = "orange"; break;
    case 7: CTX.strokeStyle = "red"; break;
    case 8: CTX.strokeStyle = "purple"; break;
  }
  */
  CTX.strokeStyle = "white";
  CTX.stroke();
}
function drawShield(data){
	
	CTX.save();
	CTX.translate(data[0] - PLAYER.x + screen.x,data[1] - PLAYER.y + screen.y);
	let grd = CTX.createRadialGradient(0, 0, .5, 0, 0, 40);
	grd.addColorStop(1, "#00aaff");
	grd.addColorStop(0, "transparent");
	CTX.fillStyle = grd;
	CTX.beginPath();
	CTX.arc(0,0,40,0,2*Math.PI);
	CTX.globalAlpha = data[3]*.06;
	CTX.fill();
	CTX.restore();
	CTX.globalAlpha = 1;
}