package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

public class Calculator {
    private static MyVector droppedPerpendicular;
    private static MyVector a_H;
    private static MyVector a_N;
    private static MyVector a_HR;
    private static MyVector a_GR;
    private static double f_H;
    private static double f_N;
    private static double rk_H;
    private static double rk_G;
    private static double f_R_H;
    private static double f_R_G;
    private static double f_R_max;
    private static double angle_max_H;
//    a_G = Utils.GRAVITY;

    /*
    * Ebenengleichung für das Collisionsrechteck.
    * Diese ist so konstruiert, dass sie die Ebene aus dem Mittelpunkt heraus aufzieht. Wir benötigen das, da wir unseren
    * Block samt ViewTexture um seinen Mittelpunkt rotieren.
    * Zur Veranschaulichung:
    *       e_x: (rX + 1/2*rW) + 1/2 ( s*rW*( cos(a)) + t*rH(sin(a)) )
    *       e_y: (rY + 1/2*rH) + 1/2 ( s*rW*(-sin(a)) + t*rH(cos(a)) )
    * Umgestellt nach s (waagerechter Parameter:
    *               (  (bX - rX - 1/2rW)*(1 - sin(a)^2)        (bY - rY - 1/2rH) * sin(a)   )
    *       s = 2 * ( ---------------------------------   -   ----------------------------  )
    *               (            rW*cos(a)                                rW                )
    *
    * Umgestellt nach t (senkrechter Parameter:
     *               (  (bX - rX - 1/2rW) * sin(a) + (bY - rY - 1/2rH) * cos(a)  )
     *       t = 2 * ( --------------------------------------------------------  )
     *               (                          rH                               )
     *
     * Die Ebene ist so konstruiert, dass RV_1 (waagerecht) und RV_2 (senkrecht) orthogonal zueinander stehen und diesen
     * Winkel immer beibehalten. Seine Ausrichtung ist an die Achsen des JavaFX Koordinatensystems angepasst (→ +x, ↓ +y)
     * und die erhaltenen Winkel werden in der Klasse Rotation gesetzt und ein Parameter e_alpha erstellt, der die
     * gegenläufige Rotation herausrechnet.
     * Dadurch können wir die erhaltenen Winkel und die Position des Balls einsetzen um s und t zu erhalten.
     * Die verschiedenen Kombinationen beachtet, sind Kollisionen bei -1 <= s,t <= 1 möglich und s & t liefern direkt
     * die Position des Lotfußes, passt man den jeweils anderen an.
     *        ----------------------------------------------------- → x
     *      |
     *      |              -1            s             1
     *      |           -1 -----------------------------
     *      |             |                            |
     *      |           t |             X              |
     *      |             |                            |
     *      |            1 -----------------------------
     *      ↓ y
     * */

    //_Calculate_Plane_Parameters_______________________________________________________________________________________
    public static MyVector calc_s_t_Parameters(Wall w, Ball b) {
        MyVector deltas = calcDeltas(w, b);
        double s = 2 * (
                (deltas.x * ( 1 - Math.pow(Math.sin(Math.toRadians( w.getE_alpha() )), 2) ) )
                        /( w.getCollision().getWidth() * Math.cos(Math.toRadians( w.getE_alpha() )) )
                        - (deltas.y * Math.sin(Math.toRadians(w.getE_alpha())))
                        /( w.getCollision().getWidth())
        );
        double t = 2 * (
                ( (deltas.x * Math.sin(Math.toRadians( w.getE_alpha() ))) + (deltas.y * Math.cos(Math.toRadians( w.getE_alpha() ))) )
                        /( w.getCollision().getHeight())
        );

        return new MyVector(s,t);
    }
    //_Calculate_deltas_used_in_both_(t_&_s)____________________________________________________________________________
    private static MyVector calcDeltas(Wall w, Ball b) {
        /*
         * The delta fo X and Y are used in s and t, thus an extra method.
         *   xDelta = bX - rX - 1/2rW
         *   yDelta = bY - rY - 1/2rH
         * */
        return new MyVector(
                b.getPosVec().x - w.getCollision().getX() - w.getCollision().getWidth()/2,
                b.getPosVec().y - w.getCollision().getY() - w.getCollision().getHeight()/2
        );
    }

