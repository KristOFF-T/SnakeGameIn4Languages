#include <SFML/Graphics.hpp>
#include <iostream>
#include <cmath>
#include <fstream>

using namespace std;

int W = 800, H = 800;
int TS = 40;
int tileCount = 20;
int CX = W/2, CY = H/2;
bool run = true;
int fps = 9;

bool collideRect(sf::RectangleShape rect1, sf::RectangleShape rect2) {
    sf::FloatRect fr1 = rect1.getGlobalBounds();
    sf::FloatRect fr2 = rect2.getGlobalBounds();

    if(fr1.intersects(fr2)) {
        return true;
    }

    return false;
}

void saveBest(int score) {
    ofstream file("best.txt");
    file << to_string(score);
    file.close();
}

sf::RectangleShape getRectByPos(sf::Vector2f pos) {
    sf::RectangleShape rect(sf::Vector2f(TS-2,TS-2));
    rect.setPosition(pos.x*TS+1,pos.y*TS+1);
    return rect;
}

sf::Vector2f getRandomPos(vector<sf::Vector2f> parts) {
    bool valid = false;
    sf::Vector2f pos;
    while(!valid) {
        valid = true;
        pos = sf::Vector2f(rand() % tileCount, rand() % tileCount);
        for(sf::Vector2f spp : parts) {
            if(spp.x == pos.x && spp.y == pos.y) {
                valid = false;
            }
        }
    }
    return pos;
}

void drawText(sf::RenderWindow &window, string text, sf::Vector2f pos, sf::Font &font) {
    sf::Text oText;
    oText.setFont(font);
    oText.setCharacterSize(16);
    oText.setPosition(pos);
    oText.setString(text);
    window.draw(oText);
}

int getBest() {
    string nline;
    std::vector<string> lines = {};
    ifstream file("best.txt");
    if (file.is_open()) {
        while(getline(file, nline)) {
            lines.push_back(nline);
            //cout << nline << endl;
        }
    }
    file.close();

    string bestString = lines[0];
    int bestInt = stoi(bestString);
    return bestInt;
}

void chd(sf::Vector2f &vel, string direction) {
    
}

int main() {
    int best = getBest();
    bool started = 0;
    int rpcd = 0;

    sf::RenderWindow window(sf::VideoMode(W, H), "Snake Game", sf::Style::Titlebar);
    window.setFramerateLimit(fps);

    vector<sf::Vector2f> snakeParts = {};
    sf::Vector2f headPos(10, 10);
    sf::Vector2f snakeVel(0,0);
    sf::Vector2f applePos(5, 5);

    sf::Font font;
    font.loadFromFile("FFFFORWA.TTF");

    int tailLength = 2;

    while(run) {
        snakeParts.push_back(headPos);
        if(snakeParts.size()-1 > tailLength) {
            snakeParts.erase(snakeParts.begin());
        }

        sf::Event event;
        while(window.pollEvent(event)) {
            switch (event.type) {
            case event.Closed:
                run = false;
                break;

            case event.KeyPressed:
                started = true;
                break;
            }

            if(sf::Keyboard::isKeyPressed(sf::Keyboard::Escape)) run = false;

            if(sf::Keyboard::isKeyPressed(sf::Keyboard::Up) ||
                    sf::Keyboard::isKeyPressed(sf::Keyboard::W)) {
                if(snakeVel.y == 0) {
                    snakeVel.x = 0;
                    snakeVel.y = -1;
                }
            }else if(sf::Keyboard::isKeyPressed(sf::Keyboard::Down) ||
                    sf::Keyboard::isKeyPressed(sf::Keyboard::S)) {
                if(snakeVel.y == 0) {
                    snakeVel.x = 0;
                    snakeVel.y = 1;
                }
            }else if(sf::Keyboard::isKeyPressed(sf::Keyboard::Left) ||
                    sf::Keyboard::isKeyPressed(sf::Keyboard::A)) {
                if(snakeVel.x == 0) {
                    snakeVel.x = -1;
                    snakeVel.y = 0;
                }
            }else if(sf::Keyboard::isKeyPressed(sf::Keyboard::Right) ||
                    sf::Keyboard::isKeyPressed(sf::Keyboard::D)) {
                if(snakeVel.x == 0) {
                    snakeVel.x = 1;
                    snakeVel.y = 0;
                }
            }
        }

        if(rpcd > 0) {
            snakeVel.x = 0;
            snakeVel.y = 0;
            rpcd--;
            started = false;
        }

        //snake move
        headPos.x += snakeVel.x;
        headPos.y += snakeVel.y;

        //cmo
        if(headPos.x < 0) headPos.x = tileCount-1;
        if(headPos.x > tileCount-1) headPos.x = 0;
        if(headPos.y < 0) headPos.y = tileCount-1;
        if(headPos.y > tileCount-1) headPos.y = 0;

        //apple eat
        if(applePos.x == headPos.x && applePos.y == headPos.y) {
            applePos = getRandomPos(snakeParts);
            tailLength++;
        }

        //check alive
        for(sf::Vector2f spp : snakeParts) {
            if(spp.x == headPos.x && spp.y == headPos.y && started) {
                if(tailLength-2 > best) {
                    best = tailLength-2;
                    saveBest(best);
                }
                snakeParts.clear();
                tailLength = 2;
                rpcd = 30;
            }
        }

        window.clear();

        //snake parts
        for(sf::Vector2f spp : snakeParts) {
            sf::RectangleShape rect = getRectByPos(spp);
            rect.setFillColor(sf::Color::Blue);

            window.draw(rect);
        }

        //head
        sf::RectangleShape headRect = getRectByPos(headPos);
        headRect.setFillColor(sf::Color::White);
        window.draw(headRect);

        //apple
        sf::RectangleShape appleRect = getRectByPos(applePos);
        appleRect.setFillColor(sf::Color::Red);
        window.draw(appleRect);

        drawText(window, "Score: " + to_string(tailLength-2), sf::Vector2f(16,16), font);
        drawText(window, "Best: " + to_string(best), sf::Vector2f(16, 50), font);

        window.display();

    }

    return 0;
}

