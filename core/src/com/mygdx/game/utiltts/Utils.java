package com.mygdx.game.utiltts;

import static java.lang.Math.abs;

public class Utils {

    public static float getAngle(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.toDegrees((float) Math.atan2(dy, dx));
    }

    public static float makeRotation(float angle, float angleTo, float rotationSpeed, float dt) {
        if (angle < angleTo) {
            if (Math.abs(angle - angleTo) < 180) {
                angle += rotationSpeed * dt;
            } else {
                angle -= rotationSpeed * dt;
            }
        }
        if (angle > angleTo) {
            if (Math.abs(angle - angleTo) < 180) {
                angle -= rotationSpeed * dt;
            } else {
                angle += rotationSpeed * dt;
            }
        }
        if (abs(angle - angleTo) < 1.5f * rotationSpeed * dt) {
            angle = angleTo;
        }
        return angle;
    }

    public static float angleToFromNegPiToPosPi(float ang) {
        while (ang < -180 || ang > 180) {
            if (ang > 180) {
                ang -= 360;
            }
            if (ang < -180) {
                ang += 360;
            }
        }
        return ang;
    }
}