package immobilier.immobilier.model;
import immobilier.immobilier.annotation.ColumnField; 
import immobilier.immobilier.annotation.Getter; 
import immobilier.immobilier.annotation.Setter; 
import immobilier.immobilier.annotation.TableName; 
import immobilier.immobilier.table.JDBC; 
import java.sql.Connection;
import java.sql.Date; 


@TableName(database = "immobilier", driver = "postgres", name = "location", password = "366325", user = "postgres")
public class Location extends JDBC {

    @ColumnField(columnName = "id" ,primaryKey = true, defaultValue = true ) 
String id;
    @ColumnField(columnName = "idbiens" ) 
String idbiens;
    @ColumnField(columnName = "idclient" ) 
String idclient;
    @ColumnField(columnName = "duree" ) 
Double duree;
    @ColumnField(columnName = "date_debut" ) 
Date date_debut;
    @ColumnField(columnName = "date_fin" ) 
Date date_fin;
    

    public Location() throws Exception{

    }

    public Location(String idbiens ,String idclient ,Double duree ,Date date_debut ,Date date_fin)throws Exception{
setIdbiens(idbiens.trim()); 
setIdclient(idclient.trim()); 
setDuree( duree); 
setDate_debut( date_debut); 
setDate_fin( date_fin); 
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

    
    	 @Getter(columnName = "id")
	public String getId() {
      	return this.id; 
	} 

	 @Setter(columnName = "id")
	public void setId(String id) { 
      	this.id=id;
	} 

	 @Getter(columnName = "idbiens")
	public String getIdbiens() {
      	return this.idbiens; 
	} 

	 @Setter(columnName = "idbiens")
	public void setIdbiens(String idbiens) { 
      	this.idbiens=idbiens;
	} 

	 @Getter(columnName = "idclient")
	public String getIdclient() {
      	return this.idclient; 
	} 

	 @Setter(columnName = "idclient")
	public void setIdclient(String idclient) { 
      	this.idclient=idclient;
	} 

	 @Getter(columnName = "duree")
	public Double getDuree() {
      	return this.duree; 
	} 

	 @Setter(columnName = "duree")
	public void setDuree(Double duree) { 
      	this.duree=duree;
	} 

	 @Getter(columnName = "date_debut")
	public Date getDate_debut() {
      	return this.date_debut; 
	} 

	 @Setter(columnName = "date_debut")
	public void setDate_debut(Date date_debut) { 
      	this.date_debut=date_debut;
	} 

	public void setDate_debut(String date_debut) {
		Date daty_temp = Date.valueOf(date_debut.trim());
setDate_debut(daty_temp);
	} 

	 @Getter(columnName = "date_fin")
	public Date getDate_fin() {
      	return this.date_fin; 
	} 

	 @Setter(columnName = "date_fin")
	public void setDate_fin(Date date_fin) { 
      	this.date_fin=date_fin;
	} 

	public void setDate_fin(String date_fin) {
		Date daty_temp = Date.valueOf(date_fin.trim());
setDate_fin(daty_temp);
	} 




}
