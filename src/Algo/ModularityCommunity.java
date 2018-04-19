package Algo;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.measure.Modularity;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.Viewer;

import java.io.IOException;

public class ModularityCommunity extends GraphTreatment {

    protected static String styleSheet =                // 4
            "node { size: 7px; fill-color: rgb(150,150,150); }" +
                    "edge { fill-color: rgb(255,50,50); size: 2px; }" +
                    "edge.cut { fill-color: rgba(200,200,200,128); }" +
                    "sprite#CC { size: 0px; text-color: rgb(150,100,100); text-size: 20; }";
    private Viewer viewer;
    private LinLog layout;
    private double a = 0;
    private double r = -1.3;
    private double force = 3;
    private ProxyPipe fromViewer;
    private Modularity modularity;
    private ConnectedComponents cc;    // 1
    private SpriteManager sm;
    private Sprite ccCount, modValue;
    private double cutThreshold = 1;

    public ModularityCommunity(String title) {

        this.graph = new SingleGraph(title);
    }


    public void findCommunities(String fileName) throws IOException, GraphParseException {


        viewer = graph.display();
        fromViewer = viewer.newThreadProxyOnGraphicGraph();
        layout = new LinLog(false);
        cc = new ConnectedComponents(graph);            // 2
        sm = new SpriteManager(graph);
        ccCount = sm.addSprite("CC");

        modularity = new Modularity("module");
        modValue = sm.addSprite("M");

        modularity.init(graph);

        layout.configure(a, r, true, force);
        cc.setCutAttribute("cut");                // 3
        ccCount.setPosition(StyleConstants.Units.PX, 20, 20, 0);
        cc.setCountAttribute("module");

        modValue.setPosition(StyleConstants.Units.PX, 20, 40, 0);    // 2

        layout.addSink(graph);
        graph.addSink(layout);
        fromViewer.addSink(graph);
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.read(fileName);
        while (!graph.hasAttribute("ui.viewClosed")) {
            fromViewer.pump();
            layout.compute();
            showCommunities();
            ccCount.setAttribute("ui.label",        // 3
                    String.format("Modules %d", cc.getConnectedComponentsCount()));
            modValue.setAttribute("ui.label",    // 3
                    String.format("Modularity %f", modularity.getMeasure()));

        }
    }


    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void findCommunities() {
        viewer = graph.display(false);
        fromViewer = viewer.newThreadProxyOnGraphicGraph();
        layout = new LinLog(false);

        cc = new ConnectedComponents(graph);            // 2
        sm = new SpriteManager(graph);
        ccCount = sm.addSprite("CC");


        modularity = new Modularity("module");
        modValue = sm.addSprite("M");

        modularity.init(graph);

        layout.configure(a, r, true, force);
        cc.setCutAttribute("cut");                // 3
        ccCount.setPosition(StyleConstants.Units.PX, 20, 20, 0);
        cc.setCountAttribute("module");

        modValue.setPosition(StyleConstants.Units.PX, 20, 40, 0);    // 2

        layout.addSink(graph);
        graph.addSink(layout);
        fromViewer.addSink(graph);
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", styleSheet);
        //communityDetection();
    }

    private void communityDetection() {
        while (!graph.hasAttribute("ui.viewClosed")) {
            fromViewer.pump();
            layout.compute();
            showCommunities();
            ccCount.setAttribute("ui.label",        // 3
                    String.format("Modules %d", cc.getConnectedComponentsCount()));
            modValue.setAttribute("ui.label",    // 3
                    String.format("Modularity %f", modularity.getMeasure()));

        }
    }

    public void showCommunities() {
        int nEdges = graph.getEdgeCount();
        double averageDist = 0;
        double edgesDist[] = new double[nEdges];

        for (int i = 0; i < nEdges; i++) {                    // 1
            Edge edge = graph.getEdge(i);
            edgesDist[i] = GraphPosLengthUtils.edgeLength(edge);    // 2
            averageDist += edgesDist[i];                // 3
        }

        averageDist /= nEdges;                        // 3

        for (int i = 0; i < nEdges; i++) {                    // 1
            Edge edge = graph.getEdge(i);

            if (edgesDist[i] > averageDist * cutThreshold) {        // 2
                edge.addAttribute("ui.class", "cut");        // 2a
                edge.addAttribute("cut");
            } else {
                edge.removeAttribute("ui.class");        // 2b
                edge.removeAttribute("cut");
            }
        }
    }


}
