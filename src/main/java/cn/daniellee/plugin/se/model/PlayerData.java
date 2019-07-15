package cn.daniellee.plugin.se.model;

public class PlayerData {

	private String name;

	private int battleTotal;

	private int battleGem;

	private int lifeTotal;

	private int lifeGem;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBattleTotal() {
		return battleTotal;
	}

	public void setBattleTotal(int battleTotal) {
		this.battleTotal = battleTotal;
	}

	public int getBattleGem() {
		return battleGem;
	}

	public void setBattleGem(int battleGem) {
		this.battleGem = battleGem;
	}

	public int getLifeTotal() {
		return lifeTotal;
	}

	public void setLifeTotal(int lifeTotal) {
		this.lifeTotal = lifeTotal;
	}

	public int getLifeGem() {
		return lifeGem;
	}

	public void setLifeGem(int lifeGem) {
		this.lifeGem = lifeGem;
	}
}
