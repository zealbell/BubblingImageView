package linkersoft.blackpanther.bubbles;

import android.graphics.Path;

import linkersoft.blackpanther.bubbles.Shape.Polygon;


public class BubblePanther {


    public BubblePanther(int[][]Nodes){
        nodesOnly=true;
        this.Nodes=Nodes;
        this.noOfNodes=Nodes.length;
        joinNodes();
    }
    public BubblePanther(int[][]Nodes, int[][]Controls){
        this.Nodes=Nodes;
        this.Controls=Controls;
        this.noOfNodes=Nodes.length;
        this.noOfControls=Controls.length;
        joinNodeControls();
    }
    private void joinNodes(){
        path=new Path();
        path.moveTo(Nodes[0][X], Nodes[0][Y]);
        for (int nodeIndx = 0; nodeIndx < Nodes.length; nodeIndx++){
            path.lineTo(Nodes[nodeIndx][X], Nodes[nodeIndx][Y]);
            if(nodeIndx==Nodes.length-1) path.lineTo(Nodes[0][X], Nodes[0][Y]);
        }
    }
    private void joinNodeControls(){
        path=new Path();
        int cntrlAIndx=0,cntrlBIndx=1;
        path.moveTo(Nodes[0][X], Nodes[0][Y]);
        for (int nodeIndx = 0; nodeIndx < Nodes.length; nodeIndx++){
            if (nodeIndx != Nodes.length - 1) {
                bezierThrough(
                        Controls[cntrlAIndx][X], Controls[cntrlAIndx][Y],
                        Controls[cntrlBIndx][X], Controls[cntrlBIndx][Y],
                        Nodes[nodeIndx + 1][X], Nodes[nodeIndx + 1][Y]);
                cntrlBIndx+=2;
                cntrlAIndx+=2;
            }else{
                bezierThrough(
                        Controls[cntrlAIndx][X], Controls[cntrlAIndx][Y],
                        Controls[cntrlBIndx][X], Controls[cntrlBIndx][Y],
                        Nodes[0][X], Nodes[0][Y]);
            }
        }path.close();
    }
    public static BubblePanther getFractionPanther(float fraction, BubblePanther In, BubblePanther Fin, boolean onlyNodes){
        int[][] Nodes,Controls;
        if(onlyNodes){
            Nodes=new int[In.Nodes.length][2];
            for (int i = 0; i < In.noOfNodes; i++) {
                Nodes[i][0] = Math.round((1 - fraction) * In.Nodes[i][0] + fraction * Fin.Nodes[i][0]);
                Nodes[i][1] = Math.round((1 - fraction) * In.Nodes[i][1] + fraction * Fin.Nodes[i][1]);
            }return new BubblePanther(Nodes);
        }else{
            Nodes=new int[Fin.Nodes.length][2];
            Controls=new int[Fin.Controls.length][2];
            for (int i = 0; i < Fin.noOfNodes; i++) {
                Nodes[i][0] = Math.round((1 - fraction) * In.Nodes[i][0] + fraction * Fin.Nodes[i][0]);
                Nodes[i][1] = Math.round((1 - fraction) * In.Nodes[i][1] + fraction * Fin.Nodes[i][1]);
            }for (int i = 0; i < Fin.noOfControls; i++) {
                Controls[i][0] = Math.round((1 - fraction) * In.Controls[i][0] + fraction * Fin.Controls[i][0]);
                Controls[i][1] = Math.round((1 - fraction) * In.Controls[i][1] + fraction * Fin.Controls[i][1]);
            }return new BubblePanther(Nodes,Controls);
        }
    }
    public Polygon getNodePolygon(){
        if(NodeCage!=null)return NodeCage;
        int xXx[]=new int[Nodes.length],yYy[]=new int[Nodes.length];
        for (int i = 0; i < Nodes.length; i++) {
            xXx[i]=Nodes[i][X];
            yYy[i]=Nodes[i][Y];
        }NodeCage=new Polygon(xXx,yYy);
         return NodeCage;
    }
    public Polygon getControlPolygon(){
        if(nodesOnly)return null;
        if(ControlCage!=null)return ControlCage;
        int xXx[]=new int[Controls.length],yYy[]=new int[Controls.length];
        for (int i = 0; i < Controls.length; i++) {
            xXx[i]=Controls[i][X];
            yYy[i]=Controls[i][Y];
        }ControlCage=new Polygon(xXx,yYy);
         return ControlCage;
    }
    private void bezierThrough(int cntrl_Ax, int cntrl_Ay, int cntr1_Bx, int cntr1_By, int node_Bx, int node_By){
        path.cubicTo( cntrl_Ax,cntrl_Ay,  cntr1_Bx,cntr1_By,  node_Bx,node_By);
    }


    @Override
    public String toString() {
        Polygon NodePoly=getNodePolygon();
        Polygon ControlPoly=getControlPolygon();
        String panther=" ([ ";for (int i = 0; i < NodePoly.size(); i++) panther+="{"+NodePoly.xXx[i]+","+NodePoly.yYy[i]+"}";panther+=" ])~";
        panther+="|~([ ";for (int i = 0; i < ControlPoly.size(); i++) panther+="{"+ControlPoly.xXx[i]+","+ControlPoly.yYy[i]+"}";panther+=" ])";
        return "@LiNKeR(>_<)~"+super.toString()+panther;
    }

    public Path path;
    private static final int X=0,Y=1;
    private boolean nodesOnly;
    public int noOfNodes,noOfControls;
    public int[][] Nodes;
    public int[][] Controls;
    private Polygon NodeCage;
    private Polygon ControlCage;
}



