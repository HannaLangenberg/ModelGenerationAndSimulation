package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

public class WallCalculations {
    //Collision
    static MyVector deltas = new MyVector(0,0);
    static MyVector droppedPerpendicular;
    //Friction
    static MyVector a_H = new MyVector(0,0);
    static MyVector a_N = new MyVector(0,0);
    private static MyVector a_R = new MyVector(0,0);
    static MyVector a_R_H = new MyVector(0,0);
    static MyVector a_R_G = new MyVector(0,0);
    static MyVector a_R_R = new MyVector(0,0);
    static double f_H;
    static double f_N;
    private static double rk_G;
    private static double rk_R;
    static double f_R_H;
    static double f_R_G;
    static double f_R_R;
    static double angle_max_H;
    static double angle_max_R;
    static int side; //0 = top, 1 = right, 2 = bottom, 3 = left

    //COLLISION_________________________________________________________________________________________________________
    /**
     * This method calculates the s (scaling factor on long side of the wall) and t (on short side) for the
     * dropped perpendicular of the current ball.
     *
     * @param w             current wall
     * @param position      ball's centerpoint
     * @return              s and t as a new MyVector
     */
    public static MyVector calc_s_t_Parameters(Wall w, MyVector position) {
        deltas = calcDeltas(w, position);

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

    /**
     * The delta for X and Y are used in s and t, thus calculated in an extra method.
     *
     * @param w             current wall
     * @param position      ball's centerpoint
     * @return              delta as a new MyVector
     */
    private static MyVector calcDeltas(Wall w, MyVector position) {
        return new MyVector(
                position.x - w.getPosVec().x,
                position.y - w.getPosVec().y
        );
    }

    /**
     * Calculates any needed coordinate on the wall, mainly along the edges.
     *
     * @param w         current wall
     * @param s_t       the scaling factors s and t
     * @return          calculated point as a new MyVector
     */
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

    /**
     * Calculated the dropped perpendicular with the provided s and t scaling factors.
     *
     * @param w             current wall
     * @param s_t           the scaling factors s and t
     * @param decision      decision variable for which side
     */
    public static void calcDroppedPerpendicular(Wall w, MyVector s_t, int decision) {
        switch (decision) {
            case 0:
                if(s_t.y < 0) {
                    droppedPerpendicular = calcCoord_onEdge(w, new MyVector(s_t.x, -1));
                    side = 0;
                }
                else {
                    droppedPerpendicular = calcCoord_onEdge(w, new MyVector(s_t.x, 1));
                    side = 2;
                }
                break;
            case 1:
                if(s_t.x < 0) {
                    droppedPerpendicular = calcCoord_onEdge(w, new MyVector(-1, s_t.y));
                    side = 3;
                }
                else {
                    droppedPerpendicular = calcCoord_onEdge(w, new MyVector(1, s_t.y));
                    side = 1;
                }
                break;
        }
    }

    /**
     * Checks if the ball is close enough to the wall to be colliding
     *
     * @param b             current ball
     * @param sidesHit      if a collision is probable
     * @param epsilon       double, safety distance for checking for collisions.
     * @return              true if ball is colliding, false if not
     */
    public static boolean checkDistance(Ball b, boolean sidesHit, double epsilon) {
        if (sidesHit) {
            double distance = MyVector.distance(b.getPosVec(), droppedPerpendicular);

            return distance <= b.getRadius() + epsilon;
        } else {
            return false;
        }
    }

    /**
     * FRICTION_________________________________________________________________________________________________________
     * Zur Berechnung der Reibung muss beachtet werden, dass das Koordinatensystem von JavaFX auf dem Kopf steht.
     * Damit man nicht umdenken muss, sind die Quadrantenangaben, als hätte man das Karthesische darüber gelegt.
     * So ist bezeichnet beispielsweise Q4 den Bereich wo x und y Achse positiv sind ( "unten rechts" )
     * Die Formeln sind:
     *      Betrag der Hangabtriebskraft:    F_H = sin(a) * F_G
     *      Betrag der Normalkraft:          F_N = cos(a) * F_G
     * Für die zweite Abgabe hatten wir Gleitreibung und Haftreibung verwendet. Da wir allerdings Reibung auf eine Kugel
     * anwenden möchten, wurde im Folgenden auf Rollreibung umgestellt. Allerdings haben wir die Haft- und Gleitreibung
     * im Code gelassen, da sie unsere Arbeit zeigt.
     * __noch von der Haft- und Gleitreibung______________________________
     * Die Reibungskoeffizienten sind für jeden Ball in frcVec gespeichert
     *      rk_H = b.getFrcVec().x;
     *      rk_G = b.getFrcVec().y;
     * Reibungskräfte:
     *      Maximale Haftreibung:            f_R_H = rk_H * F_N;
     *              Gleitreibung:            f_R_G = rk_G * F_N;
     *
     * maximaler Winkel, der möglich ist, ohne, dass der Gegenstand sich bewegt:
     *      max Böschungswinkel:             angle_max_H = Math.atan(rk_H);
     * __jetzt Rollreibung________________________________________________
     * Der Rollreibungskoeffizient ist für jeden Ball in rolVec gespeichert
     *      rk_R = b.getRolVec().x;
     * Rollreibungskräfte:
     *             Rolltreibung:             f_R_R = rk_R * F_N;
     * "maximaler Rollreibungswinkel", der möglich ist, ohne, dass der Gegenstand sich bewegt:
     *                                       angle_max_R = Math.atan(rk_R/Radius);
     *
     * */

    /**
     * Initialize the forces and accelerations that are needed for friction.
     *                      (Formerly Haft und Gleitreibung → commented out)
     * Currently Rollreibung.
     *
     * @param w             current wall
     * @param b             current ball
     * @param decision      decision variable for current rotation (0 = left, 1 = right)
     */
    public static void initializeForces(Wall w, Ball b, int decision) {
        /*
         * Entscheidung zwischen rotateLeft und rotateRight
         * Orientation von wall:
         * 0: Rotation nach links
         * 1: Rotation nach rechts
         * */

//        angle_max_H = Math.atan(b.getFrcVec().x);                   //Rückgabe in Radians
//        angle_max_H = Math.toDegrees(angle_max_H);                  //Wir rechnen aber in Degree

        angle_max_R = Math.atan(b.getRolVec().x / b.getRadius());     //Rückgabe in Radians
        angle_max_R = Math.toDegrees(angle_max_R);                    //Wir rechnen aber in Degree
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

                if(Math.abs(w.getSpin()) <= angle_max_R & vel <= f_R_R) {
                    dontMove(b);
                }
                else {
                    calcRollreibung(b);
                    b.setAccVec(MyVector.add(b.getAccVec(), a_R_R));
                }
                /*if(Math.abs(w.getSpin()) <= angle_max_H & vel <= f_R_H) {
                    calcHaftreibung(b);
                    b.setVelVec(MyVector.add(b.getVelVec(), a_R_H));
                }
                else {
                    calcGleitreibung(b);
                    b.setAccVec(MyVector.add(b.getAccVec(), a_R_G));
                }*/
                break;
            case 1: //Rechts
                //Winkel: +1++
                calcForces(w.getSpin(), b);
                //Winkel: +360--
                beeteUmstechen(b, w.getE_alpha());
                if(Math.abs(w.getSpin()) <= angle_max_R & vel <= f_R_R) {
                    dontMove(b);
                }
                else {
                    calcRollreibung(b);
                    b.setAccVec(MyVector.add(b.getAccVec(), a_R_R));
                }
                /*if(Math.abs(w.getSpin()) <= angle_max_H & vel <= f_R_H) {
                    calcHaftreibung(b);
                    b.setVelVec(MyVector.add(b.getVelVec(), a_R_H));
                }
                else {
                    calcGleitreibung(b);
                    b.setAccVec(MyVector.add(b.getAccVec(), a_R_G));
                }*/
                break;
        }
    }

