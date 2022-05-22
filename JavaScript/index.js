//  +buttons
/*
const buttons = [];
for(let i=1; i<=4;i++) {
    const button = document.getElementById('b_'+i);

    switch(i) {
        case 1: chd('up'); break;
        case 2: chd('down'); break;
        case 3: chd('left'); break;
        case 4: chd('right'); break;
    }

    buttons.push(button);
}
*/

//  -buttons

const canvas = document.getElementById('game');
const ctx = canvas.getContext('2d');

const bottom = document.getElementById('bottom')
const dbt = bottom.getContext('2d')

class SnakePart{
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
}

let fps = 10;

let tileCount = 20;
let tileSize = canvas.width / tileCount - 2;
let headX = 10//math.floor(tileCount/2);
let headY = 10//math.floor(tileCount/2);
const snakeParts = [];
let tailLength = 2;

let xVel=0;
let yVel=0;
let appleX = 5;
let appleY = 5;

let bonusX = 2;
let bonusY = 2;
let bonusCd = 100;
let appleNeeded = 3;

let score = 0;
let best = 0;

//game loop
function drawGame(){
    if(appleNeeded <= 0) bonusCd--;
    if(bonusCd < 0) {
        newBonusPos()
        resetBonusCd()
    }
    needinput = true;
    moveSnake();
    checkAlive();
    if(score >= best) {
        best = score;
    }

    clearScreen();
    checkApple();
    drawApple();
    drawSnake();
    drawScore();
    setTimeout(drawGame, 1000/fps);
}

function drawScore() {
    dbt.fillStyle = 'white';
    dbt.font = '20px Verdana';
    dbt.fillText('Score ' + score, canvas.width-100, 25);
    dbt.fillText('Best ' + best, 20, 25);
    dbt.fillText(bonusCd, 200, 25)
}

function clearScreen(){
    ctx.fillStyle = 'black';
    ctx.fillRect(0,0,canvas.width,canvas.height);

    dbt.fillStyle = 'black';
    dbt.fillRect(0,0,bottom.width, bottom.height);
}

function drawApple() {
    ctx.fillStyle = 'red';
    ctx.fillRect(appleX*tileSize, appleY*tileSize, tileSize, tileSize);

    if (bonusCd < 30 && bonusCd % 2 == 0){
        drawPX("white", bonusX, bonusY, 1);
    }
}

function drawSnake(){
    ctx.fillStyle = 'darkblue';
    ctx.fillRect(headX*tileSize, headY*tileSize, tileSize, tileSize);

    for(let i=0; i < snakeParts.length; i++) {
        let part = snakeParts[i];
        drawPX('blue', part.x, part.y);
        //ctx.fillRect(part.x*tileSize, part.y*tileSize, tileSize, tileSize);
    }

    snakeParts.push(new SnakePart(headX, headY));

    if(snakeParts.length > tailLength) {
        snakeParts.shift();
    }
}

function checkAlive() {
    for(let i=0; i<snakeParts.length; i++) {
        let part = snakeParts[i];
        if(part.x == headX && part.y == headY) {
            tailLength = 2;
            snakeParts.length = 0;
            score = 0;
        }
    }
}

function moveSnake(){
    headX += xVel;
    headY += yVel;

    if(headX < 0)            headX = tileCount+1;
    if(headX >= tileCount+2) headX = 0;
    if(headY < 0)            headY = tileCount+1;
    if(headY >= tileCount+2) headY = 0;
}

document.body.addEventListener('keydown', keyDown);

function checkApple() {
    if(appleX == headX && appleY == headY) {
        appleNeeded--;
        np = newpos();
        [appleX, appleY] = [np[0], np[1]];

        tailLength++;
        score++;
    }
    
    if(bonusCd <= 30) {
        if(bonusX == headX && bonusY == headY) {

            newBonusPos()
            score += 10;
            resetBonusCd()
        }
    }
    
}

function drawPX(color, x, y, corner=1) {
    ctx.fillStyle = color;
    dc = corner*2
    ctx.fillRect(x*tileSize+corner, y*tileSize+corner, tileSize-dc, tileSize-dc);
}

function keyDown(event) {
    if(needinput) {
        switch(event.keyCode) {
            case 38: chd('up'); break;
            case 40: chd('down'); break;
            case 37: chd('left'); break;
            case 39: chd('right'); break;
        }
    }
    needinput = false;
}

function chd(direction) {
    if(direction == 'up' || direction == 3) {
        if(yVel == 0){
            xVel = 0;
            yVel = -1;
        }
    }else if(direction == 'down') {
        if(yVel == 0) {
            xVel = 0;
            yVel = 1; 
        }
    }else if(direction == 'left') {
        if(xVel == 0) {
            xVel = -1;
            yVel = 0; 
        }
    }else if(direction == 'right') {
        if(xVel == 0) {
            xVel = 1;
            yVel = 0; 
        }
    }
}

function newpos() {

    let validpos = false;
    while(!validpos) {
        validpos = true;
        var vx = Math.floor(Math.random() * tileCount);
        var vy = Math.floor(Math.random() * tileCount);
        for(let i=0;i<snakeParts.length;i++){
            part = snakeParts[i];
            if(vx == part.x &&
                vy == part.y){
                    validpos = false;
                }
        }
    }
    return new Int16Array([vx, vy]);
}

function resetBonusCd() {
    bonusCd = 120;
    appleNeeded = 3;
}

function newBonusPos() {
    [bonusX, bonusY] = newpos();
}

document.documentElement.requestFullscreen();

drawGame();