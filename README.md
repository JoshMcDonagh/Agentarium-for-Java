# Agentarium for Java

**Agentarium** is a modular, extensible, and multithreaded agent-based modelling (ABM) framework for Java. It provides a flexible architecture for defining agents, environments, and behaviours using composable attributes, and supports high-performance simulation with optional cross-thread coordination.

## Features

- **Attribute-based modelling** of agents and environments
- **Multi-core execution** with thread-safe synchronisation
- **Pluggable schedulers** for ordered or random agent execution
- **Configurable results recording** to memory or SQLite
- **Integrated environment simulation** each tick
- **Testable and functional hooks** for use in hybrid Java/Python workflows

## Core Concepts

- `ModelElement`: Base class for agents and environments
- `Attribute` / `Property` / `Event`: Defines state and behaviour run each tick
- `AttributeSet`: Groups related attributes and controls execution
- `AttributeSetCollection`: Attached to a ModelElement, runs all its sets
- `AgentSet`: Collection of named agents
- `AgentGenerator`: Defines how agents are created in the model
- `Environment`: Shared, ticked component run after agents
- `EnvironmentGenerator`: Defines how the environment is created for the model
- `Scheduler`: Defines tick policy (in order, random, or functional)
- `Results`: Stores raw and processed simulation data
