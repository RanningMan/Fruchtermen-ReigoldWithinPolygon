package window;

import graph.DefaultDirectedGraph;
import graph.DefaultEdge;
import graph.DefaultVertex;
import graph.FRPolyVertex;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import layout.FRLayout;
import layout.FRPolygonLayout;
import visualization.Visualizer;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Windows {
	private final int temprature = 10;
	private final int iterations = 1000000;
	private final String frgraph = "test.txt";
	private final String frpolygraph = "test.txt";

	//here get the input polygon
	//NOTE in this program, the positions of vertices of polygon must be input in clockwise order 
	private final double[][] node = {
			{100,600,700,750,630,50},
			{30,80,40,300,100,350}
	};
	
	private JFrame mFrame = new JFrame();
	private DefaultDirectedGraph<DefaultVertex, DefaultEdge> directed = new DefaultDirectedGraph<DefaultVertex, DefaultEdge>();
	private DefaultDirectedGraph<FRPolyVertex, DefaultEdge> directedPoly; //= new DefaultDirectedGraph<FRPolyVertex, DefaultEdge>();
	private JTextArea outputText = new javax.swing.JTextArea();
	private JPanel documentPanel = new JPanel();
	private JTabbedPane consoleTab = new JTabbedPane(JTabbedPane.TOP);
	private JTabbedPane graphTab = new JTabbedPane(JTabbedPane.TOP);	
	private JPanel graphPanel = new JPanel();	
	private JPanel consolePanel = new JPanel();
	private JButton runButton = new JButton("run");
	private JButton randomButton = new JButton("Random Graph");
	private JScrollPane consoleScrollPane = new JScrollPane();	
	private JTextField textFieldVertex;
	private JTextField textFieldEdge;	
	private final JButton runPolygonButton = new JButton("run polygon");
	
	public static void main(String[] args) {
		new Windows();
	}
	
	public Windows(){
				
		GroupLayout groupLayout = new GroupLayout(mFrame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(documentPanel, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(consolePanel, GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
						.addComponent(graphPanel, GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(graphPanel, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(consolePanel, GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
				.addComponent(documentPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
		);
		
		outputText.setEditable(false);
        outputText.setColumns(20);
        outputText.setRows(5);
        consoleScrollPane.setViewportView(outputText);
        consoleTab.addTab("Output", consoleScrollPane);
        
		GroupLayout gl_consolePanel = new GroupLayout(consolePanel);
		gl_consolePanel.setHorizontalGroup(
			gl_consolePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_consolePanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(consoleTab, GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_consolePanel.setVerticalGroup(
			gl_consolePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_consolePanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(consoleTab, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
					.addContainerGap())
		);
		consolePanel.setLayout(gl_consolePanel);		
		
		GroupLayout gl_graphPanel = new GroupLayout(graphPanel);
		gl_graphPanel.setHorizontalGroup(
			gl_graphPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_graphPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(graphTab, GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_graphPanel.setVerticalGroup(
			gl_graphPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_graphPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(graphTab, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
					.addContainerGap())
		);
		graphPanel.setLayout(gl_graphPanel);
		
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadGraph(frgraph, directed);
				if (directed != null){
		        	addFRTab();
				}
		        else
		            JOptionPane.showMessageDialog(new JFrame(), "Please load a graph in order to render.", "Oops!", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		documentPanel.add(runButton);
		runPolygonButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				directedPoly = new DefaultDirectedGraph<FRPolyVertex, DefaultEdge>();
				loadGraphPoly(frpolygraph, directedPoly);
				if (directedPoly != null){
		        	addFRPolygonTab();
				}
		        else
		            JOptionPane.showMessageDialog(new JFrame(), "Please load a graph in order to render.", "Oops!", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		documentPanel.add(runPolygonButton);
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screen = toolkit.getScreenSize();
		
		mFrame.getContentPane().setLayout(groupLayout);
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.setSize(screen.width, screen.height - 30);
		mFrame.setVisible(true);
	}
	
	private void addFRTab(){
        FRLayout fr = new FRLayout(directed, graphTab.getWidth(), graphTab.getHeight(), new Random(123), temprature);
        Visualizer visualPane = new Visualizer(fr, outputText, iterations);
        graphTab.addTab(null, visualPane);
        graphTab.setSelectedComponent(visualPane);       
    }
	
	private void addFRPolygonTab(){		
		FRPolygonLayout fr = new FRPolygonLayout(directedPoly, node[0], node[1], new Random(123), temprature);
        Visualizer visualPane = new Visualizer(fr, outputText, iterations, node[0], node[1]);
        graphTab.addTab(null, visualPane);
        graphTab.setSelectedComponent(visualPane); 
	}
	
	 private void loadGraph(String filepath, DefaultDirectedGraph<DefaultVertex, DefaultEdge> directed) {
	        try {
	            BufferedReader reader = new BufferedReader(new FileReader(filepath));
	            outputText.append("Reading file: " + filepath + "\n");
	            directed.setName(reader.readLine());
	            reader.readLine(); // skip line which has the number of vertices
	            
	            String s;
	            while ((s = reader.readLine()) != null) {
	                String[] tokens;
	                tokens = s.split(":");
	                DefaultVertex source = new DefaultVertex(tokens[0]);
	                if (!directed.addVertex(source)) {
	                    source = directed.getVertex(source);
	                }
	                tokens = tokens[1].trim().split(" ");
	                if (tokens.length > 1) {  // at least one edge exists
	                    for (int i = 0; i < tokens.length - 1; i++) {
	                        DefaultVertex target = new DefaultVertex(tokens[i]);
	                        if (!directed.addVertex(target)) {
	                            target = directed.getVertex(target);
	                        }
	                        directed.addEdge(new DefaultEdge(source, target, true), source, target);
	                    }
	                }
	            }
	            outputText.append("Graph file loaded. Contains " + directed.getNumVertices() + " vertices, " + directed.getNumEdges() + " edges.\n");
	            runButton.setEnabled(true);
	        }
	        catch (IOException e) {
	            outputText.append("Problem reading file: " + filepath + "\n");
	        }
	    }
	 
	 private void loadGraphPoly(String filepath, DefaultDirectedGraph<FRPolyVertex, DefaultEdge> directed) {
	        try {
	            BufferedReader reader = new BufferedReader(new FileReader(filepath));
	            outputText.append("Reading file: " + filepath + "\n");
	            directed.setName(reader.readLine());
	            reader.readLine(); // skip line which has the number of vertices
	            
	            String s;
	            while ((s = reader.readLine()) != null) {
	                String[] tokens;
	                tokens = s.split(":");
	                FRPolyVertex source = new FRPolyVertex(tokens[0]);
	                if (!directed.addVertex(source)) {
	                    source = directed.getVertex(source);
	                }
	                tokens = tokens[1].trim().split(" ");
	                if (tokens.length > 1) {  // at least one edge exists
	                    for (int i = 0; i < tokens.length - 1; i++) {
	                    	FRPolyVertex target = new FRPolyVertex(tokens[i]);
	                        if (!directed.addVertex(target)) {
	                            target = directed.getVertex(target);
	                        }
	                        directed.addEdge(new DefaultEdge(source, target, true), source, target);
	                    }
	                }
	            }
	            outputText.append("Graph file loaded. Contains " + directed.getNumVertices() + " vertices, " + directed.getNumEdges() + " edges.\n");
	            runButton.setEnabled(true);
	        }
	        catch (IOException e) {
	            outputText.append("Problem reading file: " + filepath + "\n");
	        }
	    }	 
}
