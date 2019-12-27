package Agents;

public abstract class Agent {
    /**
     * @return текущее состояние робота
     */
    protected abstract String getStatus();

    /**
     * @return имя агента
     */
    protected abstract String getAgentName();
}
