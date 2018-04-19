package Algo;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.community.Leung;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.view.Viewer;

import java.io.IOException;

public class LinLogLayout extends GraphTreatment {
    protected static String styleSheet = "node { size: 7px; fill-color: rgb(150,150,150); }" +
            "edge { fill-color: rgb(255,50,50); size: 2px; }" +
            "edge.cut { fill-color: rgba(200,200,200,128); }";
    private Viewer viewer;
    private LinLog layout;
    private Leung leung;
    private double a = 0;
    private double r = -1.3;
    private double force = 3;
    private ProxyPipe fromViewer;
    private double cutThreshold = 1;
    private ConnectedComponents cc;

    public LinLogLayout(String title) {

        this.graph = new SingleGraph(title);
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void findCommunities(String fileName)
            throws IOException, GraphParseException {
        /*graph = new SingleGraph("communities");
        viewer = graph.display(false);
        fromViewer = viewer.newThreadProxyOnGraphicGraph();

        layout = new LinLog(false);
        layout.configure(a, r, true, force);
        layout.addSink(graph);
        graph.addSink(layout);
        fromViewer.addSink(graph);
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.read(fileName);
        CommunityDetection();*/
        viewer = graph.display(false);
        fromViewer = viewer.newThreadProxyOnGraphicGraph();
        layout = new LinLog(false);
        this.cc = new ConnectedComponents(graph);
        layout.configure(a, r, true, force);

        cc.setCutAttribute("cut");
        layout.addSink(graph);
        graph.addSink(layout);
        fromViewer.addSink(graph);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", styleSheet);


    }


    public void findCommunities() {
        viewer = graph.display(false);
        fromViewer = viewer.newThreadProxyOnGraphicGraph();
        layout = new LinLog(false);
        viewer.enableAutoLayout();
        layout.configure(a, r, true, force);
        layout.addSink(graph);
        graph.addSink(layout);
        fromViewer.addSink(graph);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", styleSheet);
        //CommunityDetection();
    }

    public void CommunityDetection() {
        System.out.println("Detect Communities");
        while (!graph.hasAttribute("ui.viewClosed")) {
            fromViewer.pump();
            layout.compute();
            showCommunities();
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