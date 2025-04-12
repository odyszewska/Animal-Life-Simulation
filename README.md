# 🦁 Animal Life Simulation Project

A Java-based simulation that visualizes the evolution of animal life.  
This project was developed in pairs as part of the **Object-Oriented Programming** course at AGH University of Science and Technology.

---

## 📖 Project Overview

Simulation models a rectangular world filled with animals and plants. Animals wander through map, search for food, reproduce, and gradually evolve over time.

The simulation unfolds as an automated, observable ecosystem — our role is not to play but to observe evolution in action.

---

## 🌍 World Mechanics

- **Terrain:** A grid-based map composed of grassland and jungle zones. Plants grow more frequently in the jungle.
- **Animals:** Each has a position, direction, energy level, and a genetic code influencing its movement behavior.
- **Movement:** Animals move daily based on their active gene, rotate accordingly, and consume energy.
- **Feeding:** Animals gain energy by consuming plants they encounter.
- **Reproduction:** When two animals meet on the same tile and both have enough energy, they reproduce. The offspring inherits a mix of genes from both parents, with possible mutations.

---

## 🔁 Simulation Cycle

Each simulation day includes the following steps:

1. Remove dead animals  
2. Animal rotation and movement  
3. Consumption of plants  
4. Reproduction of animals on the same tile  
5. New plant growth

---

## ⚙️ Configurable Parameters

The simulation allows configuration of various parameters:

- Map size   
- Initial number of animals and plants  
- Energy values (starting, reproduction threshold, plant energy gain, etc.)  
- Mutation types
- Plant growth  

### Supported Variants

#### 🌱 Plant Growth Strategies
- Equator-focused 
- Corpse-based (growth near recent deaths)  

#### 🧬 Mutation Types
- Full randomness  
- Slight value change  

---

## 📦 How to Run

To build and run the project:

1. Use **Gradle** to build the project  
2. Run **Main.java** located at `src/main/java/main/Main.java`
