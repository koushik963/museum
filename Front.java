
package front;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author koushik
 */
public class Front {

    /**
     * @param args the command line arguments
     */
    private static int wallsets;
    private static int artpieces;
    private static int guards;
    private static int no_of_walls;
    private static int node;
    private static String[] str;
    private static int index, index2;
    private static String[] guard_vis;
    private static int tot_art;
    private static int case_num;
    private static ArrayList curves = new ArrayList();
    private static Point[][] walls_p;
    private static Point[] art_p;
    private static Point[] guard_p;
    private static int[] art_level;
    private static int[] guard_level;
    private static ArrayList lines = new ArrayList();
    private static double radii[][];
    private static int[][] Dx;
    private static int[][] Dy;
    private static Point2D[][] centers;
    private static ArrayList<Line2D> walls_lines = new ArrayList();
    private static ArrayList<Line2D> all_lines = new ArrayList();
    private static boolean atleastone;
    private static boolean major;
    private static int invisible;
//  private int curves[];
    public Front() {
        node = 0;
        //str=new String[6];
        index = 0;
        invisible=0;
        index2 = 0;
        case_num = 0;
        atleastone=false;
        major=false;
    }

    public static void process(String museum, String assignment) throws FileNotFoundException, IOException {
        String temp;
        BufferedReader br = new BufferedReader(new FileReader(museum));
        temp = br.readLine();
        CharSequence s = "s";
        CharSequence c = "c";
        String[] temp25 = temp.split("\\s");
        wallsets = Integer.parseInt(temp25[0]);
        artpieces = Integer.parseInt(temp25[1]);
        guards = Integer.parseInt(temp25[2]);
        temp = "";
        // 
        guard_vis = new String[guards];
        walls_p = new Point[wallsets][];
        art_p = new Point[artpieces];
        guard_p = new Point[guards];
        art_level = new int[artpieces];
        guard_level = new int[guards];
        str = new String[artpieces + guards + 1];
        for (int j = 0; j < wallsets; j++) {
            no_of_walls = Integer.parseInt(br.readLine());
            Dx = new int[wallsets][no_of_walls];
            Dy = new int[wallsets][no_of_walls];
            walls_p[j] = new Point[no_of_walls];
            for (int i = 0; i < no_of_walls; i++) {
                int x, y;
                temp = br.readLine();
                String[] temp1 = temp.split("\\s");

                x = Integer.parseInt(temp1[0]);
                y = Integer.parseInt(temp1[1]);
                walls_p[j][i] = new Point(x, y);

                if (temp.contains(s)) {
                    lines.add(i);
                }
                if (temp.contains(c)) {
                    curves.add(i);
                    /* dx.add(Integer.parseInt(temp1[3]));
                     dy.add(Integer.parseInt(temp1[4]));*/
                    Dx[j][i] = Integer.parseInt(temp1[3]);
                    Dy[j][i] = Integer.parseInt(temp1[4]);
                }

                temp = "";
                temp1 = new String[0];
            }

        }
       // System.out.println("no of curves="+curves.get(0)+"----"+curves.get(1));
        temp = "";
        //drawing curves
        radii = new double[wallsets][curves.size()];
        centers = new Point2D[wallsets][curves.size()];
        drawcurves();
        //generateLines();
        for (int i = 0; i < artpieces; i++) {
            int x, y;
            temp = br.readLine();
            String[] temp1 = temp.split("\\s");
            x = Integer.parseInt(temp1[0]);
            y = Integer.parseInt(temp1[1]);
            art_level[i] = Integer.parseInt(temp1[2]);
            art_p[i] = new Point(x, y);
        }
        for (int h = 0; h < art_level.length; h++) {
            tot_art = tot_art + art_level[h];
        }
        temp = "";

        for (int i = 0; i < guards; i++) {
            int x, y;
            temp = br.readLine();
            String[] temp1 = temp.split("\\s");
            x = Integer.parseInt(temp1[0]);
            y = Integer.parseInt(temp1[1]);
            guard_level[i] = Integer.parseInt(temp1[2]);
            guard_p[i] = new Point(x, y);
        }
        for (int j = 0; j < wallsets; j++) {
            for (int i = 0; i < lines.size(); i++) {
                int t = (int) lines.get(i);
                Line2D line;
                if (t < no_of_walls - 1) {
                    line = new Line2D.Float(walls_p[j][t], walls_p[j][t + 1]);
                    walls_lines.add(line);
                } else if (t == no_of_walls - 1) {
                    line = new Line2D.Float(walls_p[j][t], walls_p[j][0]);
                    walls_lines.add(line);
                }

            }
        }

        for (int i = 0; i < guards; i++) {
            for (int j = 0; j < artpieces; j++) {

                Line2D line = new Line2D.Float(guard_p[i], art_p[j]);
                all_lines.add(line);
            }
        }
        //int total_lines= walls_lines.size()+all_lines.size();
        ArrayList<Line2D> conflict = new ArrayList();
        for (int i = 0; i < all_lines.size(); i++) {
            for (int j = 0; j < walls_lines.size(); j++) {
                Line2D temp1 = (Line2D) all_lines.get(i);

                Line2D temp2 = (Line2D) walls_lines.get(j);
                if (temp1.intersectsLine(temp2)) {
                    conflict.add(temp1);
                    break;
                }

            }

        }
        //building graph
        str[index] = "";
       // System.out.println("guards="+guards);
        for (int i = 0; i < guards; i++) {
            str[index] = str[index] + (i + 1) + " " + guard_level[i] + " ";
          //  System.out.println(str[index]);

        }
        index++;
        str[index] = "";
        // end of 
        node = guards + 1;

    //building graph
        //  node=guards+1;
        for (int i = 0; i < guards; i++) {
            //in`t t=art_level[i],count=0;
            node = i + 1;
            guard_vis[index2] = "";
            int m = 0;
            for (int j = 0; j < artpieces; j++) {
                Line2D art_line = new Line2D.Float(guard_p[i], art_p[j]);
                for (int k = 0; k < walls_lines.size(); k++) {
                    Line2D temp_line = (Line2D) walls_lines.get(k);
                    if (art_line.intersectsLine(temp_line) || curve_inter((int) guard_p[i].getX(), (int) guard_p[i].getY(), (int) art_p[j].getX(), (int) art_p[j].getY())) {

                        break;
                    } else if (k == walls_lines.size() - 1) {
                        atleastone=true;
                        try (PrintStream out = new PrintStream(new FileOutputStream("F:\\test.txt"))) {
                      //  out.print((i+1)+" "+guard_level[i]+" ");
                            //  out.print((node)+" "+1+" ");
                            str[index] = str[index] + (guards + 1 + j) + " " + 1 + " ";
                            node = guards + 1 + j;
                            //System.out.println(str[index]);
                            if (m == 0) {
                                guard_vis[index2] = guard_vis[index2] + "G" + i + ":" + "A" + j + ",";
                                m++;
                            } else if (j == artpieces - 1) {
                                guard_vis[index2] = guard_vis[index2] + "A" + j;
                            } else {
                                guard_vis[index2] = guard_vis[index2] + "A" + j + ",";

                            }

                            //          node++;
                        }
                    }
                    /* else
                     {
                
                     guard_vis[index2]=guard_vis[index2]+"G"+i+":"+"A"+j+",";
                     //index2++;
                
                     }*/
                }

            }
            if(str[index]!="")
            {
            index++;
            index2++;
            major=true;
            invisible++;
            str[index] = "";
            
            }
            /*if(str[index-1]=="")
             {
                 index--;
             } 
            */
        }
        if(!atleastone)
        {
            //System.out.println("entered");
             System.out.println("max flow=0");
            System.out.println("No");
            System.exit(0);
        }
        node = guards + 1 + artpieces;
        // System.out.println("index="+index);
        for (int i = 0; i < artpieces; i++) {
            int temp_level = art_level[i];
            try (PrintStream out = new PrintStream(new FileOutputStream("F:\\test.txt"))) {
                out.println(node + " " + temp_level);
                //  out.println();
                str[index] = str[index] + (node) + " " + temp_level + " ";
                index++;
                // node=guards+1+i;
                if (index < artpieces + guards + 1) {
                    str[index] = "";
                }
                // System.out.println(str[index]);
            }
        }
    //end of
        //System.out.println(""+conflict.size());
        //System.out.println("size="+str.length);
        if(str.length==0)
        {
            System.out.println("max flow=0");
            System.out.println("No");
            System.exit(0);
        }
        for (int k = 0; k < str.length; k++) {
       
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("F:\\output.txt", true)))) {
                if(major)
                {
                    continue;
                }
                if (k == 0) {
                    PrintWriter writer = new PrintWriter("F:\\output.txt");
                    writer.print("");
                    writer.close();
                }
                out.println(str[k]);
                if (k == str.length - 1) { 
                    out.println(" ");
                }

            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
        for (int l = 0; l < guard_vis.length; l++) {
           // System.out.println(guard_vis[l]);
        }
        int p_t=guard_vis.length;
        if(major)
        {
 //           p_t= guard_vis.length-1;
         //      p_t--;
        }
        for (int k = 0; k < p_t-invisible; k++) {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(assignment, true)))) {
                if (k == 0) {
                    PrintWriter writer = new PrintWriter("assignment.txt");
                    writer.print("");
                    writer.close();
                }
               
                out.println(guard_vis[k]);
                if (k == guard_vis.length - 1) {
                    out.println(" ");
                }

            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }

        int mf;
        FFS f = new FFS();
        f.ffs_eval("F:\\output.txt");
        mf = f.maxflow();
        System.out.println("Max flow=" + mf);
        if (mf >= tot_art) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }

    //----------curves part---------------

    public static void drawcurves() {
        calculate_centers();

    }

    public static void calculate_centers() {
        // Point2D Center=new Point2D.Float(); 
        for (int f = 0; f < wallsets; f++) {
            for (int l = 0; l < curves.size(); l++) {
                double x1 = 0.0, y1 = 0.0, x2 = 0.0, y2 = 0.0, x3, y3, dx = 0.0, dy = 0.0, m, m1, xc, yc;
                int c5 = (int) curves.get(l);
                if (c5 < no_of_walls - 1) {
                    x1 = walls_p[f][c5].getX();
                    y1 = walls_p[f][c5].getY();
                    dx = Dx[f][c5];
                    dy = Dy[f][c5];
                    x2 = walls_p[f][c5 + 1].getX();
                    y2 = walls_p[f][c5 + 1].getY();

                } else if (c5 == no_of_walls - 1) {
                    x2 = walls_p[f][0].getX();
                    y2 = walls_p[f][0].getY();
                }

                x3 = (x1 + x2) / 2;
                y3 = (y1 + y2) / 2;

                if (y2 - y1 == 0) {
                    if (dx == 0) {
                        yc = y1;
                        xc = (x1 + x2) / 2;
                    } else {
                        m = dy / dx;
                        xc = (x1 + x2) / 2;
                        yc = (x1 - xc + m * y1) / m;
                    }
                } else {
                    if (dy == 0) {
                        xc = x1;
                        m1 = (x1 - x2) / (y2 - y1);
                        yc = m1 * (xc - x3) + y3;
                    } else {
                        m = (dy / dx);
                        m1 = (x1 - x2) / (y2 - y1);

                        xc = (x1 + m * (y1 - y3 + (m1 * x3))) / (1 + m * m1);
                        yc = (x1 - xc + m * y1) / m;

                    }
                }

                //Calculating radii
                double r = Math.sqrt((xc - x1) * (xc - x1) + (yc - y1) * (yc - y1));

                radii[f][l] = r;
                Point2D temp_center = new Point2D.Double(xc, yc);
                centers[f][l] = temp_center;
                //centers[f][l]=yc;
            }
        }
    }

    public static boolean curve_inter(int xg, int yg, int xa, int ya) {
        boolean chck = false;
        boolean arc_inter1 = false, arc_inter2 = false;
        double slope2;
        boolean[][] res = new boolean[curves.size()][1];
        boolean[][] res1 = new boolean[curves.size()][1];
        for (int q = 0; q < wallsets; q++) {

            for (int l = 0; l < curves.size(); l++) {
                double xc = centers[q][0].getX();
                double yc = centers[q][0].getY();

                double r = radii[q][l];
                double px1 = 0.0, py1 = 0.0, px2 = 0.0, py2 = 0.0;
                int c7 = (int) curves.get(l);
                if (c7 < no_of_walls - 1) {
                    px1 = walls_p[q][c7].getX();
                    py1 = walls_p[q][c7].getY();
                    px2 = walls_p[q][c7 + 1].getX();
                    py2 = walls_p[q][c7 + 1].getY();
                } else if (c7 == no_of_walls - 1) {
                    px2 = walls_p[q][0].getX();
                    py2 = walls_p[q][0].getY();
                }

                double distance, ix1, ix2, iy1, iy2, c1, c2, a, b;
                int c6 = (int) curves.get(l);
                double x1 = px1 + px2;
                double y1 = px2 + py2;
                boolean result2 = false;
                if ((xg - xa) == 0) {
                    distance = xc + xg;
                    if (distance < radii[q][l]) {

                        ix1 = xa;
                        ix2 = xa;
                        c1 = (r * r) - (xc * xc) - (yc * yc) - (xa * xa) + (2 * xc * xa);
                        iy1 = yc + Math.sqrt((yc * yc) + c1);
                        iy2 = yc - Math.sqrt((yc * yc) + c1);

                        if (arc_inter_test(x1, y1, ix1, iy1, px1, py1, px2, py2) == false) {
                            arc_inter1 = dist_check(xg, yg, xa, ya, ix1, iy1);
                        }
                        if (arc_inter_test(x1, y1, ix2, iy2, px1, py1, px2, py2) == false) {
                            arc_inter2 = dist_check(xg, yg, xa, ya, ix2, iy2);
                        }
                        if (arc_inter1 || arc_inter2) {
                            res1[l][0] = true;
                        }
                        /*System.out.println(ix1+" "+iy1+" "+ix2+" "+iy2);*/
                    }

                } else if ((yg - ya) == 0) {
                    distance = yc + yg;
                    iy1 = ya;
                    iy2 = ya;
                    c1 = (r * r) - (xc * xc) - (yc * yc) - (ya * ya) + (2 * yc * ya);
                    ix1 = xc + Math.sqrt((xc * xc) + c1);
                    ix2 = xc - Math.sqrt((xc * xc) + c1);
                    if (arc_inter_test(x1, y1, ix1, iy1, px1, py1, px2, py2) == false) {
                        arc_inter1 = dist_check(xg, yg, xa, ya, ix1, iy1);
                    }
                    if (arc_inter_test(x1, y1, ix2, iy2, px1, py1, px2, py2) == false) {
                        arc_inter2 = dist_check(xg, yg, xa, ya, ix2, iy2);
                    }
                    if (arc_inter1 || arc_inter2) {
                        res1[l][0] = true;
                    }
                    /*System.out.println(ix1+" "+iy1+" "+ix2+" "+iy2);*/
                } else {
                    slope2 = (yg - ya) / (xg - xa);
                    distance = ((slope2 * xc) - yc + (ya - xa)) / Math.sqrt((slope2 * slope2) + 1);
                    c1 = ya - xa;
                    c2 = (r * r) - (c1 * c1) + (2 * yc * c1) - (xc * xc) - (yc * yc);
                    a = 1 + (slope2 * slope2);
                    b = (2 * slope2 * c1) - (2 * xc) - (2 * yc * slope2);
                    ix1 = (-b + Math.sqrt((b * b + (4 * a * c2))) / (2 * a));
                    ix2 = (-b - Math.sqrt((b * b + (4 * a * c2))) / (2 * a));
                    iy1 = slope2 * ix1 + c1;
                    iy2 = slope2 * ix2 + c1;
                    if (arc_inter_test(x1, y1, ix1, iy1, px1, py1, px2, py2) == false) {
                        arc_inter1 = dist_check(xg, yg, xa, ya, ix1, iy1);
                    }
                    if (arc_inter_test(x1, y1, ix2, iy2, px1, py1, px2, py2) == false) {
                        arc_inter2 = dist_check(xg, yg, xa, ya, ix2, iy2);
                    }
                    if (arc_inter1 || arc_inter2) {
                        res1[l][0] = true;
                    }
                    /*System.out.println(ix1+" "+iy1+" "+ix2+" "+iy2);*/
                }
            }
        }
        for (int l = 0; l < curves.size(); l++) {
            if (res1[l][0] == true) {
                chck = true;
            }
        }
        return (chck);
        //   return false;
    }

    public static boolean arc_inter_test(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        if (cal(x1, y1, x3, y3, x4, y4) == cal(x2, y2, x3, y3, x4, y4)) {
            return false;
        } else if (cal(x1, y1, x2, y2, x3, y3) == cal(x1, y1, x2, y2, x4, y4)) {
            return false;
        } else {
            return true;
        }

    }

    public static boolean cal(double x1, double y1, double x2, double y2, double x3, double y3) {
        return (y3 - y1) * (x2 - x1) > (y2 - y1) * (x3 - x1);
    }

    //Method to check if the intersections points are on the line segment joining the art piece and guard
    public static boolean dist_check(double x1, double y1, double x2, double y2, double x3, double y3) {
        boolean result_here = false;
        if ((Math.sqrt((x1 - x3) * (x1 - x3) + (y1 - y3) * (y1 - y3)) + Math.sqrt((x1 - x3) * (x1 - x3) + (y1 - y3) * (y1 - y3))) == Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))) {
            result_here = true;
        }
        return (result_here);
    }

    //------------end of curves part------
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        //BufferedReader br=new BufferedReader(new FileReader("F://input.txt"));

        // f.process();
        String str = args[0];
        switch (str) {
            case "-f":
                 long startTime1 = System.currentTimeMillis();
                
                String file = args[1];
                FFS f1 = new FFS();
                f1.ffs_eval(file);
                int mf2 = f1.maxflow();
                System.out.println("Max Flow=" + mf2);
                 long endTime1 = System.currentTimeMillis();
                System.out.println("\nThe time of execution in MilliSeconds: "+ (endTime1 - startTime1)); 
                
                break;
            case "-b":
                String file2 = args[1];
                int start = Integer.parseInt(args[2]);
                int end = Integer.parseInt(args[3]);
                bfs b1 = new bfs();
                System.out.println(file2);
                b1.generatepath(start, end, file2);
                break;
            case "-m":
                long startTime = System.currentTimeMillis();
                Front f = new Front();
                String temp4 = args[1];
                String temp5 = args[2];
                f.process(temp4, temp5);
                long endTime = System.currentTimeMillis();
                System.out.println("\nThe time of execution in MilliSeconds: "+ (endTime - startTime)); 
                break;

        }
    }

}
