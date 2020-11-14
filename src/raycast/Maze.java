/*  
 *  Title of project
 * 
 *  Date of completion
 * 
 *  This program was created under the collaboration of Nathan Grimsey, Eric Lumpkin, Dylan Gibbons-Churchward, and Matthew McGuinn
 *  for Martin Hock's CS143 class in the Fall quarter of 2020.
 * 
 *  This code may be found at https://github.com/CS143-Raycasting-Project/Raycast along with documentation.
 */

package raycast;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Maze {
    int x, y;
	private Point[] walls; // this will be the array of points representing walls 
	public int[][] framing;
	
	
	/**
	 * Creates a random maze with input dimensions(assumes shape is square for now)
	 * @param x - x dimension of the maze
	 * @param y - y dimension of the maze
	 */
	public Maze(int x, int y){
		walls = new Point[x*y];
		framing = new int[x][y];
		mazePrimer(framing);
		workHorse(x/2, y/2, 0);
		workHorse(x/2, y/2, 0);
		workHorse(x/2 - 10, y/2 - 10, 0);
		workHorse(x/2 - 10, y/2 - 10, 0);
		mazeDeprimer(framing);
	}
	
	/**
	 * Starts with a point in the middle of the map and grows randomly (with certain constraints). 
	 * Parameters such as the starting point, how many times you run workHorse, 
	 * and variables in the wellnessCheck and pickANewPointer methods can be changed
	 * to change the nature of the map produced. 
	 * @param x
	 * @param y
	 * @param count
	 */
	private void workHorse(int x, int y, int count) {
		framing[x][y] = 0;
		Point pointHolder = pickANewPointer(x, y);
		if(count > 1000) {
			return;
		}
		workHorse(pointHolder.x, pointHolder.y, count + 1);
	}

	/**
	 * Sets the "outer rim" of the array of ints to 0 so the maze doesn't run off the side
	 * @param framing
	 */
	private void mazePrimer(int[][] framing) {
		for(int i = 0; i < framing.length; i++) {
			for(int j = 0; j < framing.length; j++) {
				framing[i][j] = 1;
			}
		}
		for(int i = 0; i < framing.length; i++) {
			for(int j = 0; j < framing.length; j++) {
				if((i == 0 && j < framing.length) ||
				(i < framing.length && j == 0) ||
				(i == framing.length - 1 && j < framing.length) ||
				(i < framing.length && j == framing.length - 1)) {
					framing[i][j] = 0;
				} 
			}
		}
	}
	/**
	 * sets the rim back to 1s, representing walls
	 * @param framing
	 */
	public void mazeDeprimer(int[][] framing) {
		for(int i = 0; i < framing.length; i++) {
			for(int j = 0; j < framing.length; j++) {
				if((i == 0 && j < framing.length) ||
				(i < framing.length && j == 0) ||
				(i == framing.length - 1 && j < framing.length) ||
				(i < framing.length && j == framing.length - 1)) {
					framing[i][j] = 1;
				} 
			}
		}
	}
	
	/**
	 * 
	 * @param x coordinate in framing int[][] matrix
	 * @param y coordinate in framing int[][] matrix
	 * @return returns true if this coordinate has less than 4 living neighbors(adjacent coordinates turned to 1). 
	 * Otherwise, returns false meaning this proposed coordinate is not available.  
	 * 		   
	 */
	private boolean wellnessCheck(int x, int y) {
		Random rand = new Random();
		int rando = rand.nextInt(5);
		int living = 0;
		if(framing[x-1][y] == 0) { // left
			living++;
			if(living == rando) return false;
		}

		if(framing[x][y+1] == 0) { // lower
			living++;
			if(living == rando) return false;
		}

		if(framing[x+1][y] == 0) { // right
			living++;
			if(living == rando) return false;
		}

		if(framing[x][y-1] == 0) { // upper
			living++;
			if(living == rando) return false;
		}
		return true;
	}
	
	/**
	 * Move the pointer to random and viable coordinate adjacent to the previous pointer
	 * @param x coordinate you are moving on from
	 * @param y coordinate you are moving on from
	 * @return a new pointer (point type)
	 */
	private Point pickANewPointer(int x, int y) {
		Random rand = new Random();
		Set<Point> viablePointers = new HashSet<>();
		int viablePointerCount = 0;
		Point p = new Point(x, y);
		
		if(x-1 > 0) {
			if(wellnessCheck(x-1, y)) {
				viablePointerCount++;
				Point p2 = new Point(x-1, y);
				viablePointers.add(p2);
			}
		}
		if(y+1 < framing.length - 1) {
			if(wellnessCheck(x, y+1)) {
				viablePointerCount++;
				Point p4 = new Point(x, y+1);
				viablePointers.add(p4);
			}
		}
		if(x+1 < framing.length - 1) {
			if(wellnessCheck(x+1, y)) {
				viablePointerCount++;
				Point p6 = new Point(x+1, y);
				viablePointers.add(p6);
			}
		}
		if(y-1 > 0) {
			if(wellnessCheck(x, y-1)) {
				viablePointerCount++;
				Point p8 = new Point(x, y-1);
				viablePointers.add(p8);
			}
		}
		if(viablePointerCount > 0);{
			int rando = rand.nextInt(viablePointerCount + 1);
			int loopCount = 0;
			for(Point points : viablePointers) {
				loopCount++;
				if(loopCount == rando) {
					p = points;
				}
			}
		}
		return p;
	}
	
	/**
	 * Prints the array of integers representing the maze
	 */
	public void print() {
		for(int i = 0; i < framing.length; i++) {
			for(int j = 0; j < framing.length; j++) {
				System.out.print(framing[i][j] + " ");
			}
			System.out.println("");
		}
	}
}
