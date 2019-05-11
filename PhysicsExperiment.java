/**
 * Physics Experiment Author: Kaven Vohra and Carolyn Yao 
 * Does this compile or finish running within 5 seconds? Yes
 * 
 * Collaborated with David Dataram 
 */
/*
 * TO-DO:
 * Fix consecutive steps - Done
 * Fix current student steps - Done
 * Implement way to check next steps - Done, from current to last (if can)
 */
/**
 * This class implements a greedy scheduler to assign students to steps in a
 * physics experiment. There are three test cases in the main method. Please
 * read through the whole file before attempting to code the solution.
 *
 * You will only be graded on code you add to the scheduleExperiments method. Do
 * not mess with the existing formatting and identation. You don't need to use
 * the helper methods, but if they come in handy setting up a custom test case,
 * feel free to use them.
 */
public class PhysicsExperiment {

	/**
	 * The actual greedy scheduler you will be implementing!
	 * 
	 * @param numStudents The number of students who can participate, m
	 * @param totalSteps  The number of steps in the experiment, n
	 * @param signUpTable An easy lookup tool, signUpTable[x][Y] = student X signed
	 *                    up or did not sign up for step Y. Example:
	 *                    signUpTable[1][3] = 1 if Student 1 signed up for Step 3
	 *                    signUpTable[1][3] = 0 if Student 1 didn't sign up for Step
	 *                    3
	 * @return scheduleTable: a table similar to the signUpTable where
	 *         scheduleTable[X][Y] = 1 means student X is assigned to step Y in an
	 *         optimal schedule
	 */
	public int[][] scheduleExperiments(int numStudents, int totalSteps, int[][] signUpTable) {

		// Value for students
		int studentNum = 0;

		// Index that will hold which student can take the step
		int studentInSchedule = -1;

		// Your scheduleTable is initialized as all 0's so far. Your code will put 1's
		// in the table in the right places based on the return description
		int[][] scheduleTable = new int[numStudents + 1][totalSteps + 1];

		// Each students consecutive step count (index)
		int consecutiveSteps[] = new int[numStudents + 1];

		// Initialize each students consecutive steps to 0
		for (int i = 0; i < consecutiveSteps.length; i++) {
			consecutiveSteps[i] = 0;
		}

		// Keep track of which students have made steps, this array will help us
		// reduce the number of switches since we can prioritize the students that
		// have previously made steps for us
		boolean[] takenSteps = new boolean[numStudents + 1];

		/**
		 * We iterate through all of the steps, at each step we are checking if
		 * the student can take that particular step. The loop makes sure we exhaust
		 * all of the consecutive steps that the the student can take starting from 
		 * the current step (the iteration of the loop that we are up to)
		 */
		for (int curStep = 1; curStep <= totalSteps; curStep++) {

			// If the student can take the step and it can take the next step
			if (studentInSchedule > 0 && consecutiveSteps[studentInSchedule] > 0) {

				// Set the schedule that the student can take that step
				scheduleTable[studentInSchedule][curStep] = 1;

				// Reduce the consecutive count by 1 of that particular student
				consecutiveSteps[studentInSchedule]--;

				// If thats student is out of consecutive steps then reset
				if (consecutiveSteps[studentInSchedule] == 0) {
					
					// We will set all steps back to 0 and start over again
					for (int inARow = 0; inARow < consecutiveSteps.length; inARow++) {
						consecutiveSteps[inARow] = 0;
					}
				}
				// Loop back to take the next step
				continue;

			} // End of if

			// Loop through all of the students (numStudents passed to function)
			for (studentNum = 1; studentNum <= numStudents; studentNum++) {

				//System.out.println(studentNum);
				
				// If this student can take the step based on look up table (signUpTable)
				if (signUpTable[studentNum][curStep] == 1) {

					// For that particular student increase the number of steps they can take
					consecutiveSteps[studentNum]++;

					// Check the rest of the steps for this student
					for (int temp = curStep; temp <= totalSteps; temp++) {
						
						// Can catch an ArrayIndexOutOfBounds since we check a step ahead.
						try {
							// Check if the student can take the step after as well
							if (signUpTable[studentNum][temp + 1] == 1) {
								// If so, increase the number of consecutive steps
								consecutiveSteps[studentNum]++;
								// Loop again to check next step 
								continue;
							} else {
								// Exit the loop since they have no more consecutive steps
								break;
							}

						} catch (ArrayIndexOutOfBoundsException ae) {
							// System.out.println(ae);
						}
					} 
				} 
			}

			/**
			 * These are initialized per step
			 */
			
			// Variable that checks if there is a user who already went
			boolean hasWent = false;
			// Variable to hold current value
			int curStudentInd = -1;
			// Variable to hold index
			int includeStudent = -1;
			
			// Iterate through all of the students
			for (int studentInd = 0; studentInd < consecutiveSteps.length; studentInd++) {
				
				// We set index to be the student's index from the consecutiveSteps array
				int index = consecutiveSteps[studentInd];
				
				// If this student has taken steps and the index of the of this student
				// is greater than the current student we are checking 
				if (takenSteps[studentInd] && index > curStudentInd) {
					// We set it that this student has went 
					hasWent = true;
					// The student we will include is the student at takenSteps[studentInd] (the index)
					includeStudent = studentInd;
					curStudentInd = index;
				}
				
				// If the student hasn't went and the index is greater than this student 
				// we say that we have checked this student
				if (!hasWent && index > curStudentInd) {
					// The student we will include is the student at studentInd (the index)
					includeStudent = studentInd;
					curStudentInd = index;
				}
				//reset the value of our flag
				hasWent = false;
			}

			// We set the student in the schedule to be the one that was included
			studentInSchedule = includeStudent;
			// Set the student that has taken steps to true
			takenSteps[studentInSchedule] = true;
			// Since the student has taken another step we remove a step left in their consecutive steps
			consecutiveSteps[studentInSchedule]--;
			// Set that student to make the step
			scheduleTable[studentInSchedule][curStep] = 1;
			
		}// No more steps to check!
		
		// We return the table of students that will carry out the steps for the experiment
		return scheduleTable;
	}

