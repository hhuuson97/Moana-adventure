package com.mygdx.game;

class Coordinate {
    double x, y, z;
    Coordinate() {}
    Coordinate(double tx, double ty) {
        x = tx;
        y = ty;
        z = 0;
    }
    Coordinate(double tx, double ty, double tz) {
        x = tx;
        y = ty;
        z = ty;
    }

    double length(Coordinate c) { return Math.sqrt((x - c.x) * (x - c.x) + (y - c.y) * (y - c.y) + (z - c.z) * (z - c.z));}
}
