package bluedazzled.lucy_atmos.atmospherics.defines;

public abstract class gas {
    protected final String id;
    protected final double specific_heat;
    protected final String name;
    //Assuming it's relative to textures/gasoverlay, might change later
    protected final boolean gas_overlay;
    /// relative rarity compared to other gases, used when seting up the reactions list
    protected final int rarity;
    protected final String description;

    protected gas(String id, double specific_heat, String name, boolean gas_overlay, int rarity, String description) {
        this.id = id;
        this.specific_heat = specific_heat;
        this.name = name;
        this.gas_overlay = gas_overlay;
        this.rarity = rarity;
        this.description = description;
    }
    public String getId() {return id;}
    public double getSpecific_heat() {return specific_heat;}
    public String getName() {return name;}
    public boolean hasGas_overlay() {return gas_overlay;}
    public int getRarity() {return rarity;}
    public String getDescription() {return description;}
}
