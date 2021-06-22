package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

/**
 * Klasse für Ball gegen Wand Kollisionen
 *
 *    Ebenengleichung für das Kollisionsrechteck.
 *    Diese ist so konstruiert, dass sie die Ebene aus dem Mittelpunkt heraus aufzieht. Wir benötigen das, da wir unseren
 *    Block samt ViewTexture um seinen Mittelpunkt rotieren.
 *    Zur Veranschaulichung:
 *          e_x: posX + 1/2 ( s*rW*( cos(a)) + t*rH(sin(a)) )
 *          e_y: posY + 1/2 ( s*rW*(-sin(a)) + t*rH(cos(a)) )
 *    Umgestellt nach s (waagerechter Parameter:
 *                  (  (bX - posX)*(1 - sin(a)^2)      (bY - posY) * sin(a)  )
 *          s = 2 * ( ---------------------------   -   ------------------   )
 *                  (            rW*cos(a)                       rW          )
 *
 *    Umgestellt nach t (senkrechter Parameter:
 *                  (  (bX - posX) * sin(a) + (bY - posY) * cos(a)  )
 *          t = 2 * ( --------------------------------------------  )
 *                  (                          rH                   )
 *
 *    Die Ebene ist so konstruiert, dass RV_1 (waagerecht) und RV_2 (senkrecht) orthogonal zueinander stehen und diesen
 *    Winkel immer beibehalten. Seine Ausrichtung ist an die Achsen des JavaFX Koordinatensystems angepasst (→ +x, ↓ +y)
 *    und die erhaltenen Winkel werden in der Klasse Rotation gesetzt und ein Parameter e_alpha erstellt, der die
 *    gegenläufige Rotation herausrechnet.
 *    Dadurch können wir die erhaltenen Winkel und die Position des Balls einsetzen um s und t zu erhalten.
 *    Die verschiedenen Kombinationen beachtet, sind Kollisionen bei -1 <= s,t <= 1 möglich und s & t liefern direkt
 *    die Position des Lotfußes, passt man den jeweils anderen an.
 *      ----------------------------------------------------- → x
 *    |
 *    |              -1            s             1
 *    |           -1 -----------------------------
 *    |             |                            |
 *    |           t |             X ------------→|
 *    |             |             |              |
 *    |            1 _____________↓_______________
 *    ↓ y
 *
 *    Es gibt verschiedene Bereiche, die unser s und t kombiniert abdecken.
 *    Je nach vorhandener Kombination müssen wir auf Kollision prüfen oder eine weitere Methode, die gesondert
 *    die Ecken überprüft aufrufen. Bereiche:
 *        ----------------------------------------------------- → x
 *      |
 *      |                            0
 *      |            4 ----------------------------- 5
 *      |             |                            |
 *      |           2 |             X              | 3
 *      |             |                            |
 *      |           6 ----------------------------- 7
 *      ↓ y                         1
 *    e = epsilon
 *    SEITEN: ggf e durch Eckprüfung abfangen
 *          0) Oberseite:   -1 <= s <= 1      &          t = -1
 *          1) Unterseite:  -1 <= s <= 1      &          t =  1
 *          2) Links:             s = -1      &    -1 <= t <= 1
 *          3) Rechts:            s =  1      &    -1 <= t <= 1
 *    ECKEN
 *          4) OL:          -1-r-e <= s <= -1      &    -1-r-e <= t <= -1
 *          5) OR:               1 <= s <= 1+r+e   &    -1-r-e <= t <= -1
 *          6) UL:          -1-r-e <= s <= -1      &         1 <= t <= 1+r+e
 *          7) UR:               1 <= s <= 1+r+e   &         1 <= t <= 1+r+e
 * */
public class WallCollisions {
    static double s;
    static double t;
    private static boolean collision_onEdge;
    private static boolean collision_onCorner;
    private static boolean s_onPosCorner;
    private static boolean s_onNegCorner;
    private static boolean t_onPosCorner;
    private static boolean t_onNegCorner;
    static int corner;
    private static MyVector s_t_Parameters;
    private static MyVector possibleCorner = new MyVector(0,0);

    public static void checkWalls(Wall w, Ball b, double e) {
        s_t_Parameters = WallCalculations.calc_s_t_Parameters(w, b.getPosVec());
        checkSides(w, b, e);
        checkCorners(w, b, e);
        if (collision_onEdge || collision_onCorner)
        {
            if(collision_onEdge)
            {
                CollisionHandling.bounceVelocity(b, WallCalculations.side, WallCalculations.droppedPerpendicular);
                if(WallCalculations.side == 0 & b.isColliding_Orthogonal_F()) {
                    CollisionHandling.stopBouncing(b);
                }
            }
            else
                CollisionHandling.bounceVelocity(b, corner, possibleCorner);

                if(w.getOrientation() == 2){
                    b.setAccVec(MyVector.add(b.getAccVec(), new MyVector(0, -Utils.CONSTANT_OF_GRAVITATION)));
                }
                else if (w.getOrientation() == 0) {                            // Rotation nach LINKS
                    WallCalculations.initializeForces(w, b, 0);
                }
                else if (w.getOrientation() == 1) {                            // Rotation nach RECHTS
                    WallCalculations.initializeForces(w,b,1);
                }

            reset();
            WallCalculations.reset();
            CollisionHandling.reset();
            Collisions.reset();
        }
    }

