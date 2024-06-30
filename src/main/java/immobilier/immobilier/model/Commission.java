package immobilier.immobilier.model;
import immobilier.immobilier.annotation.ColumnField; 
import immobilier.immobilier.annotation.Getter; 
import immobilier.immobilier.annotation.Setter; 
import immobilier.immobilier.annotation.TableName; 
import immobilier.immobilier.table.JDBC; 
import java.sql.Connection; 


@TableName(database = "immobilier", driver = "postgres", name = "commission", password = "366325", user = "postgres")
public class Commission extends JDBC {

    @ColumnField(columnName = "id" ,primaryKey = true, defaultValue = true ) 
String id;
    @ColumnField(columnName = "idtype" ) 
String idtype;
    @ColumnField(columnName = "valeur" ) 
String valeur;
    

    public Commission() throws Exception{

    }

    public Commission(String idtype ,String valeur)throws Exception{
setIdtype(idtype.trim()); 
setValeur(valeur.trim()); 
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

	 @Getter(columnName = "idtype")
	public String getIdtype() {
      	return this.idtype; 
	} 

	 @Setter(columnName = "idtype")
	public void setIdtype(String idtype) { 
      	this.idtype=idtype;
	} 

	 @Getter(columnName = "valeur")
	public String getValeur() {
      	return this.valeur; 
	} 

	 @Setter(columnName = "valeur")
	public void setValeur(String valeur) { 
      	this.valeur=valeur;
	} 




}