    //_Check_Plane's_sides_for_collisions_______________________________________________________________________________
    public static void calcSideCollisions(Wall w, MyVector s_t, int decision) {
        switch (decision) {
            case 0:
                if(s_t.y < 0)
                    droppedPerpendicular = calcCoord_onEdge(w, new MyVector(s_t.x, -1));
                else
                    droppedPerpendicular = calcCoord_onEdge(w, new MyVector(s_t.x, 1));
                break;
            case 1:
                if(s_t.x < 0)
                    droppedPerpendicular = calcCoord_onEdge(w, new MyVector(-1, s_t.y));
                else
                    droppedPerpendicular = calcCoord_onEdge(w, new MyVector( 1, s_t.y));
                break;
        }
    }
    //_Calculate_"dropped_perpendicular"'s_missing_coordinate___________________________________________________________
    private static MyVector calcCoord_onEdge(Wall w, MyVector s_t) {
        /*
         *       e_x: (rX + 1/2*rW) + 1/2 ( s*rW*( cos(a)) + t*rH(sin(a)) )
         *       e_y: (rY + 1/2*rH) + 1/2 ( s*rW*(-sin(a)) + t*rH(cos(a)) )
         * */
        double x = (w.getCollision().getX() + w.getCollision().getWidth()/2)
                + (   s_t.x * w.getCollision().getWidth()  * Math.cos(Math.toRadians(w.getE_alpha()))
                + s_t.y * w.getCollision().getHeight() * Math.sin(Math.toRadians(w.getE_alpha()))
        )
                /2;
        double y = (w.getCollision().getY() + w.getCollision().getHeight()/2)
                + (   s_t.x * w.getCollision().getWidth()  * Math.sin(Math.toRadians(w.getE_alpha())) *-1
                + s_t.y * w.getCollision().getHeight() * Math.cos(Math.toRadians(w.getE_alpha()))
        )
                /2;

        return new MyVector(x,y);
    }

    //_Overloaded_checkDistance_(for_sides_&_corners)___________________________________________________________________
    public static boolean checkDistance(Ball b, double epsilon, boolean sidesHit) {
        if (sidesHit) {
            double distance = MyVector.distance(b.getPosVec(), droppedPerpendicular);

            return distance < b.getRadius() + epsilon;
        } else {
            return false;
        }
    }
    public static boolean checkDistance(Ball b, MyVector wCoord, double epsilon) {
        return MyVector.distance(b.getPosVec(), wCoord) <= b.getRadius() + epsilon;
    }