    /**
     * As long as the angle at which the wall is rotated is smaller than the max Rollreibungswinkel, the ball does not
     * move as the forces that are applied to it cancel each other out.
     *
     * @param b     current ball
     */
    private static void dontMove(Ball b) {
        b.setAccVec(MyVector.add(b.getAccVec(), new MyVector(0, -Utils.CONSTANT_OF_GRAVITATION)));
    }

    private static void calcForces(double a, Ball b) {
        f_H = Utils.CONSTANT_OF_GRAVITATION * Math.sin(Math.toRadians(a));
        f_N = Utils.CONSTANT_OF_GRAVITATION * Math.cos(Math.toRadians(a));
        //f_R_H = b.getFrcVec().x * f_N;
    }

    //LEFT
    /**
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
        x =  Math.cos(Math.toRadians(a));           // sin(a+90) =  cos(a)
        y = -Math.sin(Math.toRadians(a));           // cos(a+90) = -sin(a)
        a_R = new MyVector(x,y);
    }

    //RIGHT
    /**
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

    /**
     * Calculate Haftreibung
     *
     * @param b     current ball
     */
    private static void calcHaftreibung(Ball b) {
        a_R_H = MyVector.multiply(b.getVelVec(), -1);   // HAFT_reibungs_BESCHLEUNIGUNG
        b.setAccVec(MyVector.add(b.getAccVec(), new MyVector(0, -Utils.CONSTANT_OF_GRAVITATION)));
    }

    /**
     * Calculate Gleitreibung
     *
     * @param b     current ball
     */
    private static void calcGleitreibung(Ball b) {
        rk_G = b.getFrcVec().y;                                 // GLEIT_reibungs_KOEFF_izient
        f_R_G = rk_G * f_N;                                     // GLEIT_reibungs_KRAFT

        a_R_G = MyVector.multiply(a_R, f_R_G);                  // GLEIT_reibungs_BESCHLEUNIGUNG
    }

    /**
     * Calculate Rollreibung
     *
     * @param b     current ball
     */
    private static void calcRollreibung(Ball b) {
        rk_R = b.getRolVec().x;
        f_R_R = rk_R * f_N;

        a_R_R = MyVector.multiply(a_R, f_R_R);
    }

    //RESET_____________________________________________________________________________________________________________
    public static void reset() {
        droppedPerpendicular = deltas = a_H = a_N = a_R = a_R_H = a_R_G = new MyVector(0,0);
        f_H = f_N = rk_G = f_R_H = f_R_G = angle_max_H = side = 0;
    }

}
