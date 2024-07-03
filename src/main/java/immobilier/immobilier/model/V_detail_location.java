package immobilier.immobilier.model;

import immobilier.immobilier.annotation.ColumnField;
import immobilier.immobilier.annotation.Getter;
import immobilier.immobilier.annotation.Setter;
import immobilier.immobilier.annotation.TableName;
import immobilier.immobilier.table.JDBC;
import java.sql.Connection;
import java.util.LinkedList;

@TableName(database = "immobilier", driver = "postgres", name = "v_detail_location", password = "366325", user = "postgres")
public class V_detail_location extends JDBC {

	@ColumnField(columnName = "idproprio")
	String idproprio;
	@ColumnField(columnName = "proprio")
	String proprio;
	@ColumnField(columnName = "idbiens")
	String idbiens;
	@ColumnField(columnName = "biens")
	String biens;
	@ColumnField(columnName = "region")
	String region;
	@ColumnField(columnName = "type")
	String type;
	@ColumnField(columnName = "idlocation")
	String idlocation;
	@ColumnField(columnName = "date")
	String date;
	@ColumnField(columnName = "date_debut")
	String date_debut;
	@ColumnField(columnName = "ca")
	Double ca;
	@ColumnField(columnName = "co")
	Double co;
	@ColumnField(columnName = "commission_pourcentage")
	String commission_pourcentage;
	@ColumnField(columnName = "gains")
	Double gains;
	@ColumnField(columnName = "num")
	Integer num;
	@ColumnField(columnName = "montant")
	Double montant;
	@ColumnField(columnName = "statut_paiement")
	String statut_paiement;

	public String getColors() {
        String color = "";
        try {
			if(this.getStatut_paiement().equals("P")){
				color = "bg-success";
			}else if(this.getStatut_paiement().equals("NP")){
				color = "bg-warning";
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return color;
    }


	public LinkedList<V_detail_location> getAllV_detail_locationsByIdLoc(String id)throws Exception{
		LinkedList<V_detail_location> locs = new V_detail_location().select("where idlocation = '"+id+"'");
		return locs;
	}

	public LinkedList<V_detail_location> getAllV_detail_locations()throws Exception{
		LinkedList<V_detail_location> locs = new V_detail_location().select();
		return locs;
	}

	public V_detail_location() throws Exception {

	}

	public V_detail_location(String idproprio, String proprio, String idbiens, String biens, String region, String type,
			String idlocation, String date, String date_debut, Double ca, Double co, String commission_pourcentage,
			Double gains, Integer num, Double montant, String statut_paiement) throws Exception {
		setIdproprio(idproprio.trim());
		setProprio(proprio.trim());
		setIdbiens(idbiens.trim());
		setBiens(biens.trim());
		setRegion(region.trim());
		setType(type.trim());
		setIdlocation(idlocation.trim());
		setDate(date.trim());
		setDate_debut(date_debut.trim());
		setCa(ca);
		setCo(co);
		setCommission_pourcentage(commission_pourcentage.trim());
		setGains(gains);
		setNum(num);
		setMontant(montant);
		setStatut_paiement(statut_paiement.trim());
	}

	public int count(Connection connection) throws Exception {
		int count = 0;
		try {
			count = select(connection).size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Getter(columnName = "idproprio")
	public String getIdproprio() {
		return this.idproprio;
	}

	@Setter(columnName = "idproprio")
	public void setIdproprio(String idproprio) {
		this.idproprio = idproprio;
	}

	@Getter(columnName = "proprio")
	public String getProprio() {
		return this.proprio;
	}

	@Setter(columnName = "proprio")
	public void setProprio(String proprio) {
		this.proprio = proprio;
	}

	@Getter(columnName = "idbiens")
	public String getIdbiens() {
		return this.idbiens;
	}

	@Setter(columnName = "idbiens")
	public void setIdbiens(String idbiens) {
		this.idbiens = idbiens;
	}

	@Getter(columnName = "biens")
	public String getBiens() {
		return this.biens;
	}

	@Setter(columnName = "biens")
	public void setBiens(String biens) {
		this.biens = biens;
	}

	@Getter(columnName = "region")
	public String getRegion() {
		return this.region;
	}

	@Setter(columnName = "region")
	public void setRegion(String region) {
		this.region = region;
	}

	@Getter(columnName = "type")
	public String getType() {
		return this.type;
	}

	@Setter(columnName = "type")
	public void setType(String type) {
		this.type = type;
	}

	@Getter(columnName = "idlocation")
	public String getIdlocation() {
		return this.idlocation;
	}

	@Setter(columnName = "idlocation")
	public void setIdlocation(String idlocation) {
		this.idlocation = idlocation;
	}

	@Getter(columnName = "date")
	public String getDate() {
		return this.date;
	}

	@Setter(columnName = "date")
	public void setDate(String date) {
		this.date = date;
	}

	@Getter(columnName = "date_debut")
	public String getDate_debut() {
		return this.date_debut;
	}

	@Setter(columnName = "date_debut")
	public void setDate_debut(String date_debut) {
		this.date_debut = date_debut;
	}

	@Getter(columnName = "ca")
	public Double getCa() {
		return this.ca;
	}

	@Setter(columnName = "ca")
	public void setCa(Double ca) {
		this.ca = ca;
	}

	@Getter(columnName = "co")
	public Double getCo() {
		return this.co;
	}

	@Setter(columnName = "co")
	public void setCo(Double co) {
		this.co = co;
	}

	@Getter(columnName = "commission_pourcentage")
	public String getCommission_pourcentage() {
		return this.commission_pourcentage;
	}

	@Setter(columnName = "commission_pourcentage")
	public void setCommission_pourcentage(String commission_pourcentage) {
		this.commission_pourcentage = commission_pourcentage;
	}

	@Getter(columnName = "gains")
	public Double getGains() {
		return this.gains;
	}

	@Setter(columnName = "gains")
	public void setGains(Double gains) {
		this.gains = gains;
	}

	@Getter(columnName = "num")
	public Integer getNum() {
		return this.num;
	}

	@Setter(columnName = "num")
	public void setNum(Integer num) {
		this.num = num;
	}

	@Getter(columnName = "montant")
	public Double getMontant() {
		return this.montant;
	}

	@Setter(columnName = "montant")
	public void setMontant(Double montant) {
		this.montant = montant;
	}

	@Getter(columnName = "statut_paiement")
	public String getStatut_paiement() {
		return this.statut_paiement;
	}

	@Setter(columnName = "statut_paiement")
	public void setStatut_paiement(String statut_paiement) {
		this.statut_paiement = statut_paiement;
	}

}