    //_Split_and_rearrange_velocity_vector_using_orthogonal_projection__________________________________________________
    public static void bounceVelocity(Ball b) {
        // Calculate values
        // The directional vector between the ball's position and its dropped perpendicular.
        // It has to be normed:
        MyVector normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), droppedPerpendicular));

        // Find the orthogonal and the parallel velocity vectors of b
        MyVector vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
        MyVector vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);

        b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)));
        droppedPerpendicular = new MyVector(0,0);
    }

    private static void calcForces(double a) {
        f_H = Math.sin(Math.toRadians(a) * Utils.FG);
        f_N = Math.cos(Math.toRadians(a) * Utils.FG);
    }

    public static void parameters(Wall w, Ball b, double e) {

    }
    public static void initializeForces(Wall w, Ball b) {
        double a; //alpha
        double x;
        double y;
        /*
         * Beachten Sie weiter, dass das Koordinatensystem von JavaFX auf dem Kopf steht. Die Formeln sind:
         * Betrag der Hangabtriebskraft:
         *   F_H = sin(a) * F_G
         *
         * Betrag der Normalkraft:
         *   F_N = cos(a) * F_G
         *
         * RV für a_H:
         *   a_H_x: -cos(a) * F_H
         *   a_H_y:  sin(a) * F_H
         *
         * RV für a_N:
         *   a_N_x:  sin(a) * F_N
         *   a_N_y:  cos(a) * F_N
         *
         *
         * Reibungskoeffizienten in Ball frcVec gespeichert
         *   rk_H = b.getFrcVec().x;
         *   rk_G = b.getFrcVec().y;
         *
         * Reibungskräfte:
         * Haftreibung:
         *   f_R_H   = rk_H * f_N;
         * maximale Haftreibung, die mit der Hangabtriebskraft überschritten werden muss:
         *   f_R_max = rk_H * f_N;
         *
         * Gleitreibung:
         *   f_R_G   = rk_G * f_N;
         *
         * maximaler Winkel, der möglich ist, ohne, dass der Gegenstand losrutsch: (max Böschungswinkel)
         *   angle_max_H = Math.atan(Math.toRadians(a));
         *
         * */

        /*
         * Entscheidung zwischen rotateLeft und rotateRight
         * Orientation von wall:
         * 0: level
         * 1: Rotation nach links
         * 2: Rotation nach rechts
         *
         * */

        switch (w.getOrientation())
        {
            case 0:
                break;
            case 1: //LINKS
                /*
                * Hangabtriebskraft und Normalkraft berechnen.
                * Winkel: +1++
                * */
                calcForces(w.getE_alpha());
                /*
                 * Hangabtrieb und Normalkraft als Vektoren zeigen beide in Q3 bzw Q4, daher +180
                 * Danach Kräfte in ihre Teilvektoren zerlegen
                 * Winkel: +180++
                 * */
                zersaegenSpaltenUndAufstapeln(b, w.getE_alpha()+180);

                break;
            case 2: //Rechts
                /*
                 * Hangabtriebskraft und Normalkraft berechnen.
                 * Winkel: +1++
                 * */
                calcForces(w.getSpin());
                /*
                 * Hangabtrieb und Normalkraft als Vektoren zeigen beide in Q3 bzw Q4, daher +180
                 * Danach Kräfte in ihre Teilvektoren zerlegen
                 * Winkel: +360--
                 * */
                beeteUmstechen(b, w.getE_alpha());

                break;
        }



//        angle_max_H = Math.atan(Math.toRadians(a)); // TODO Ausrichtung des JavaFX Koordinatensystems schauen
    }

    /*
    * Kräfte in Vektoren zerlegen bei Rotation nach links
    * */
    private static void beeteUmstechen(Ball b, double a) {
        double x, y;

        //Hangabtrieb und Normalkraft als Vektoren zeigen beide in Q3 bzw Q4
        x =  Math.cos(Math.toRadians(a) * f_H);
        y = -Math.sin(Math.toRadians(a) * f_H);
        a_H = new MyVector(x,y);

        x =  Math.sin(Math.toRadians(a) * f_N);      //  cos(a+90) =  sin(a)
        y = Math.cos(Math.toRadians(a) * f_N);     // -sin(a-90) = cos(a)
        a_N = new MyVector(x,y);

        rk_H = b.getFrcVec().x;
        rk_G = b.getFrcVec().y;

        f_R_H = rk_H * f_N;
        f_R_G = rk_G * f_N;
    }

    /*
    * Kräfte in Vektoren zerlegen bei Rotation nach rechts
    * */
    public static void zersaegenSpaltenUndAufstapeln(Ball b, double a) {
        double x,y;

        //Hangabtrieb und Normalkraft als Vektoren zeigen beide in Q3 bzw Q4
        x =  Math.cos(Math.toRadians(a) * f_H);
        y = -Math.sin(Math.toRadians(a) * f_H);
        a_H = new MyVector(x,y);

        x =  Math.sin(Math.toRadians(a) * f_N);      //  cos(a+90) =  sin(a)
        y = -Math.cos(Math.toRadians(a) * f_N);      // -sin(a+90) = -cos(a)
        a_N = new MyVector(x,y);

        rk_H = b.getFrcVec().x;
        rk_G = b.getFrcVec().y;

        f_R_H = rk_H * f_N;
        f_R_G = rk_G * f_N;




    }









    //_GETTERS_SETTERS__________________________________________________________________________________________________
    public static void setDroppedPerpendicular(MyVector droppedPerpendicular) {
        Calculator.droppedPerpendicular = droppedPerpendicular;
    }
    public static MyVector getDroppedPerpendicular() {
        return droppedPerpendicular;
    }
}
