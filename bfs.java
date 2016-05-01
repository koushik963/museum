package front;

import java.io.*;
import java.util.*;

class Node_BFS {    
	public Integer val;
	public Integer nod_name;

	public Node_BFS(Integer node_t)
        {
		this.nod_name = node_t;
	}
//in order to store the edges we have used the below hash map
	Map<Integer, Integer> map = new HashMap<Integer, Integer>();  
	public String color;
	public Integer leng;
	public Integer parent;
}

public class bfs{  //To calculate the shortest path in the graph
        //"pat" gives the path to the text file where the adjacency list graph is found.
    //st is the source node_t
    //fin is the final node_t
        public static void generatepath(int st,int fin,String pat) throws IOException{
        	long startTime = System.currentTimeMillis();// to calculate the time taken for the execution
		FileInputStream f = null;
		ArrayList<Node_BFS> nodearray = new ArrayList<Node_BFS>(); 
	        int start_node = st;
                int final_node = fin;
		try {
			String inputfile = pat; 
			f = new FileInputStream(inputfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(f));

			List<ArrayList<String>> list = new ArrayList<ArrayList<String>>(); 
                        String[] line_split;
                        String line,temp=" ";
			ArrayList<String> line_node;
			while ((line = br.readLine()) != null) 
                        { 			
				line_node = new ArrayList<String>();
				line_split = line.split("\\s");
                                for(int g=0;g<line_split.length;g++)
                                {
                                    String t1=line_split[g];
                                    line_node.add(t1);
                                }
				list.add(line_node); // storing all the lines data in ArrayList
			}
			int index = 0;
			for (List<String> s3 : list) { 
                                Node_BFS graph_node = new Node_BFS(index);
				index++;
                         	for (int i = 0; i < s3.size(); i++) {
					String key1 = s3.get(i); 
					i++;
					String value1 = s3.get(i);
					graph_node.map.put(Integer.parseInt(key1), Integer.parseInt(value1));								// connected to particular node_t
				}
				nodearray.add(graph_node);
			}

			Object[] nodes = nodearray.toArray();
                        if(final_node>nodes.length)
                        {
                            System.out.println("Path doesn't exists");
                            System.out.println("Final node:" + final_node+" not found");
                            System.exit(0);
                        }
			BFS(nodes, start_node, final_node);
			System.out.println("Shortest path from source to sink");
			System.out.print("(Final Node) " + final_node);
			getpath(nodes, start_node, final_node);
			long endTime = System.currentTimeMillis();
			System.out.println("\nThe time of execution in MilliSeconds: "
					+ (endTime - startTime));
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
        }
	public static void BFS(Object[] graph, int start_node, int final_node) { 
		// finds the shortest path
		for (int i = start_node; i < graph.length; i++) {
			Node_BFS node_t = (Node_BFS) graph[i];

			if (i == start_node) {
				node_t.color = "gray";
				node_t.leng = 0;
				node_t.parent = null;
			} else {
				node_t.color = "white";
				node_t.leng = 0;
				node_t.parent = null;
			}
		}

		List<Node_BFS> Q = new ArrayList<Node_BFS>();
		Q.add((Node_BFS) graph[start_node]);
		while (!Q.isEmpty()) 
                {
			Node_BFS node_t = Q.remove(0);
			for (Map.Entry<Integer, Integer> e : node_t.map.entrySet()) 
                        {
				Node_BFS neighbor_t = (Node_BFS) graph[e.getKey()];
				if (neighbor_t.color.equalsIgnoreCase("white")) {
					neighbor_t.color = "gray";
					neighbor_t.leng += 1;
					neighbor_t.parent = node_t.nod_name;
					Q.add(neighbor_t);
				}
			}
			node_t.color = "black";
		}
	}
	
	public static void getpath(Object[] graph, Integer start_node,Integer final_node)
	// prints the shortest path
	{
              Node_BFS sink = (Node_BFS) graph[final_node];
		Integer parent = sink.parent;
		if (parent != null) {
			if (parent != start_node) {
				System.out.print(" -----> " + parent);
				getpath(graph, start_node, parent);
			} else {
				System.out.println(" -----> " + start_node + " {Source Node}");
			}
		} else {
			System.out.println(" -> (source not found)" + " ----> " + " path doesn't exists for the input");
		}
	}
}