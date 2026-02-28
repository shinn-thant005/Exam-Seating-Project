package main_Project;

import java.util.*;

//Student class
class Student {
 int id;
 String department;

 public Student(int id, String department) {
     this.id = id;
     this.department = department;
 }
}

//Individual (Chromosome)
class Individual {
 Student[][] seating;
 int fitness;

 public Individual(int rows, int cols) {
     seating = new Student[rows][cols];
     fitness = 0;
 }
}

public class ExamSeatingGA {

 // GA Parameters
 static final int POPULATION_SIZE = 100;
 static final int GENERATIONS = 300;
 static final double MUTATION_RATE = 0.05;

 static int rows = 5;
 static int cols = 6;

 static List<Student> students = new ArrayList<>();
 static Random random = new Random();

 // ================= MAIN FUNCTION =================
 public static void main(String[] args) {

     // Create students
     createStudents();

     // Initialize population
     List<Individual> population = initializePopulation();

     // Evolution process
     for (int generation = 0; generation < GENERATIONS; generation++) {

         // Calculate fitness
         for (Individual ind : population) {
             ind.fitness = calculateFitness(ind);
         }

         // Sort population by fitness (descending)
         population.sort((a, b) -> b.fitness - a.fitness);

         // Create new population
         List<Individual> newPopulation = new ArrayList<>();

         // Elitism: Keep best 10 individuals
         for (int i = 0; i < 10; i++) {
             newPopulation.add(population.get(i));
         }

         // Generate remaining individuals
         while (newPopulation.size() < POPULATION_SIZE) {

             Individual parent1 = tournamentSelection(population);
             Individual parent2 = tournamentSelection(population);

             Individual child = crossover(parent1, parent2);

             if (random.nextDouble() < MUTATION_RATE) {
                 mutate(child);
             }

             newPopulation.add(child);
         }

         population = newPopulation;
     }

     // Final evaluation
     for (Individual ind : population) {
         ind.fitness = calculateFitness(ind);
     }

     population.sort((a, b) -> b.fitness - a.fitness);

     Individual best = population.get(0);

     // Print result
     System.out.println("Best Exam Seating Plan:\n");
     printSeating(best);
     System.out.println("\nBest Fitness Score: " + best.fitness);
 }

 // ================= CREATE STUDENTS =================
 static void createStudents() {
     for (int i = 0; i < rows * cols; i++) {
         String dept;
         if (i % 3 == 0)
             dept = "CSE";
         else if (i % 3 == 1)
             dept = "ECE";
         else
             dept = "ME";

         students.add(new Student(i + 1, dept));
     }
 }

 // ================= INITIAL POPULATION =================
 static List<Individual> initializePopulation() {

     List<Individual> population = new ArrayList<>();

     for (int i = 0; i < POPULATION_SIZE; i++) {

         Individual ind = new Individual(rows, cols);

         List<Student> shuffled = new ArrayList<>(students);
         Collections.shuffle(shuffled);

         Iterator<Student> iterator = shuffled.iterator();

         for (int r = 0; r < rows; r++) {
             for (int c = 0; c < cols; c++) {
                 ind.seating[r][c] = iterator.next();
             }
         }

         population.add(ind);
     }

     return population;
 }

 // ================= FITNESS FUNCTION =================
 static int calculateFitness(Individual ind) {

     int fitness = 0;

     for (int r = 0; r < rows; r++) {
         for (int c = 0; c < cols; c++) {

             Student current = ind.seating[r][c];

             // Check right neighbor
             if (c + 1 < cols) {
                 if (!current.department.equals(ind.seating[r][c + 1].department)) {
                     fitness++;
                 }
             }

             // Check bottom neighbor
             if (r + 1 < rows) {
                 if (!current.department.equals(ind.seating[r + 1][c].department)) {
                     fitness++;
                 }
             }
         }
     }

     return fitness;
 }

 // ================= TOURNAMENT SELECTION =================
 static Individual tournamentSelection(List<Individual> population) {

     Individual best = null;

     for (int i = 0; i < 5; i++) {
         Individual ind = population.get(random.nextInt(POPULATION_SIZE));

         if (best == null || ind.fitness > best.fitness) {
             best = ind;
         }
     }

     return best;
 }

 // ================= CROSSOVER =================
 static Individual crossover(Individual p1, Individual p2) {

     Individual child = new Individual(rows, cols);
     Set<Integer> used = new HashSet<>();

     int cutPoint = random.nextInt(rows * cols);
     int index = 0;

     // Copy first part from parent1
     for (int r = 0; r < rows; r++) {
         for (int c = 0; c < cols; c++) {

             if (index < cutPoint) {
                 child.seating[r][c] = p1.seating[r][c];
                 used.add(p1.seating[r][c].id);
             }
             index++;
         }
     }

     // Fill remaining from parent2
     for (int r = 0; r < rows; r++) {
         for (int c = 0; c < cols; c++) {

             if (child.seating[r][c] == null) {
                 for (int i = 0; i < rows; i++) {
                     for (int j = 0; j < cols; j++) {

                         Student candidate = p2.seating[i][j];

                         if (!used.contains(candidate.id)) {
                             child.seating[r][c] = candidate;
                             used.add(candidate.id);
                             break;
                         }
                     }
                     if (child.seating[r][c] != null)
                         break;
                 }
             }
         }
     }

     return child;
 }

 // ================= MUTATION =================
 static void mutate(Individual ind) {

     int r1 = random.nextInt(rows);
     int c1 = random.nextInt(cols);
     int r2 = random.nextInt(rows);
     int c2 = random.nextInt(cols);

     Student temp = ind.seating[r1][c1];
     ind.seating[r1][c1] = ind.seating[r2][c2];
     ind.seating[r2][c2] = temp;
 }

 // ================= PRINT SEATING =================
 static void printSeating(Individual ind) {

     for (int r = 0; r < rows; r++) {
         for (int c = 0; c < cols; c++) {
             System.out.print(ind.seating[r][c].department + "-"
                     + ind.seating[r][c].id + "\t");
         }
         System.out.println();
     }
 }
}