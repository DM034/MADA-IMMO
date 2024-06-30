package immobilier.immobilier.model;
import immobilier.immobilier.annotation.ColumnField; 
import immobilier.immobilier.annotation.Getter; 
import immobilier.immobilier.annotation.Setter; 
import immobilier.immobilier.annotation.TableName; 
import immobilier.immobilier.table.JDBC; 
import java.sql.Connection; 


@TableName(database = "immobilier", driver = "postgres", name = "type_biens", password = "366325", user = "postgres")
public class Type_biens extends JDBC {

    @ColumnField(columnName = "id" ,primaryKey = true, defaultValue = true ) 
String id;
    @ColumnField(columnName = "nom" ) 
String nom;
    @ColumnField(columnName = "etat" ) 
String etat;
    

    public Type_biens() throws Exception{

    }

    public Type_biens(String nom ,String etat)throws Exception{
setNom(nom.trim()); 
setEtat(etat.trim()); 
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

	 @Getter(columnName = "nom")
	public String getNom() {
      	return this.nom; 
	} 

	 @Setter(columnName = "nom")
	public void setNom(String nom) { 
      	this.nom=nom;
	} 

	 @Getter(columnName = "etat")
	public String getEtat() {
      	return this.etat; 
	} 

	 @Setter(columnName = "etat")
	public void setEtat(String etat) { 
      	this.etat=etat;
	} 




}
