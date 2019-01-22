import java.util.*;
public class FifteenPuzzleAStar {
	private static HashMap<Frame,Integer> levelOfFrame = new HashMap(); //to store cuurentState and level
	private static HashMap<Frame,Frame> previousFrames  = new HashMap(); //to store currentState and its parent
	private static int framevalue;
	private static int a = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Frame frame = null;
		frame = getInputArgs(args); //get input from argument
		checkpreviousFrames(frame, null); 
		
		try{

			long start = System.currentTimeMillis();
			System.out.println("A Star solution with heuristics 1:");
			Solution solution1 = aStar1(frame,start);
			long duration = System.currentTimeMillis() - start;
			if(a==1)
				System.out.println("Solution cannot be found");
			else
				printSolutionAndData(frame, solution1, duration);
		}
		catch(OutOfMemoryError e){
			System.out.println("Memory out of Bound");
		}
		try{

			long start = System.currentTimeMillis();
			System.out.println("A Star solution with heuristics 2:");
			Solution solution2 = aStar2(frame,start);
			long duration = System.currentTimeMillis() - start;
			if(a==1)
				System.out.println("Solution cannot be found");
			else
				printSolutionAndData(frame, solution2, duration);
		}
		catch(OutOfMemoryError e){
			System.out.println("Memory out of Bound");
		}
		
	}
	//calculate the memory
	public static double memoryUsage(){
		Runtime runtime = Runtime.getRuntime();
		return ((runtime.totalMemory() - runtime.freeMemory()) / 1024);
	}
	//parse the input to store in integer format
	public static Frame getInputArgs(String args[]){
		int[][] input = new int[4][4];
		int k = 0;
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				String s = args[k];
				input[i][j] = Integer.parseInt(s);
				k++;
			}
		}
		return new Frame(input);

	}
	//function to print the ouput
	public static void printSolutionAndData(Frame frame, Solution solution, long time){
		
		String moves = "";
		for(Frame.Direction d: solution.solution.getSolutionPath())
			moves += d.toString().substring(0,1);
		
		System.out.println("Moves: "+moves);
		System.out.println("Number of Nodes expanded: "+solution.expandedNodes);
		System.out.println("Time Taken: "+time+"ms");
		System.out.println("Memory Used: "+solution.memory+"kb");
	}
	 //checking for the repeated states
		private static boolean checkpreviousFrames(Frame newFrame, Frame currentFrame) {
			boolean a = false;
        if(!levelOfFrame.containsKey(newFrame)){  
            framevalue = currentFrame==null ? 0 : levelOfFrame.get(currentFrame)+1;   
            levelOfFrame.put(newFrame, framevalue);         
            previousFrames.put(newFrame, currentFrame);    
            a = true;
          }
      return a;
    }
		// A* search using heuristic 1 - ‘number of misplaced tile'
		public static Solution aStar1(Frame frame, long time){
			ArrayList<Frame> fringe = new ArrayList<Frame>();
			fringe.add(frame);
			int expandedNodes = 0;
			while(fringe.size() > 0 && System.currentTimeMillis()-time<=300000000){
				int min = fringe.get(0).getHeuristic1() + fringe.get(0).getSolutionPath().size();
				int minPos = 0;
				for(int i=0; i<fringe.size(); i++){
					if(fringe.get(i).getHeuristic1() + fringe.get(i).getSolutionPath().size() < min){
						min = fringe.get(i).getHeuristic1() + fringe.get(i).getSolutionPath().size();
						minPos = i;
					}
				}

				Frame expand = fringe.remove(minPos);
				if(expand.isSolved()){
					return new Solution(expand, expandedNodes, memoryUsage());
				}
				expandedNodes++;
				for(Frame.Direction d: expand.moveableDirections()){
					fringe.add(expand.move(d));
				}
			}
			if(System.currentTimeMillis()-time>300000000)
			{
				a=1;
			}
			return null; // should never get here
		}

		// A* search using heuristic 2 - 'manhattan distance'
		public static Solution aStar2(Frame frame, long time){
			ArrayList<Frame> fringe = new ArrayList<Frame>();
			fringe.add(frame);
			int expandedNodes = 0;
			while(fringe.size() > 0 && System.currentTimeMillis()-time<=300000000){
				int min = fringe.get(0).getHeuristic2() + fringe.get(0).getSolutionPath().size();
				int minPos = 0;
				for(int i=0; i<fringe.size(); i++){
					if(fringe.get(i).getHeuristic2() + fringe.get(i).getSolutionPath().size() < min){
						min = fringe.get(i).getHeuristic2() + fringe.get(i).getSolutionPath().size();
						minPos = i;
					}
				}

				Frame expand = fringe.remove(minPos);
				if(expand.isSolved()){
					return new Solution(expand, expandedNodes, memoryUsage());
				}
				expandedNodes++;
				for(Frame.Direction d: expand.moveableDirections()){
					fringe.add(expand.move(d));
				}
			}
			if(System.currentTimeMillis()-time>300000000)
			{
				a=1;
			}
			return null; // should never get here
		}
	//Defining the solution state	
	private static class Solution{
		public int expandedNodes;
		public Frame solution;
		public double memory;
		public Solution(Frame solution, int expandedNodes, double memory){
			this.expandedNodes = expandedNodes;
			this.solution = solution;
			this.memory = memory;
		}
	}
}