	/**
	 * Makes the convenient lookup table based on the steps each student says they
	 * can do
	 * 
	 * @param numSteps       the number of steps in the experiment
	 * @param studentSignUps student sign ups ex: {{1, 2, 4}, {3, 5}, {6, 7}}
	 * @return a lookup table so if we want to know if student x can do step y,
	 *         lookupTable[x][y] = 1 if student x can do step y lookupTable[x][y] =
	 *         0 if student x cannot do step y
	 */
	public int[][] makeSignUpLookup(int numSteps, int[][] studentSignUps) {
		int numStudents = studentSignUps.length;
		int[][] lookupTable = new int[numStudents + 1][numSteps + 1];
		for (int student = 1; student <= numStudents; student++) {
			int[] signedUpSteps = studentSignUps[student - 1];
			for (int i = 0; i < signedUpSteps.length; i++) {
				lookupTable[student][signedUpSteps[i]] = 1;
			}
		}
		return lookupTable;
	}

	/**
	 * Prints the optimal schedule by listing which steps each student will do
	 * Example output is Student 1: 1, 3, 4
	 * 
	 * @param schedule The table of 0's and 1's of the optimal schedule, where
	 *                 schedule[x][y] means whether in the optimal schedule student
	 *                 x is doing step y
	 */
	public void printResults(int[][] schedule) {
		for (int student = 1; student < schedule.length; student++) {
			int[] curStudentSchedule = schedule[student];
			System.out.print("Student " + student + ": ");
			for (int step = 1; step < curStudentSchedule.length; step++) {
				if (curStudentSchedule[step] == 1) {
					System.out.print(step + " ");
				}
			}
			System.out.println("");
		}
	}

