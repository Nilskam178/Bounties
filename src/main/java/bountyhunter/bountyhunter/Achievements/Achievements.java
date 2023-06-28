package bountyhunter.bountyhunter.Achievements;

public enum Achievements {
    ACHIEVEMENT_0("Rookie Wrangler", 1),
    ACHIEVEMENT_1("Bounty Bandit", 5),
    ACHIEVEMENT_2("Master Marauder", 10),
    ACHIEVEMENT_3("Savage Slayer", 50),
    ACHIEVEMENT_4("Elite Executioner", 100),
    ACHIEVEMENT_5("Legendary Lawbreaker", 500),
    ACHIEVEMENT_6("Supreme Scoundrel", 1000),
    ACHIEVEMENT_7("The Big Earner", 10000),
    ACHIEVEMENT_8("The Six-Figure Sensation", 100000),
    ACHIEVEMENT_9("The Money Magnet", 1000000),
    ACHIEVEMENT_10("The Multi-Millionaire Mogul", 10000000),
    ACHIEVEMENT_11("The Hundred-Million Hero", 100000000);

    private final String name;
    private final double requirement;

    Achievements(String name, double requirement) {
        this.name = name;
        this.requirement = requirement;
    }

    public String getName() {
        return name;
    }

    public double getRequirement() {
        return requirement;
    }
}
