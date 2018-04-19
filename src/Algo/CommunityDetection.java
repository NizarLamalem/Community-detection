package Algo;

import TwitterData.SwissArmyKnife;
import org.graphstream.algorithm.community.Community;
import org.graphstream.algorithm.community.Leung;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.view.Viewer;

public class CommunityDetection extends GraphTreatment {
    protected static String styleSheet = "node {fill-mode: dyn-plain; fill-color:black ,magenta, cyan,green,yellow,Gray ,blue, orange , brown, red;}" +
            "edge {  fill-color: #222; arrow-size: 3px, 2px;}";
    private Leung leung;
    private Viewer viewer;
    private ProxyPipe fromViewer;


    /* Layout LinLog
    private double a = 0;
    private double r = -1.2;
    private double force = 8;
    private LinLog layout;
    Layout LinLog*/


    public CommunityDetection(String title, String marker) {
        this.graph = new SingleGraph(title);
        this.leung = new Leung(graph, marker, 0.8, 0.05);

    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        this.leung.init(graph);
    }

    public void findCommunities() {
        this.viewer = graph.display(true);
        /*LinLog Layout
        layout = new LinLog(false);
        layout.configure(a, r, true, force);
        layout.addSink(graph);
        graph.addSink(layout);
        LingLog Layout*/
        this.fromViewer = viewer.newThreadProxyOnGraphicGraph();
        this.fromViewer.addSink(graph);
        this.graph.addSink(leung);
        this.graph.addAttribute("ui.quality");
        this.graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", styleSheet);
    }


    public void communityDetection() {
        System.err.println("Detect Communities");

        this.leung.compute();
        for (Node node : graph.getEachNode()) {
            Community community = node.getAttribute(leung.getMarker());
            System.out.println("Node Weight  => ".concat(node.toString().concat("  ".concat(Double.toString(node.getAttribute(leung.getMarker().concat(".score")))))));
            System.out.println("Node Community  => ".concat(node.toString().concat("  ".concat(community.toString()))));
            node.setAttribute("ui.color", SwissArmyKnife.normalise(community.id(), 0, 10));

        }

        while (true) {
            //layout.compute();
            this.fromViewer.pump();

        }


    }
}

