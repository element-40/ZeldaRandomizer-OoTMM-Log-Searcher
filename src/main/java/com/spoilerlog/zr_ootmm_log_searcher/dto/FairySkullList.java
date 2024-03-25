package com.spoilerlog.zr_ootmm_log_searcher.dto;

public class FairySkullList {
    private GreatBayStrayFairyList greatBayStrayFairyList;
    private WoodfallStrayFairyList woodfallStrayFairyList;
    private SnowheadStrayFairyList snowheadStrayFairyList;
    private StoneTowerStrayFairyList stoneTowerStrayFairyList;
    private SwampSkullList swampSkullList;
    private OceanSkullList oceanSkullList;

    public FairySkullList() {
        this.greatBayStrayFairyList = new GreatBayStrayFairyList();
        this.woodfallStrayFairyList = new WoodfallStrayFairyList();
        this.snowheadStrayFairyList = new SnowheadStrayFairyList();
        this.stoneTowerStrayFairyList = new StoneTowerStrayFairyList();
        this.swampSkullList = new SwampSkullList();
        this.oceanSkullList = new OceanSkullList();
    }

    public GreatBayStrayFairyList getGreatBayStrayFairyList() {
        return greatBayStrayFairyList;
    }

    public void setGreatBayStrayFairyList(GreatBayStrayFairyList greatBayStrayFairyList) {
        this.greatBayStrayFairyList = greatBayStrayFairyList;
    }

    public WoodfallStrayFairyList getWoodfallStrayFairyList() {
        return woodfallStrayFairyList;
    }

    public void setWoodfallStrayFairyList(WoodfallStrayFairyList woodfallStrayFairyList) {
        this.woodfallStrayFairyList = woodfallStrayFairyList;
    }

    public SnowheadStrayFairyList getSnowheadStrayFairyList() {
        return snowheadStrayFairyList;
    }

    public void setSnowheadStrayFairyList(SnowheadStrayFairyList snowheadStrayFairyList) {
        this.snowheadStrayFairyList = snowheadStrayFairyList;
    }

    public StoneTowerStrayFairyList getStoneTowerStrayFairyList() {
        return stoneTowerStrayFairyList;
    }

    public void setStoneTowerStrayFairyList(StoneTowerStrayFairyList stoneTowerStrayFairyList) {
        this.stoneTowerStrayFairyList = stoneTowerStrayFairyList;
    }

    public SwampSkullList getSwampSkullList() {
        return swampSkullList;
    }

    public void setSwampSkullList(SwampSkullList swampSkullList) {
        this.swampSkullList = swampSkullList;
    }

    public OceanSkullList getOceanSkullList() {
        return oceanSkullList;
    }

    public void setOceanSkullList(OceanSkullList oceanSkullList) {
        this.oceanSkullList = oceanSkullList;
    }
}
