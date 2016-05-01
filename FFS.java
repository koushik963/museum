package front;

import java.io.*;
import java.util.*;

class Node_FFS {
	public Integer val;
	public Integer nodename;

	public Node_FFS(Integer node) {
		this.nodename = node;
	}

	Map<Integer, Integer> map_ffs = new HashMap<Integer, Integer>();
	public String color;
	public Integer length;
	public Integer parent;
}

public class FFS {
	static ArrayList<Integer> objList = new ArrayList<Integer>();
	static int pathflow = Integer.MAX_VALUE;
	public static int flow = 0;
	static int start_node = 0;
	static int val = 0;
        public static int ffs_eval(String str1) throws IOException
        {
                int max_flow=-1;
            	long startTime = System.currentTimeMillis();
		FileInputStream f = null;
		ArrayList<Node_FFS> nodearray = new ArrayList<Node_FFS>();
		// Reading the input file line by line and differentiating as keys and
		// values in every line
		try {
			String inputfile = str1;
			f = new FileInputStream(inputfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(f));
			List<List<String>> list = new ArrayList<List<String>>();
			String[] line_split;
			List<String> line_node;
			String line;
			while ((line = br.readLine()) != null) {
				String str = line; 
				line_node = new ArrayList<String>();
				line_split = str.split("\\s");
				
                                 for(int g=0;g<line_split.length;g++)
                                {
                                    String t1=line_split[g];
                                    line_node.add(t1);
                                }
				list.add(line_node);
			}
			int index = 0;
			for (List<String> s3 : list) {
				Node_FFS n3 = new Node_FFS(index);
				index++;
				for (int i = 0; i < s3.size(); i++) {
					String key1 = s3.get(i);
					i++;
					String value1 = s3.get(i);
					n3.map_ffs.put(Integer.parseInt(key1), Integer.parseInt(value1));
				}
				nodearray.add(n3);
			}
			Object[] graph = nodearray.toArray(); // graph as an array
			max_flow=BFS(graph);
                    
			long endTime = System.currentTimeMillis();
			
                        return max_flow;
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
            return max_flow;
        }
	
	public static int BFS(Object[] graph) {
		// Gives the shortest path
		Node_FFS length = (Node_FFS) graph[graph.length - 1];
		for (int i = 0; i < graph.length; i++) {
			Node_FFS node = (Node_FFS) graph[i];
			if (i == 0) {
				node.color = "gray";
				node.length = 0;
				node.parent = null;
			} else {
				node.color = "white";
				node.length = 0;
				node.parent = null;
			}
		}
		List<Node_FFS> Q = new ArrayList<Node_FFS>();
		Q.add((Node_FFS) graph[0]);
		while (!Q.isEmpty()) {
			Node_FFS node = Q.remove(0);
			for (Map.Entry<Integer, Integer> e : node.map_ffs.entrySet()) {
				Node_FFS neighbor_t = (Node_FFS) graph[e.getKey()];
				if (neighbor_t.color.equalsIgnoreCase("white")) {
					neighbor_t.color = "gray";
					neighbor_t.length += 1;
					neighbor_t.parent = node.nodename;
					Q.add(neighbor_t);
				}
			}
			node.color = "black";
		}
		if (length.color.equalsIgnoreCase("black"))
                {
			pathflow = Integer.MAX_VALUE;
			getpath(graph, start_node, graph.length - 1);
		} 
                else
                {
                        return flow;
		}
                return 0;
	}

	public static int maxflow() {
	    return flow;
	}

	public static void getpath(Object[] graph, Integer start_node,
			Integer final_node) {
		// Prints the shortest path and finds the min capacity on the edges of a
		// shortest path found
            if(final_node>graph.length)
                        {
                            System.out.println("Final node:" + final_node+" not found");
                            System.exit(0);
                        }
		Node_FFS sink = (Node_FFS) graph[final_node];
		sink.color = "Red";
		Integer parent = sink.parent;
		if (parent != null) {
			Node_FFS lastnode = (Node_FFS) graph[parent];
			Integer val = lastnode.map_ffs.get(final_node);
			pathflow = Math.min(pathflow, val);
			getpath(graph, start_node, parent);
		} else {
			flow = flow + pathflow;
			updategraph(graph, graph.length - 1);
		}
	}

	private static void updategraph(Object[] graph, Integer last_vert) {
		
		Node_FFS sink = (Node_FFS) graph[last_vert];
		Integer parent = sink.parent;
		if (parent != null) {
			Node_FFS parent_1 = (Node_FFS) graph[parent];
			if (sink.color == "Red" && parent_1.color == "Red") {
				Integer val1 = parent_1.map_ffs.get(last_vert);
				if (val1 != null) 
                                {
					int value1 = val1 - pathflow;
					parent_1.map_ffs.put(last_vert, value1);
				}
				Integer returnflow = sink.map_ffs.get(parent);
				if (returnflow != null) {
					int rev = -pathflow;
					int val2 = returnflow - (rev);
					sink.map_ffs.put(parent, val2);
				} else {
					sink.map_ffs.put(parent, pathflow);
				}
				updategraph(graph, parent);
			}
		} else {
			removeedge(graph, graph.length - 1);
		}
	}

	private static void removeedge(Object[] graph, Integer last_vert) {
		
		Node_FFS sink = (Node_FFS) graph[last_vert];
		Integer parent = sink.parent;
		if (parent != null) {
			Node_FFS parent_1 = (Node_FFS) graph[parent];
			if (sink.color == "Red" && parent_1.color == "Red") {
				Integer val = parent_1.map_ffs.get(last_vert);
				if (val == 0) {
					parent_1.map_ffs.remove(last_vert);
				}
				Integer rev_val = sink.map_ffs.get(parent);
				if (rev_val == 0) {
					sink.map_ffs.remove(parent);
				}
				removeedge(graph, parent);
			}
		} else {
			BFS(graph);
		}
	}
}