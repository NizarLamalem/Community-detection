package Algo;

import TwitterData.SwissArmyKnife;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;


public abstract class GraphTreatment {
    protected Graph graph;


    public void autoCreate(boolean autoCreate) {
        this.graph.setStrict(!autoCreate);
        this.graph.setAutoCreate(autoCreate);

    }

    //Adding New Edges Between Users
    public void addNewLinkBetweenEdges(String topic, String newEdge, SwissArmyKnife.Users oldEdges) {
        //System.out.println("Adding Link Between ".concat(newEdge.concat(" and ".concat(oldEdges.toString().concat(" about ".concat(topic))))));
        SwissArmyKnife.Users cursor = oldEdges;
        while (cursor != null) {
            System.out.println("Adding Link Between ".concat(newEdge.concat(" and ".concat(cursor.toString().concat(" about ".concat(topic))))));
            Edge edge = doesItAlreadyExist(cursor.toString(), newEdge);
            if (edge != null) {
                edge.setAttribute("ui.label", ((String) edge.getAttribute("ui.label")).concat(";".concat(topic)));
            } else if (!newEdge.equals(cursor.getUserString())) {
                edge = graph.addEdge(topic.concat(" + ".concat(newEdge.concat(" , ".concat(cursor.toString())))), newEdge, cursor.getUserString());
                if (edge != null && !edge.hasAttribute("ui.label")) {
                    edge.addAttribute("ui.label", topic);
                    edge.getSourceNode().addAttribute("ui.label", newEdge);
                    if (!edge.getTargetNode().hasLabel(cursor.toString())) {
                        edge.getTargetNode().addAttribute("ui.label", cursor.toString());
                    }
                }
            }
            cursor = cursor.nextUser;

        }
    }

    public Edge doesItAlreadyExist(String oldedge, String newedge) {
        Node node1 = graph.getNode(oldedge);
        if (node1 != null && node1.hasEdgeBetween(newedge)) {
            return node1.getEdgeBetween(newedge);
        }
        return null;
    }
}
