package map.kll.org.brickkilnnew.library;


import java.util.ArrayList;

public  class BrickKiln {
    public String name;
    public String city;
    public Double latitude;
    public Double longitude;
    public String ownership;
    public String market;
    public String operating_seasons;
    public String days_open;


    public String raw_material;
    public String fuel;
    public String fuel_quantity;
    public String brick_kind;
    public String chimney_cat;
    public String chimney_height;
    public String chimney_number;
    public String moulding_process;
    public String firing;
    public String capacity;
    public String brick_per_batch;
    public String quality;

    public String labor_children;
    public String labor_male;
    public String labor_female;
    public String labor_total;
    public String labor_young;
    public String labor_old;
    public String labor_currently_studying;
    public String labor_slc;
    public String labor_informal_edu;
    public String labor_illiterate;
    public String food_allowance;

    public ArrayList<String> image;


    public BrickKiln(String nameKiln, Double lat, Double lon, String cityKiln, String ownershipKiln, String marketKiln, String operatingSeasonKiln, String daysOpenKiln,
                     String raw_matetrialKiln, String fuelKiln, String fuel_quantitykiln, String brick_kindKiln, String chimney_catKiln, String chimney_heightKiln,
                     String chimney_numberKiln, String moulding_processKiln, String firingKiln, String capacityKiln, String brick_per_batchKiln, String qualityKiln,
                     String labor_childrenKiln, String labor_maleKiln, String labor_femaleKiln, String labor_totalKiln, String labor_youngKiln, String labor_oldKiln, String labor_currently_studyingKiln, String labor_slcKiln, String labor_informal_eduKiln, String labor_illiterateKiln, String food_allowanceKiln,
                     ArrayList<String> imageKiln){
        this.name = nameKiln;
        this.latitude = lat;
        this.longitude = lon;
        this.city = cityKiln;
        this.ownership = ownershipKiln;
        this.market= marketKiln;
        this.operating_seasons = operatingSeasonKiln;
        this.days_open = daysOpenKiln;
        this.raw_material = raw_matetrialKiln;
        this.fuel = fuelKiln;
        this.fuel_quantity = fuel_quantitykiln;
        this.brick_kind = brick_kindKiln;
        this.chimney_cat = chimney_catKiln;
        this.chimney_height = chimney_heightKiln;
        this.chimney_number = chimney_numberKiln;
        this.moulding_process = moulding_processKiln;
        this.firing = firingKiln;
        this.capacity = capacityKiln;
        this.brick_per_batch = brick_per_batchKiln;
        this.quality = qualityKiln;
        this.labor_children = labor_childrenKiln;
        this.labor_male = labor_maleKiln;
        this.labor_female = labor_femaleKiln;
        this.labor_total = labor_totalKiln;
        this.labor_young = labor_youngKiln;
        this.labor_old = labor_oldKiln;
        this.labor_currently_studying = labor_currently_studyingKiln;
        this.labor_slc = labor_slcKiln;
        this.labor_informal_edu = labor_informal_eduKiln;
        this.labor_illiterate = labor_illiterateKiln;
        this.food_allowance = food_allowanceKiln;
        this.image = imageKiln;

    }



}