import pygame

pygame.font.init()

clock = pygame.time.Clock()
FPS = 9

red = (255,0,0)
blue = (0,0,255)
white = (255,255,255)
black = (0,0,0)

parts = []

head = [5,5]
apple = [10,10]

tileCount = 20
TS = 40

SS = 800

length = 3

vel = [0,0]

f = 'FFFFORWA.TTF'

def font(size):
    font = pygame.font.Font(f, size)
    return font

def mts(surface, rect, size, smthing, color, x, y):
    text = font(size).render(smthing, 1, color)
    if rect:
        text_rect = text.get_rect(center = (x, y))
        surface.blit((text), text_rect)
    elif not rect:
        surface.blit(text, (x, y))
    return text