    //_Split_and_rearrange_velocity_vector_using_orthogonal_projection__________________________________________________
    private static void bounceVelocity(Ball b) {
        // Calculate values
        // The directional vector between the ball's position and its dropped perpendicular.
        // It has to be normed:
        MyVector normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), possibleCorner));

        // Find the orthogonal and the parallel velocity vectors of b
        MyVector vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
        MyVector vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);

//        b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)));


        if( (corner == 4 & vParallel.x < 0)
                || (corner == 7 & vParallel.x > 0)
                || (corner == 5 & vParallel.x < 0)
                || (corner == 6 & vParallel.x > 0))
        {
            b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -b.getElasticity())));
        }
        b.setColliding_Parallel_B(false);
    }
    private static void checkCorners(Wall w, Ball b, double e) {
        s_onPosCorner = (s >=  1 + (-b.getRadius()-e)/w.getCollision().getWidth()/2)   &  (s <=  1 + (b.getRadius()+e)/w.getCollision().getWidth()/2);
        s_onNegCorner = (s >= -1 + (-b.getRadius()-e)/w.getCollision().getWidth()/2)   &  (s <= -1 + (b.getRadius()+e)/w.getCollision().getWidth()/2);
        t_onPosCorner = (t >=  1 + (-b.getRadius()-e)/w.getCollision().getHeight()/2)  &  (t <=  1 + (b.getRadius()+e)/w.getCollision().getHeight()/2);
        t_onNegCorner = (t >= -1 + (-b.getRadius()-e)/w.getCollision().getHeight()/2)  &  (t <= -1 + (b.getRadius()+e)/w.getCollision().getHeight()/2);

        if(s_onPosCorner & t_onPosCorner) {
            possibleCorner = WallCalculations.calcCoord_onEdge(w, new MyVector(1, 1));
            corner = 1;
        }
        else if(s_onPosCorner & t_onNegCorner) {
            possibleCorner = WallCalculations.calcCoord_onEdge(w, new MyVector(1,-1));
            corner = 1;
        }
        else if(s_onNegCorner & t_onNegCorner) {
            possibleCorner = WallCalculations.calcCoord_onEdge(w, new MyVector(-1,-1));
            corner = 3;
        }
        else if(s_onNegCorner & t_onPosCorner) {
            possibleCorner = WallCalculations.calcCoord_onEdge(w, new MyVector(-1,1));
            corner = 3;
        }
        collision_onCorner = Calculator.checkDistance(b, possibleCorner, e);
        b.setColliding_Parallel_B(collision_onCorner);

    }

    private static void checkSides(Wall w, Ball b, double e) {
        s = s_t_Parameters.x;
        t = s_t_Parameters.y;
        boolean s_onEdge = s >= -1 && s <= 1;
        boolean t_onEdge = t >= -1 && t <= 1;

        if (s_onEdge) {
            WallCalculations.calcDroppedPerpendicular(w, s_t_Parameters, 0);
        }
        if (t_onEdge) {
            WallCalculations.calcDroppedPerpendicular(w, s_t_Parameters, 1);
        }
        collision_onEdge = WallCalculations.checkDistance(b, s_onEdge||t_onEdge, e);
        b.setColliding_Parallel_B(collision_onEdge);
    }

    private static void reset() {
        collision_onCorner = false;
        collision_onEdge = false;
        s_onPosCorner = false;
        s_onNegCorner = false;
        t_onPosCorner = false;
        t_onNegCorner = false;
        s_t_Parameters = new MyVector(0,0);
        possibleCorner = new MyVector(0,0);
        s = -5;
        t = -5;
    }

    //_for snapping in Placer__
    public static boolean checkBallPlacement(Wall w, Ball b, MyVector s_t_Parameters, double e) {
        s = s_t_Parameters.x;
        t = s_t_Parameters.y;
        boolean s_onEdge = s >= -1 && s <= 1;
        boolean t_onEdge = t >= -1 && t <= 1;

        if (s_onEdge) {
            Calculator.setDroppedPerpendicular(WallCalculations.calcCoord_onEdge(w, new MyVector(s, -1)));
        }
        return WallCalculations.checkDistance(b, s_onEdge||t_onEdge, e+15);
    }
}
