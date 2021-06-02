package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

public class Calculator {
    private static MyVector droppedPerpendicular;
    private static MyVector a_H = new MyVector(0,0);
    private static MyVector a_N = new MyVector(0,0);
    private static MyVector a_R = new MyVector(0,0);
    private static MyVector a_R_H = new MyVector(0,0);
    private static MyVector a_R_G = new MyVector(0,0);
    private static  MyVector deltas = new MyVector(0,0);
    private static double f_H;
    private static double f_N;
    private static double rk_G;
    private static double f_R_H;
    private static double f_R_G;
    private static double f_R_max;
    private static double angle_max_H;


    //_COLLISION________________________________________________________________________________________________________
    /*
    * Ebenengleichung für das Collisionsrechteck.
    * Diese ist so konstruiert, dass sie die Ebene aus dem Mittelpunkt heraus aufzieht. Wir benötigen das, da wir unseren
    * Block samt ViewTexture um seinen Mittelpunkt rotieren.
    * Zur Veranschaulichung:
    *       e_x: (posX) + 1/2 ( s*rW*( cos(a)) + t*rH(sin(a)) )
    *       e_y: (posY) + 1/2 ( s*rW*(-sin(a)) + t*rH(cos(a)) )
    * Umgestellt nach s (waagerechter Parameter:
    *               (  (bX - posX)*(1 - sin(a)^2)      (bY - posY) * sin(a)  )
    *       s = 2 * ( ---------------------------   -   ------------------   )
    *               (            rW*cos(a)                       rW          )
    *
    * Umgestellt nach t (senkrechter Parameter:
     *               (  (bX - posX) * sin(a) + (bY - posY) * cos(a)  )
     *       t = 2 * ( --------------------------------------------  )
     *               (                          rH                   )
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
    // Currently overloaded, TODO combine
    public static MyVector calc_s_t_Parameters(Wall w, Ball b) {
        deltas = calcDeltas(w, b);
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
    public static MyVector calc_s_t_Parameters(Wall w, MyVector position) {
        deltas = new MyVector(
                position.x - w.getPosVec().x,
                position.y - w.getPosVec().y);

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
         * The delta for X and Y are used in s and t, thus an extra method.
         *   xDelta = bX - wPosX
         *   yDelta = bY - wPosY
         * */
        return new MyVector(
                b.getPosVec().x - w.getPosVec().x,
                b.getPosVec().y - w.getPosVec().y
        );
    }

    //_Check_plane's_sides_for_collisions_______________________________________________________________________________
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
    public static MyVector calcCoord_onEdge(Wall w, MyVector s_t) {
        /*
         *       e_x: (posX) + 1/2 ( s*rW*( cos(a)) + t*rH(sin(a)) )
         *       e_y: (posY) + 1/2 ( s*rW*(-sin(a)) + t*rH(cos(a)) )
         * */
        double x = w.getPosVec().x
                + (   s_t.x * w.getCollision().getWidth()  * Math.cos(Math.toRadians(w.getE_alpha()))
                + s_t.y * w.getCollision().getHeight() * Math.sin(Math.toRadians(w.getE_alpha()))
        )
                /2;
        double y = w.getPosVec().y
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

            return distance <= b.getRadius() + epsilon;
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
        deltas = new MyVector(0,0);
    }


    //_REIBUNG__________________________________________________________________________________________________________
    //_Berechne_HAFT_und_GLEITreibung_basiert_auf_Rotation______________________________________________________________
    public static void initializeForces(Wall w, Ball b, int decision) {
        /*
         * Beachten Sie weiter, dass das Koordinatensystem von JavaFX auf dem Kopf steht.
         * Damit man nicht umdenken muss, sind die Quadrantenangaben, als hätte man das Karthesische darüber gelegt.
         * So ist bezeichnet beispielsweise Q4 den Bereich wo x und y Achse positiv sind (" unten rechts ")
         * Die Formeln sind:
         * Betrag der Hangabtriebskraft:
         *   F_H = sin(a) * F_G
         *
         * Betrag der Normalkraft:
         *   F_N = cos(a) * F_G
         *
         * Reibungskoeffizienten in Ball frcVec gespeichert
         *   rk_H = b.getFrcVec().x;
         *   rk_G = b.getFrcVec().y;
         *
         * Reibungskräfte:
         * Maximale Haftreibung:
         *   f_R_H   = rk_H * F_N;
         *
         * Gleitreibung:
         *   f_R_G   = rk_G * F_N;
         *
         * maximaler Winkel, der möglich ist, ohne, dass der Gegenstand sich bewegt: (max Böschungswinkel)
         *   angle_max_H = Math.atan(rk_H);
         *
         * */

        /*
         * Entscheidung zwischen rotateLeft und rotateRight
         * Orientation von wall:
         * 0: Rotation nach links
         * 1: Rotation nach rechts
         * */

        angle_max_H = Math.atan(b.getFrcVec().x);     //Rückgabe in Radians
        angle_max_H = Math.toDegrees(angle_max_H);    //Wir rechnen aber in Degree
        double vel = MyVector.length(b.getVelVec());

        switch (decision)
        {
            case 0://LINKS
                /*
                 * Gravitationskraft
                 * Hangabtriebskraft und
                 * Normalkraft berechnen.
                 * Winkel: +1++
                 * */
                calcForces(w.getE_alpha(), b);
                /*
                 * Hangabtrieb und Normalkraft als Vektoren zeigen beide in Q3 bzw Q4
                 * Danach Kräfte in ihre Teilvektoren zerlegen
                 * Winkel: +1++
                 * */
                zersaegenSpaltenUndAufstapeln(b, w.getE_alpha());

                if(Math.abs(w.getSpin()) <= angle_max_H & vel <= f_R_H) {
                    calcHaftreibung(b);
                    b.setVelVec(MyVector.add(b.getVelVec(), a_R_H));
                }
                else {
                    calcGleitreibung(b);
                    b.setAccVec(MyVector.add(b.getAccVec(), a_R_G));
                }
                break;
            case 1: //Rechts
                //Winkel: +1++
                calcForces(w.getSpin(), b);
                //Winkel: +360--
                beeteUmstechen(b, w.getE_alpha());
                if(Math.abs(w.getSpin()) <= angle_max_H & vel <= f_R_H) {
                    calcHaftreibung(b);
                    b.setVelVec(MyVector.add(b.getVelVec(), a_R_H));
                }
                else {
                    calcGleitreibung(b);
                    b.setAccVec(MyVector.add(b.getAccVec(), a_R_G));
                }
                break;
        }
    }

    private static void calcForces(double a, Ball b) {
        f_H = Utils.CONSTANT_OF_GRAVITATION * Math.sin(Math.toRadians(a));
        f_N = Utils.CONSTANT_OF_GRAVITATION * Math.cos(Math.toRadians(a));
        f_R_H = b.getFrcVec().x * f_N;
    }

    //LINKS
    /*
    * Kräfte in Vektoren zerlegen bei Rotation nach LINKS
    * a_H:
    * Hangabtriebskraft als Vektor zeigt in Q3
    * Daher a + 180° rechnen, oder -cos(a) und sin(a) verwenden
    *   a_H_x: -cos(a) * F_H
    *   a_H_y:  sin(a) * F_H
    *
    * a_N:
    * Normalkraft als Vektor zeigt in Q4
    * Daher a nochmals +90, oder sin(a) und cos(a) verwenden
    *   a_N_x:  sin(a) * F_N
    *   a_N_y:  cos(a) * F_N
    *
    *a_R:
    * Einheitsvektor Reibung vorbereitet in Gegenrichtung zu Hangabtriebsbeschleunigung
    * als Vektor zeigt in Q1
    * Daher a += 90, oder cos(a) und -sin(a) verwenden
    *   a_R_x:  cos(a)
    *   a_R_y: -sin(a)
    * */
    private static void zersaegenSpaltenUndAufstapeln(Ball b, double a)
    {
        double x,y;
        /*
         * a_H
         * */
        x = -Math.cos(Math.toRadians(a)) * f_H;     //  cos(a+90+90) = -sin(a+90) = -cos(a)
        y =  Math.sin(Math.toRadians(a)) * f_H;     // -sin(a+90+90) = -cos(a+90) =  sin(a)
        a_H = new MyVector(x,y);

        /*
         * a_N
         * */
        x =  Math.sin(Math.toRadians(a)) * f_N;     // -cos(a+90) = sin(a)
        y =  Math.cos(Math.toRadians(a)) * f_N;     //  sin(a+90) = cos(a)
        a_N = new MyVector(x,y);

        /*
         * a_R
         * */
        x =  Math.cos(Math.toRadians(a));      // sin(a+90) =  cos(a)
        y = -Math.sin(Math.toRadians(a));      // cos(a+90) = -sin(a)
        a_R = new MyVector(x,y);
    }

    //RECHTS
    /*
    * Kräfte in Vektoren zerlegen bei Rotation nach RECHTS
    *
    * a_H:
    * Hangabtriebskraft als Vektor zeigt in Q4
    * Den erhaltenen Winkel können wir übernehmen, dieser beträgt +360--.
    * Darauf wenden wir cos(a) und -sin(a) an.
    *   a_H_x:  cos(a) * F_H
    *   a_H_y: -sin(a) * F_H
    *
    * a_N:
    * Normalkraft als Vektor zeigt in Q3
    * Daher a -= 90, oder sin(a) und cos(a) verwenden
    *   a_N_x:  sin(a) * F_N
    *   a_N_y:  cos(a) * F_N
    *
    * a_R:
    * Einheitsvektor Reibung vorbereitet in Gegenrichtung zu Hangabtriebsbeschleunigung
    * als Vektor zeigt in Q2
    * Daher a -= 90, oder -cos(a) und sin(a) verwenden
    *   a_R_x: -cos(a)
    *   a_R_y:  sin(a)
    *
    * */
    private static void beeteUmstechen(Ball b, double a)
    {
        double x, y;

        /*
         * a_H
         * */
        x =  Math.cos(Math.toRadians(a)) * f_H;
        y = -Math.sin(Math.toRadians(a)) * f_H;
        a_H = new MyVector(x,y);

        /*
         * a_N
         * */
        x =  Math.sin(Math.toRadians(a)) * f_N;    //  cos(a-90) = sin(a)
        y =  Math.cos(Math.toRadians(a)) * f_N;    // -sin(a-90) = cos(a)
        a_N = new MyVector(x,y);

        /*
         * a_R
         * */
        x = -Math.cos(Math.toRadians(a));          // sin(a-90) = -cos(a)
        y =  Math.sin(Math.toRadians(a));          // cos(a-90) =  sin(a)
        a_R = new MyVector(x,y);
    }

    //Berechne Haftreibung
    private static void calcHaftreibung(Ball b) {
        a_R_H = MyVector.multiply(b.getVelVec(), -1);   // HAFT_reibungs_BESCHLEUNIGUNG
        b.setAccVec(MyVector.multiply(b.getAccVec(), 0));
    }
    //Berechne Gleitreibung
    private static void calcGleitreibung(Ball b) {
        rk_G = b.getFrcVec().y;                                 // GLEIT_reibungs_KOEFF_izient
        f_R_G = rk_G * f_N;                                     // GLEIT_reibungs_KRAFT

        a_R_G = MyVector.multiply(a_R, f_R_G);                  // GLEIT_reibungs_BESCHLEUNIGUNG
    }


    //_GETTERS_SETTERS__________________________________________________________________________________________________
    public static void setDroppedPerpendicular(MyVector droppedPerpendicular) {
        Calculator.droppedPerpendicular = droppedPerpendicular;
    }
    public static MyVector getDroppedPerpendicular() {
        return droppedPerpendicular;
    }

    public static void setA_H(MyVector a_H) {
        Calculator.a_H = a_H;
    }
    public static MyVector getA_H() {
        return a_H;
    }

    public static void setA_N(MyVector a_N) {
        Calculator.a_N = a_N;
    }
    public static MyVector getA_N() {
        return a_N;
    }

    public static void setA_R_H(MyVector a_R_H) {
        Calculator.a_R_H = a_R_H;
    }
    public static MyVector getA_R_H() {
        return a_R_H;
    }

    public static void setA_R_G(MyVector a_R_G) {
        Calculator.a_R_G = a_R_G;
    }
    public static MyVector getA_R_G() {
        return a_R_G;
    }

    public static void setF_H(double f_H) {
        Calculator.f_H = f_H;
    }
    public static double getF_H() {
        return f_H;
    }

    public static void setF_N(double f_N) {
        Calculator.f_N = f_N;
    }
    public static double getF_N() {
        return f_N;
    }

    public static void setF_R_H(double f_R_H) {
        Calculator.f_R_H = f_R_H;
    }
    public static double getF_R_H() {
        return f_R_H;
    }

    public static void setF_R_G(double f_R_G) {
        Calculator.f_R_G = f_R_G;
    }
    public static double getF_R_G() {
        return f_R_G;
    }

    public static void setF_R_max(double f_R_max) {
        Calculator.f_R_max = f_R_max;
    }
    public static double getF_R_max() {
        return f_R_max;
    }

    public static void setAngle_max_H(double angle_max_H) {
        Calculator.angle_max_H = angle_max_H;
    }
    public static double getAngle_max_H() {
        return angle_max_H;
    }
}
