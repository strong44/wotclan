package com.wot.shared;

import java.io.Serializable;

public class DataCommunityAccountRatings implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2580719093758162023L;
	
	  private DataCommunityAccountRatingsElement spotted = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement dropped_ctf_points = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement battle_avg_xp = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement xp = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement battles = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement damage_dealt = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement ctf_points = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement integrated_rating = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement battle_avg_performance = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement battle_avg_performanceCalc = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement frags = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement battle_wins = new DataCommunityAccountRatingsElement();
	  //info calculée
	
	  private DataCommunityAccountRatingsElement ratioCtfPoints = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement ratioDamagePoints = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement ratioDroppedCtfPoints = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement ratioDestroyedPoints = new DataCommunityAccountRatingsElement();
	
	  private DataCommunityAccountRatingsElement ratioDetectedPoints = new DataCommunityAccountRatingsElement();

	private Double averageLevelTankCalc;
	
	

	public Double getRatioDetectedPoints() {
		return ratioDetectedPoints.getValue();
	}
	public Double getRatioDestroyedPoints() {
		return ratioDestroyedPoints.getValue();
	}
	public void setRatioDestroyedPoints(Double ratioDestroyedPoints) {
		this.ratioDestroyedPoints.setValue( ratioDestroyedPoints);
	}
	public Double getRatioDroppedCtfPoints() {
		return ratioDroppedCtfPoints.getValue();
	}
	public Double getRatioDamagePoints() {
		return ratioDamagePoints.getValue();
	}
	public Double getRatioCtfPoints() {
		return ratioCtfPoints.getValue();
	}
	public int getSpotted() {
		return spotted.getValue().intValue();
	}
	public void setSpotted(Double spotted) {
		this.spotted.setValue( spotted);
	}
	public int getDropped_ctf_points() {
		return dropped_ctf_points.getValue().intValue();
	}
	public void setDropped_ctf_points(Double  dropped_ctf_points) {
		this.dropped_ctf_points.setValue( dropped_ctf_points);
	}
	public int getBattle_avg_xp() {
		return battle_avg_xp.getValue().intValue();
	}
	public void setBattle_avg_xp(Double battle_avg_xp) {
		this.battle_avg_xp.setValue(battle_avg_xp);
	}
	public int getBattles() {
		return battles.getValue().intValue();
	}
	public void setBattles(Double battles) {
		this.battles.setValue( battles);
	}
	public int getDamage_dealt() {
		return damage_dealt.getValue().intValue();
	}
	public void setDamage_dealt(Double damage_dealt) {
		this.damage_dealt.setValue( damage_dealt);
	}
	
	//win rate
	public int getBattle_avg_performance() {
		return battle_avg_performance.getValue().intValue();
	}
	public void setBattle_avg_performance(Double battle_avg_performance) {
		this.battle_avg_performance.setValue( battle_avg_performance);
	}
	public int getIntegrated_rating() {
		return integrated_rating.getValue().intValue();
	}
	public void setIntegrated_rating(Double integrated_rating) {
		this.integrated_rating.setValue(integrated_rating);
	}
	public int getFrags() {
		return frags.getValue().intValue();
	}
	public void setFrags(Double frags) {
		this.frags.setValue(frags);
	}
	public int getXp() {
		return xp.getValue().intValue();
	}
	public void setXp(Double xp) {
		this.xp.setValue(xp);
	}
	public int getCtf_points() {
		return ctf_points.getValue().intValue();
	}
	public void setCtf_points(Double ctf_points) {
		this.ctf_points.setValue(ctf_points);
	}
	public int getBattle_wins() {
		return battle_wins.getValue().intValue();
	}
	public void setBattle_wins(Double battle_wins) {
		this.battle_wins.setValue(battle_wins);
	}
	  
	public Double getBattle_avg_performanceCalc() {
		return battle_avg_performanceCalc.getValue();
	}
	public void setBattle_avg_performanceCalc(Double battle_avg_performanceCalc) {
		this.battle_avg_performanceCalc.setValue(battle_avg_performanceCalc);
	}
	public void setRatioCtfPoints(Double ctfPointsCal) {
		// TODO Auto-generated method stub
		this.ratioCtfPoints.setValue(ctfPointsCal );
	}
	public void setRatioDamagePoints(Double ratioFragsPoints) {
		// TODO Auto-generated method stub
		this.ratioDamagePoints.setValue(ratioFragsPoints);
	}
	public void setRatioDroppedCtfPoints(Double ratioDroppedCtfPoints) {
		// TODO Auto-generated method stub
		this.ratioDroppedCtfPoints.setValue(ratioDroppedCtfPoints);
	}
	public void setRatioDetectedPoints(Double ratioDetectedPoints) {
		this.ratioDetectedPoints.setValue(ratioDetectedPoints);
		
	}
	public void setAverageLevel(Double averageLevelTank) {
		// TODO Auto-generated method stub
		this.averageLevelTankCalc = averageLevelTank;
	}
	public Double getAverageLevel() {
		// TODO Auto-generated method stub
		return this.averageLevelTankCalc;
	}
	
}