	/**
	 * This validates the input data about the experiment step sign-ups.
	 * 
	 * @param numStudents the number of students
	 * @param numSteps    the number of steps
	 * @param signUps     the data given about which steps each student can do
	 * @return true or false whether the input sign-ups match the given number of
	 *         students and steps, and whether all the steps are guaranteed at least
	 *         one student.
	 */
	public boolean inputsValid(int numStudents, int numSteps, int signUps[][]) {
		int studentSignUps = signUps.length;

		// Check if there are any students or signups
		if (numStudents < 1 || studentSignUps < 1) {
			System.out.println("You either did not put in any student or any signups");
			return false;
		}

		// Check if the number of students and sign-up rows matches
		if (numStudents != studentSignUps) {
			System.out.println("You input " + numStudents + " students but your signup suggests " + signUps.length);
			return false;
		}

		// Check that all steps are guaranteed in the signups
		int[] stepsGuaranteed = new int[numSteps + 1];
		for (int i = 0; i < studentSignUps; i++) {
			for (int j = 0; j < signUps[i].length; j++) {
				stepsGuaranteed[signUps[i][j]] = 1;
			}
		}
		for (int step = 1; step <= numSteps; step++) {
			if (stepsGuaranteed[step] != 1) {
				System.out.println("Your signup is incomplete because not all steps are guaranteed.");
				return false;
			}
		}

		return true;
	}

	/**
	 * This sets up the scheduling test case and calls the scheduling method.
	 * 
	 * @param numStudents the number of students
	 * @param numSteps    the number of steps
	 * @param signUps     which steps each student can do, in order of students and
	 *                    steps
	 */
	public void makeExperimentAndSchedule(int experimentNum, int numStudents, int numSteps, int[][] signUps) {
		System.out.println("----Experiment " + experimentNum + "----");
		if (!inputsValid(numStudents, numSteps, signUps)) {
			System.out.println("Experiment signup info is invalid");
			return;
		}
		int[][] signUpsLookup = makeSignUpLookup(numSteps, signUps);
		int[][] schedule = scheduleExperiments(numStudents, numSteps, signUpsLookup);
		printResults(schedule);
		System.out.println("");
	}

	/**
	 * You can make additional test cases using the same format. In fact the helper
	 * functions I've provided will even check your test case is set up correctly.
	 * Do not touch any of of the existing lines. Just make sure to comment out or
	 * delete any of your own test cases when you submit. The three experiment test
	 * cases existing in this main method should be the only output when running
	 * this file.
	 */
	public static void main(String args[]) {
		PhysicsExperiment pe = new PhysicsExperiment();

		// Experiment 1: Example 1 from README, 3 students, 6 steps:
		int[][] signUpsExperiment1 = { { 1, 2, 3, 5 }, { 2, 3, 4 }, { 1, 4, 5, 6 } };
		pe.makeExperimentAndSchedule(1, 3, 6, signUpsExperiment1);

		// Experiment 2: Example 2 from README, 4 students, 8 steps
		int[][] signUpsExperiment2 = { { 5, 7, 8 }, { 2, 3, 4, 5, 6 }, { 1, 5, 7, 8 }, { 1, 3, 4, 8 } };
		pe.makeExperimentAndSchedule(2, 4, 8, signUpsExperiment2);

		// Experiment 3: Another test case, 5 students, 11 steps
		int[][] signUpsExperiment3 = { { 7, 10, 11 }, { 8, 9, 10 }, { 2, 3, 4, 5, 7 }, { 1, 5, 6, 7, 8 },
				{ 1, 3, 4, 8 } };
		pe.makeExperimentAndSchedule(3, 5, 11, signUpsExperiment3);
		
		/*
		 * 
		 * 
		// Extra Test Case
		int[][] signUpsExperiment4 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9, 10, 11 }, { 1, 5, 6, 7, 8 },
				{ 1, 3, 4, 8 } };
		pe.makeExperimentAndSchedule(4, 5, 11, signUpsExperiment4);
	   	*
	    *
	    */
	}
}
