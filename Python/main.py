import pygame, sys
from pygame.locals import *
from random import randint

from second import *

screen = pygame.display.set_mode((SS, SS))
pygame.display.set_caption('Snake Game')

with open('best.txt') as f:
   bestStr = f.readlines(0)[0]
   best = int(bestStr)

def gameOver():
   for sp in parts:
      if sp == head:
         return True

   return False

def drbp(x, y, color):
   pygame.draw.rect(screen, color, (x*TS+1, y*TS+1, TS-2, TS-2))

def draw():
   screen.fill(black)

   drbp(head[0], head[1], white)

   for sp in parts:
      drbp(sp[0], sp[1], blue)

   drbp(apple[0], apple[1], red)

   mts(screen, False, 16, f'Score: {length}', white, 20, 20)
   mts(screen, False, 16, f'Best: {best}', white, 20, 45)

   pygame.display.update()

run = True
while run:
   clock.tick(FPS)

   parts.append([head[0], head[1]])
   if len(parts) > length+1:
      parts.remove(parts[0])

   ev = pygame.event.get()
   for e in ev:
      if e.type == QUIT:
         run = False

      if e.type == KEYDOWN:
         if e.key == K_ESCAPE:
            run = False

   keys = pygame.key.get_pressed()

   if keys[K_UP]:
      if vel[1] == 0:
         vel[0] = 0
         vel[1] = -1

   elif keys[K_DOWN]:
      if vel[1] == 0:
         vel[0] = 0
         vel[1] = 1

   elif keys[K_LEFT]:
      if vel[0] == 0:
         vel[0] = -1
         vel[1] = 0

   elif keys[K_RIGHT]:
      if vel[0] == 0:
         vel[0] = 1
         vel[1] = 0

   head[0] += vel[0]
   head[1] += vel[1]

   if head[0] > tileCount-1: head[0] = 0
   if head[1] > tileCount-1: head[1] = 0
   if head[0] < 0: head[0] = tileCount-1
   if head[1] < 0: head[1] = tileCount-1

   if apple[0] == head[0] and apple[1] == head[1]:

      validPos = False

      while not validPos:
         validPos = True
         apple = [randint(0, tileCount-1) for i in range(2)]
         for sp in parts:
            if sp == apple:
               validPos = False

      length += 1
   
   if gameOver():

      if length > best:
         print('new')
         with open('best.txt', 'w') as f:
            f.write(f'{length}')

      length = 0
      parts *= 0
      vel = [0,0]

      with open('best.txt') as f:
         bestStr = f.readlines()[0]
         print(bestStr)
         best = int(bestStr)

   draw()


pygame.quit()
sys.exit()
