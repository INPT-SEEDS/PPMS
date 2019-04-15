package FrontEnd.Graph;

import BackEnd.Portfolio.Portfolio;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.LocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.filter.FisheyeTreeFilter;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.*;
import prefuse.data.io.TreeMLReader;
import prefuse.data.search.PrefixSearchTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.ui.JFastLabel;
import prefuse.util.ui.JSearchPanel;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.TreeDepthItemSorter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Graph
  extends Display
{
  private static final String tree = "tree";
  private static final String treeNodes = "tree.nodes";
  private static final String treeEdges = "tree.edges";
  private LabelRenderer m_nodeRenderer;
  private EdgeRenderer m_edgeRenderer;
  private String m_label;
  private int m_orientation = 0;
  
  public Graph(prefuse.data.Tree t, String label)
  {
    super(new Visualization());
    m_label = label;
    
    m_vis.add("tree", t);
    
    m_nodeRenderer = new LabelRenderer(m_label);
    m_nodeRenderer.setRenderType(2);
    m_nodeRenderer.setHorizontalAlignment(0);
    m_nodeRenderer.setRoundedCorner(8, 8);
    m_edgeRenderer = new EdgeRenderer(1);
    
    DefaultRendererFactory rf = new DefaultRendererFactory(m_nodeRenderer);
    rf.add(new InGroupPredicate("tree.edges"), m_edgeRenderer);
    m_vis.setRendererFactory(rf);
    

    ItemAction nodeColor = new NodeColorAction("tree.nodes");
    ItemAction textColor = new ColorAction("tree.nodes", 
      VisualItem.TEXTCOLOR, ColorLib.rgb(0, 0, 0));
    m_vis.putAction("textColor", textColor);
    
    ItemAction edgeColor = new ColorAction("tree.edges", 
      VisualItem.STROKECOLOR, ColorLib.rgb(200, 200, 200));
    

    ActionList repaint = new ActionList();
    repaint.add(nodeColor);
    repaint.add(new RepaintAction());
    m_vis.putAction("repaint", repaint);
    

    ActionList fullPaint = new ActionList();
    fullPaint.add(nodeColor);
    m_vis.putAction("fullPaint", fullPaint);
    

    ActionList animatePaint = new ActionList(400L);
    animatePaint.add(new ColorAnimator("tree.nodes"));
    animatePaint.add(new RepaintAction());
    m_vis.putAction("animatePaint", animatePaint);
    

    NodeLinkTreeLayout treeLayout = new NodeLinkTreeLayout("tree", 
      m_orientation, 50.0D, 0.0D, 8.0D);
    treeLayout.setLayoutAnchor(new Point2D.Double(25.0D, 300.0D));
    m_vis.putAction("treeLayout", treeLayout);
    
    CollapsedSubtreeLayout subLayout = 
      new CollapsedSubtreeLayout("tree", m_orientation);
    m_vis.putAction("subLayout", subLayout);
    
    AutoPanAction autoPan = new AutoPanAction();
    

    ActionList filter = new ActionList();
    filter.add(new FisheyeTreeFilter("tree", 2));
    filter.add(new FontAction("tree.nodes", FontLib.getFont("Tahoma", 20.0D)));
    filter.add(treeLayout);
    filter.add(subLayout);
    filter.add(textColor);
    filter.add(nodeColor);
    filter.add(edgeColor);
    m_vis.putAction("filter", filter);
    

    ActionList animate = new ActionList(1000L);
    animate.setPacingFunction(new SlowInSlowOutPacer());
    animate.add(autoPan);
    animate.add(new QualityControlAnimator());
    animate.add(new VisibilityAnimator("tree"));
    animate.add(new LocationAnimator("tree.nodes"));
    animate.add(new ColorAnimator("tree.nodes"));
    animate.add(new RepaintAction());
    m_vis.putAction("animate", animate);
    m_vis.alwaysRunAfter("filter", "animate");
    

    ActionList orient = new ActionList(2000L);
    orient.setPacingFunction(new SlowInSlowOutPacer());
    orient.add(autoPan);
    orient.add(new QualityControlAnimator());
    orient.add(new LocationAnimator("tree.nodes"));
    orient.add(new RepaintAction());
    m_vis.putAction("orient", orient);
    



    setSize(1366, 768);
    setItemSorter(new TreeDepthItemSorter());
    addControlListener(new ZoomToFitControl());
    addControlListener(new ZoomControl());
    addControlListener(new WheelZoomControl());
    addControlListener(new PanControl());
    addControlListener(new FocusControl(1, "filter"));
    
    registerKeyboardAction(
      new OrientAction(0), 
      "left-to-right", KeyStroke.getKeyStroke("ctrl 1"), JComponent.WHEN_FOCUSED);
    registerKeyboardAction(
      new OrientAction(2), 
      "top-to-bottom", KeyStroke.getKeyStroke("ctrl 2"), JComponent.WHEN_FOCUSED);
    registerKeyboardAction(
      new OrientAction(1), 
      "right-to-left", KeyStroke.getKeyStroke("ctrl 3"), JComponent.WHEN_FOCUSED);
    registerKeyboardAction(
      new OrientAction(3), 
      "bottom-to-top", KeyStroke.getKeyStroke("ctrl 4"), JComponent.WHEN_FOCUSED);
    



    setOrientation(m_orientation);
    m_vis.run("filter");
    
    TupleSet search = new PrefixSearchTupleSet();
    m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, search);
    search.addTupleSetListener((t1, add, rem) -> {
      m_vis.cancel("animatePaint");
      m_vis.run("fullPaint");
      m_vis.run("animatePaint");
    });
  }

  public void setOrientation(int orientation)
  {
    NodeLinkTreeLayout rtl = 
      (NodeLinkTreeLayout)m_vis.getAction("treeLayout");
    CollapsedSubtreeLayout stl = 
      (CollapsedSubtreeLayout)m_vis.getAction("subLayout");
    switch (orientation) {
    case 0: 
      m_nodeRenderer.setHorizontalAlignment(0);
      m_edgeRenderer.setHorizontalAlignment1(1);
      m_edgeRenderer.setHorizontalAlignment2(0);
      m_edgeRenderer.setVerticalAlignment1(2);
      m_edgeRenderer.setVerticalAlignment2(2);
      break;
    case 1: 
      m_nodeRenderer.setHorizontalAlignment(1);
      m_edgeRenderer.setHorizontalAlignment1(0);
      m_edgeRenderer.setHorizontalAlignment2(1);
      m_edgeRenderer.setVerticalAlignment1(2);
      m_edgeRenderer.setVerticalAlignment2(2);
      break;
    case 2: 
      m_nodeRenderer.setHorizontalAlignment(2);
      m_edgeRenderer.setHorizontalAlignment1(2);
      m_edgeRenderer.setHorizontalAlignment2(2);
      m_edgeRenderer.setVerticalAlignment1(3);
      m_edgeRenderer.setVerticalAlignment2(4);
      break;
    case 3: 
      m_nodeRenderer.setHorizontalAlignment(2);
      m_edgeRenderer.setHorizontalAlignment1(2);
      m_edgeRenderer.setHorizontalAlignment2(2);
      m_edgeRenderer.setVerticalAlignment1(4);
      m_edgeRenderer.setVerticalAlignment2(3);
      break;
    default: 
      throw new IllegalArgumentException(
        "Unrecognized orientation value: " + orientation);
    }
    m_orientation = orientation;
    rtl.setOrientation(orientation);
    stl.setOrientation(orientation);
  }

  public static void CreateGraphWithPortfolio(Portfolio portfolio)
  {
    Tree t = new Tree(portfolio);
    Tree.CreateXML(t, t.getPortfolio().GetProjectsCount());

    String infile = "Branch.xml";
    String label = "name";

    JComponent treeview = demo(infile, label);
    JFrame frame = new JFrame("Branch And Bound View");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setContentPane(treeview);
    frame.pack();
    frame.setSize(1366, 768);
    frame.setVisible(true);
  }

  public static JComponent demo(String datafile, String label)
  {
    Color BACKGROUND = Color.WHITE;
    Color FOREGROUND = Color.BLACK;

    prefuse.data.Tree t = null;
    try
    {
      InputStream inputStream = new FileInputStream(new File("Branch.xml"));
      t=(prefuse.data.Tree) new TreeMLReader().readGraph(inputStream);
  }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }

    Graph tview = new Graph(t, label);
    tview.setBackground(BACKGROUND);
    tview.setForeground(FOREGROUND);

    JSearchPanel search = new JSearchPanel(tview.getVisualization(),
      "tree.nodes", Visualization.SEARCH_ITEMS, label, true, true);
    search.setShowResultCount(true);
    search.setBorder(BorderFactory.createEmptyBorder(5, 5, 4, 0));
    search.setFont(FontLib.getFont("Tahoma", 0, 11));
    search.setBackground(BACKGROUND);
    search.setForeground(FOREGROUND);
    
    final JFastLabel title = new JFastLabel("                 ");
    title.setPreferredSize(new Dimension(350, 20));
    title.setVerticalAlignment(3);
    title.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
    title.setFont(FontLib.getFont("Tahoma", 0, 16));
    title.setBackground(BACKGROUND);
    title.setForeground(FOREGROUND);
    
    tview.addControlListener(new ControlAdapter() {
      public void itemEntered(VisualItem item, MouseEvent e) {
        if (item.canGetString(label))
          title.setText(item.getString(label));
      }
      
      public void itemExited(VisualItem item, MouseEvent e) { title.setText(null);
      }

    });
    Box box = new Box(0);
    box.add(Box.createHorizontalStrut(10));
    box.add(title);
    box.add(Box.createHorizontalGlue());
    box.add(search);
    box.add(Box.createHorizontalStrut(3));
    box.setBackground(BACKGROUND);
    
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(BACKGROUND);
    panel.setForeground(FOREGROUND);
    panel.add(tview, "Center");
    panel.add(box, "South");
    return panel;
  }

  public class OrientAction extends AbstractAction
  {
    private int orientation;
    
    public OrientAction(int orientation) { this.orientation = orientation; }
    
    public void actionPerformed(ActionEvent evt)
    {
      setOrientation(orientation);
      getVisualization().cancel("orient");
      getVisualization().run("treeLayout");
      getVisualization().run("orient");
    } }
  
  public class AutoPanAction extends Action
  { public AutoPanAction() {}
    
    private Point2D m_start = new Point2D.Double();
    private Point2D m_end = new Point2D.Double();
    private Point2D m_cur = new Point2D.Double();
    private int m_bias = 150;
    
    public void run(double frac) {
      TupleSet ts = m_vis.getFocusGroup(Visualization.FOCUS_ITEMS);
      if (ts.getTupleCount() == 0) {
        return;
      }
      if (frac == 0.0D) {
        int xbias = 0;int ybias = 0;
        switch (m_orientation) {
        case 0: 
          xbias = m_bias;
          break;
        case 1: 
          xbias = -m_bias;
          break;
        case 2: 
          ybias = m_bias;
          break;
        case 3: 
          ybias = -m_bias;
        }
        
        
        VisualItem vi = (VisualItem)ts.tuples().next();
        m_cur.setLocation(getWidth() / 2.0, getHeight() / 2.0);
        getAbsoluteCoordinate(m_cur, m_start);
        m_end.setLocation(vi.getX() + xbias, vi.getY() + ybias);
      } else {
        m_cur.setLocation(m_start.getX() + frac * (m_end.getX() - m_start.getX()), 
          m_start.getY() + frac * (m_end.getY() - m_start.getY()));
        panToAbs(m_cur);
      }
    }
  }
  
  public static class NodeColorAction extends ColorAction
  {
    public NodeColorAction(String group)
    {
      super(group,VisualItem.FILLCOLOR);
    }
    
    public int getColor(VisualItem item) {
      if (m_vis.isInGroup(item, Visualization.SEARCH_ITEMS))
        return ColorLib.rgb(255, 190, 190);
      if (m_vis.isInGroup(item, Visualization.FOCUS_ITEMS))
        return ColorLib.rgb(198, 229, 229);
      if (item.getDOI() > -1.0D) {
        return ColorLib.rgb(164, 193, 193);
      }
      return ColorLib.rgba(255, 255, 255, 0);
    }
  }
}
